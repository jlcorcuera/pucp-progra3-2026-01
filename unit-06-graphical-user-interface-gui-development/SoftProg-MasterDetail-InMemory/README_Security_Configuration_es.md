# Configuración de Seguridad — Referencia Técnica

Este documento proporciona una descripción técnica exhaustiva de la arquitectura de seguridad implementada en **SoftProg WebApp**, una aplicación Blazor Interactive Server construida sobre .NET 8. Está dirigido a estudiantes universitarios matriculados en cursos de ingeniería de software o desarrollo web, y presupone familiaridad con C#, el pipeline de solicitudes de ASP.NET Core y los conceptos básicos de autenticación HTTP.

---

## 🗂️ Estructura de Archivos de Seguridad

Todos los artefactos relacionados con la seguridad están organizados en dos ubicaciones dedicadas dentro del proyecto, siguiendo el principio de **separación de responsabilidades**:

```
SoftProg-WebApp/
│
├── Security/                               ← Lógica de autenticación del lado del servidor
│   ├── AuthExtensions.cs                   ← Registro de servicios (método de extensión)
│   └── AuthEndpoints.cs                    ← Endpoints HTTP de inicio y cierre de sesión
│
└── Components/
    ├── Security/                           ← Componentes Razor de seguridad
    │   ├── AccessDenied.razor              ← Componente de UI reutilizable "Acceso Denegado"
    │   └── RedirectToLogin.razor           ← Manejador de redirección para usuarios no autenticados
    ├── Pages/
    │   └── AccessDenied.razor              ← Página enrutable en /access-denied (usada por el middleware)
    └── Routes.razor                        ← Enrutador central con lógica de autorización
```

> [!NOTE]
> Centralizar los artefactos de seguridad en una carpeta dedicada `Security/` mejora la trazabilidad y el mantenimiento del sistema. Cualquier desarrollador que trabaje en el control de acceso puede localizar toda la lógica relevante en un único lugar predecible.

---

## 🔑 Cuentas de Usuario Predefinidas

Con fines de demostración y prueba, la aplicación incluye dos cuentas de usuario codificadas de forma fija con roles distintos:

| Usuario | Contraseña | Rol | Nombre de Pantalla |
|:---|:---|:---|:---|
| `admin` | `admin` | **Administrador** | Administrador Principal |
| `employee` | `employee` | **Empleado** | Empleado de Ventas |

> [!IMPORTANT]
> En un entorno de producción, las credenciales **nunca** deben codificarse de forma fija. Las cuentas de usuario deben almacenarse en una base de datos con contraseñas cifradas mediante hash (p. ej., utilizando BCrypt o ASP.NET Core Identity). El enfoque empleado aquí tiene una finalidad estrictamente pedagógica.

---

## 🛠️ Arquitectura del Sistema — Flujo de Solicitudes de Extremo a Extremo

El siguiente diagrama ilustra el flujo completo de una solicitud de usuario a través de las capas de seguridad de la aplicación:

```text
[ El usuario envía sus credenciales ]
        │
        ▼  POST /auth/login  (envío estándar de formulario HTML)
[ AuthEndpoints.cs ]  ──► Valida credenciales → construye ClaimsPrincipal → SignInAsync
        │
        ▼  Cookie HTTP cifrada emitida hacia el navegador
[ Middleware de ASP.NET Core ]  ──► UseAuthentication / UseAuthorization
        │
        ▼  El usuario navega a una página protegida
[ Routes.razor ]  ──► AuthorizeRouteView evalúa el atributo [Authorize] de la página
        │
        ├── ✅ Autenticado + rol correcto  ──► La página se renderiza
        │
        ├── ❌ Autenticado, rol insuficiente
        │       ├── Blazor (navegación interna)  ──► <AccessDenied />
        │       └── Middleware HTTP (acceso directo por URL)  ──► /access-denied
        │
        └── ❌ No autenticado
                ├── Blazor (navegación interna)  ──► <RedirectToLogin />
                └── Middleware HTTP  ──► / (pantalla de inicio de sesión)
```

Este diseño por capas garantiza que el control de acceso se aplique en **dos niveles independientes**: el pipeline de middleware HTTP (del lado del servidor) y el enrutador de componentes Blazor (del lado del cliente). Ambas capas deben estar alineadas para prevenir brechas de seguridad.

---

## 📋 Recorrido de Implementación

### Paso 1 — Registro de Servicios de Autenticación

**Archivo:** [`Security/AuthExtensions.cs`](SoftProg-WebApp/Security/AuthExtensions.cs)

Se define un **método de extensión** denominado `AddCookieAuth()` sobre `IServiceCollection` para encapsular todos los registros de servicios relacionados con la autenticación. Este patrón mantiene `Program.cs` conciso y hace que la configuración de autenticación sea cohesiva y de fácil localización.

```csharp
public static IServiceCollection AddCookieAuth(this IServiceCollection services)
{
    services
        .AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
        .AddCookie(options =>
        {
            options.LoginPath = "/";                       // Redirigir aquí si no está autenticado
            options.AccessDeniedPath = "/access-denied";   // Redirigir aquí si está autenticado pero no autorizado
            options.SlidingExpiration = true;
            options.ExpireTimeSpan = TimeSpan.FromHours(8);
        });

    services.AddAuthorization();
    services.AddCascadingAuthenticationState();  // Propaga el estado de autenticación como parámetro en cascada
    services.AddHttpContextAccessor();

    return services;
}
```

**Decisiones de diseño clave explicadas:**

> **¿Por qué `AddCascadingAuthenticationState()`?**  
> En Blazor Server, el estado de autenticación se expone a los componentes a través de un `CascadingParameter`. Sin este registro, componentes integrados como `<AuthorizeView>` y `<AuthorizeRouteView>` no recibirían el usuario autenticado actual y serían incapaces de evaluar las reglas de control de acceso.

> **¿Por qué `LoginPath = "/"`?**  
> El formulario de inicio de sesión se renderiza en la ruta raíz `/`. Cuando el middleware detecta una solicitud no autenticada hacia un recurso protegido, redirige automáticamente al usuario a esta ruta.

> **¿Por qué `AccessDeniedPath = "/access-denied"`?**  
> Esta ruta es distinta de `LoginPath`. Se invoca cuando un usuario **está autenticado** pero **no posee el rol requerido** para acceder a un recurso. Dirigir estos dos escenarios a destinos diferentes permite a la aplicación mostrar retroalimentación contextualmente apropiada al usuario.

---

### Paso 2 — Configuración del Pipeline de Middleware

**Archivo:** [`Program.cs`](SoftProg-WebApp/Program.cs)

```csharp
using SoftProg_WebApp.Security;

builder.Services.AddCookieAuth();       // Registrar todos los servicios de autenticación

app.UseAuthentication();                // Leer la cookie y poblar HttpContext.User
app.UseAuthorization();                 // Evaluar las políticas de acceso contra HttpContext.User

app.MapAuthEndpoints();                 // Registrar POST /auth/login y GET /auth/logout
```

> [!IMPORTANT]
> **El orden del middleware es crítico.** `UseAuthentication()` debe invocarse antes que `UseAuthorization()`. Ambos deben colocarse antes de `UseAntiforgery()` y antes de mapear los componentes Razor. Un orden incorrecto provocará que el estado de autenticación no esté disponible cuando se tomen las decisiones de autorización.

---

### Paso 3 — Endpoints HTTP de Inicio y Cierre de Sesión

**Archivo:** [`Security/AuthEndpoints.cs`](SoftProg-WebApp/Security/AuthEndpoints.cs)

Aquí debe comprenderse una restricción fundamental de Blazor Interactive Server: una vez que el **circuito SignalR** está establecido, los encabezados de respuesta HTTP ya han sido enviados al navegador. En consecuencia, **no es posible establecer ni limpiar cookies desde un componente Razor** durante un circuito activo. Para superar esta limitación arquitectónica, las operaciones de autenticación se gestionan a través de endpoints HTTP estándar utilizando el modelo de **Minimal API** de ASP.NET Core.

El endpoint de inicio de sesión sigue un proceso de cuatro pasos:

```csharp
endpoints.MapPost("/auth/login", async (HttpContext context) =>
{
    // Paso 1: Leer las credenciales y el indicador "recordarme" del cuerpo del formulario
    var username = form["usuario"].ToString();
    var password = form["contrasena"].ToString();
    var rememberMe = form["rememberMe"].ToString() == "on"; // el checkbox envía "on" cuando está marcado

    // Paso 2: Validar las credenciales y asignar un rol
    // (admin/admin → rol Admin | employee/employee → rol Employee)

    // Paso 3: Construir un ClaimsPrincipal que represente al usuario autenticado
    var claims = new List<Claim>
    {
        new(ClaimTypes.Name, username),
        new(ClaimTypes.Role, role),
        new("DisplayName", displayName),
        new("EmployeeId", "56478")
    };

    // Paso 4: Emitir la cookie de autenticación
    // IsPersistent controla si el navegador escribe la cookie en disco (true)
    // o la mantiene solo en memoria (false). ExpiresUtc controla la validez del ticket del lado del servidor.
    // Cuando rememberMe=false, ExpiresUtc se establece en 30 minutos — véase la nota a continuación.
    await context.SignInAsync(
        CookieAuthenticationDefaults.AuthenticationScheme,
        principal,
        new AuthenticationProperties
        {
            IsPersistent = rememberMe,
            ExpiresUtc = rememberMe
                ? DateTimeOffset.UtcNow.AddHours(8)
                : DateTimeOffset.UtcNow.AddMinutes(30)
        });

    return Results.LocalRedirect("/home");
});

endpoints.MapGet("/auth/logout", async (HttpContext context) =>
{
    await context.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
    return Results.LocalRedirect("/");
});
```

> **`IsPersistent` vs. `ExpiresUtc` — una distinción importante**
>
> Estas dos propiedades controlan diferentes capas de la vida útil de la cookie:
>
> | Propiedad | Qué controla | Dónde tiene efecto |
> |:---|:---|:---|
> | `IsPersistent` | Si el atributo `Expires` aparece en el encabezado `Set-Cookie` | **Navegador** — determina si la cookie se escribe en disco o se conserva en memoria |
> | `ExpiresUtc` | La marca de tiempo de expiración codificada dentro del ticket cifrado | **Servidor** — ASP.NET Core rechaza tickets pasado este tiempo, independientemente del navegador |
>
> **Importante**: cuando `IsPersistent = false`, ASP.NET Core **nunca** emite un atributo `Expires` en el encabezado `Set-Cookie`, incluso si `ExpiresUtc` está establecido. La cookie es siempre una cookie de sesión desde la perspectiva del navegador. `ExpiresUtc` únicamente limita la aceptación del ticket en el lado del servidor.
>
> **Advertencia sobre la restauración de sesión del navegador:** Los navegadores modernos (Chrome, Edge, Firefox) incluyen una función de restauración de sesión que guarda las cookies de sesión en disco y las recarga en el siguiente inicio. Esto significa que `IsPersistent = false` por sí solo **no** garantiza que el usuario cierre sesión al cerrar el navegador. Establecer `ExpiresUtc` con una duración corta (p. ej., 30 minutos) garantiza que el servidor rechace el ticket de cookie restaurado, aplicando efectivamente el tiempo de expiración incluso cuando el navegador restaura la sesión.

El formulario de inicio de sesión en `Login.razor` utiliza marcado HTML estándar — no un `EditForm` de Blazor — para garantizar que la solicitud se envíe como un POST HTTP completo:

```html
<form method="post" action="/auth/login">
    <AntiforgeryToken />
    <input name="usuario" ... />
    <input name="contrasena" type="password" ... />
    <input type="checkbox" name="rememberMe" />  <!-- envía "on" cuando está marcado, omitido cuando no lo está -->
    <button type="submit">Iniciar Sesión</button>
</form>
```

> **¿Por qué se requiere `<AntiforgeryToken />`?**  
> El middleware `UseAntiforgery()` de Blazor valida un token anti-falsificación oculto en cada solicitud POST. Este mecanismo protege contra ataques de **Falsificación de Solicitudes entre Sitios (CSRF)**, donde un sitio malicioso podría de otro modo enviar un formulario en nombre de un usuario autenticado. Omitir este token produce una respuesta `400 Bad Request`.

---

### Paso 4 — Declaración de Requisitos de Acceso a Nivel de Página

El atributo `[Authorize]` se aplica a los componentes de página Razor para declarar sus requisitos de acceso. El enrutador central evalúa este atributo antes de renderizar cualquier página.

| Página | Directiva | Nivel de Acceso |
|:---|:---|:---|
| `Login.razor` | `@attribute [AllowAnonymous]` | Acceso público — no se requiere autenticación |
| `Home.razor` | `@attribute [Authorize]` | Requiere una sesión autenticada activa |
| `CreateOrder.razor` | `@attribute [Authorize]` | Requiere una sesión autenticada activa |
| `ManageOrders.razor` | `@attribute [Authorize(Roles = "Admin")]` | Restringido a usuarios con el rol Administrador |

> **Para añadir una nueva página protegida**, coloca el atributo apropiado en la parte superior del archivo `.razor`. El enrutador y el middleware gestionan la aplicación automáticamente — no se requiere configuración adicional.

```razor
@page "/nueva-pagina"
@attribute [Authorize(Roles = "Admin")]   <!-- o simplemente [Authorize] -->
```

---

### Paso 5 — El Enrutador de Autorización Central

**Archivo:** [`Components/Routes.razor`](SoftProg-WebApp/Components/Routes.razor)

El enrutador distingue entre dos escenarios distintos de acceso no autorizado y responde a cada uno de manera apropiada:

```razor
<AuthorizeRouteView RouteData="routeData" DefaultLayout="typeof(Layout.MainLayout)">
    <NotAuthorized>
        @if (context.User.Identity?.IsAuthenticated == true)
        {
            <AccessDenied />       <!-- El usuario está autenticado pero no posee el rol requerido -->
        }
        else
        {
            <RedirectToLogin />    <!-- El usuario no está autenticado → redirigir al inicio de sesión -->
        }
    </NotAuthorized>
</AuthorizeRouteView>
```

**Notas conceptuales:**

- `context` es un objeto `AuthenticationState` proporcionado por el servicio `CascadingAuthenticationState` registrado en el Paso 1.
- `context.User.Identity?.IsAuthenticated` se evalúa como `true` cuando hay una cookie de autenticación válida presente, independientemente de si el usuario posee el rol correcto.
- Esta lógica se define **una sola vez** y se aplica **globalmente** a todas las páginas protegidas, eliminando la necesidad de verificaciones de autorización repetitivas a nivel de componente.

---

### Paso 6 — Componentes Razor de Seguridad

#### `RedirectToLogin.razor`
**Ubicación:** `Components/Security/RedirectToLogin.razor`  
**Invocado por:** `Routes.razor` → `<NotAuthorized>` cuando `context.User.Identity?.IsAuthenticated` es `false`.

```razor
@inject NavigationManager NavigationManager

@code {
    protected override void OnInitialized()
    {
        // forceLoad: true desencadena una solicitud HTTP completa en lugar de una navegación
        // del lado del cliente de Blazor. Esto permite que el middleware de autenticación de cookies
        // tome el control y aplique correctamente la lógica de redirección del lado del servidor.
        NavigationManager.NavigateTo("/", forceLoad: true);
    }
}
```

#### `AccessDenied.razor` (Componente de UI)
**Ubicación:** `Components/Security/AccessDenied.razor`  
**Invocado por:** `Routes.razor` → `<NotAuthorized>` cuando el usuario está autenticado pero no posee el rol requerido.  
Este componente contiene exclusivamente el marcado de la tarjeta "Acceso Denegado". Actúa como la **única fuente de verdad** para esta interfaz, garantizando consistencia visual independientemente de cómo sea invocado.

#### `AccessDenied.razor` (Página Enrutable)
**Ubicación:** `Components/Pages/AccessDenied.razor`  
**Invocado por:** El middleware de ASP.NET Core al redirigir directamente a `/access-denied` mediante `AccessDeniedPath`.

```razor
@page "/access-denied"

<PageTitle>Acceso Denegado</PageTitle>

<AccessDenied />   <!-- Consume el componente de UI de Components/Security/ -->
```

> **¿Por qué son necesarios ambos archivos?**  
> Dos flujos independientes deben mostrar la misma interfaz de usuario:
> 1. **Navegación interna de Blazor** — `Routes.razor` renderiza `<AccessDenied />` directamente dentro del circuito activo.
> 2. **Acceso directo por URL** — El middleware HTTP redirige a `/access-denied`, que es una página enrutable que envuelve el mismo componente.  
>
> Al tener ambos archivos consumir el mismo componente subyacente, la aplicación garantiza **una experiencia de usuario consistente** evitando la duplicación de código.

---

### Paso 7 — Renderizado Condicional de Elementos de la Interfaz por Rol

Además de proteger páginas completas, los elementos individuales de la interfaz — como los enlaces de navegación — pueden renderizarse condicionalmente en función del rol del usuario mediante el componente `<AuthorizeView>` en `MainLayout.razor`:

```razor
<AuthorizeView Roles="Admin">
    <li><a href="/manage-orders">Órdenes de Venta</a></li>
</AuthorizeView>
```

**Distinciones importantes:**

- El elemento es **excluido completamente del DOM renderizado** para los usuarios que no poseen el rol especificado. No se trata de un control de visibilidad mediante CSS; el HTML nunca se envía al navegador.
- `<AuthorizeView>` puede utilizarse en cualquier componente Razor, no exclusivamente en archivos de diseño.
- Este mecanismo proporciona **control de acceso a nivel de interfaz de usuario** y es complementario a la protección a nivel de ruta mediante `[Authorize]`, pero no la sustituye.

---

## ➕ Cómo Añadir una Nueva Página Protegida

El siguiente procedimiento debe seguirse siempre que se incorpore una nueva página restringida a la aplicación:

1. Crear un nuevo archivo `.razor` bajo `Components/Pages/`.
2. Declarar el nivel de acceso requerido en la parte superior del archivo:
   ```razor
   @page "/mi-nueva-pagina"
   @attribute [Authorize(Roles = "Admin")]   <!-- o @attribute [Authorize] para cualquier usuario autenticado -->
   ```
3. No se requiere configuración adicional. El enrutador (`Routes.razor`) y el middleware (`AuthExtensions.cs`) aplican el control de acceso de forma automática.

> Para introducir un **nuevo rol**, defínelo en `AuthEndpoints.cs` al construir las claims del usuario, y luego referencialo en `[Authorize(Roles = "NuevoRol")]` y `<AuthorizeView Roles="NuevoRol">` donde corresponda.

---

## ⚙️ Resumen de Responsabilidades por Archivo

| Archivo | Responsabilidad |
|:---|:---|
| `Security/AuthExtensions.cs` | Registro de autenticación por cookie, rutas de inicio de sesión/acceso denegado, expiración de sesión |
| `Security/AuthEndpoints.cs` | Lógica de validación de credenciales, emisión y destrucción de cookies |
| `Program.cs` | Orquestación del pipeline — delega completamente a los dos archivos anteriores |
| `Components/Routes.razor` | Distingue entre no autenticado y no autorizado; aplica el control globalmente |
| `Components/Security/AccessDenied.razor` | Componente de UI reutilizable "Acceso Denegado" (única fuente de verdad) |
| `Components/Security/RedirectToLogin.razor` | Redirección de página completa para usuarios no autenticados |
| `Components/Pages/AccessDenied.razor` | Página enrutable en `/access-denied` consumida por el middleware HTTP |
| `Components/_Imports.razor` | Expone el espacio de nombres `SoftProg_WebApp.Components.Security` a nivel de proyecto |
