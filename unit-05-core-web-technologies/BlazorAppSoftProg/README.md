# BlazorAppSoftProg

Aplicación web desarrollada con **ASP.NET Core Blazor** que implementa un módulo de **gestión de empleados y áreas**. El proyecto está diseñado con una arquitectura en capas que separa las responsabilidades del dominio, acceso a datos, lógica de negocio y presentación.


---

## 📁 Estructura del Proyecto

La solución (`BlazorAppSoftProg.slnx`) está organizada en **cinco proyectos** independientes, cada uno con una responsabilidad específica:

```
BlazorAppSoftProg/
├── BlazorAppSoftProg/      ← Proyecto principal (Interfaz Web - Blazor)
├── SoftProgDomain/         ← Capa de Dominio (modelos/entidades)
├── SoftProgDBManager/      ← Capa de Gestión de Conexión a BD
├── SoftProgDAO/            ← Capa de Acceso a Datos (DAO)
└── SoftProgBL/             ← Capa de Lógica de Negocio (BL)
```

---

## 🗂 Capas de la Arquitectura

### 1. `SoftProgDomain` — Capa de Dominio

Esta capa define las **entidades del negocio**: las clases que representan los objetos del mundo real con los que trabaja la aplicación. No depende de ninguna otra capa.

#### `Persona` (clase base)
Representa a cualquier persona registrada en el sistema.

| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `IdPersona` | `int` | Identificador único en la base de datos |
| `DNI` | `string?` | Número de documento de identidad |
| `Nombre` | `string?` | Nombre de pila |
| `ApellidoPaterno` | `string?` | Apellido paterno |
| `Sexo` | `char?` | `'M'` para masculino, `'F'` para femenino |
| `FechaNacimiento` | `DateTime?` | Fecha de nacimiento |

#### `Empleado` (hereda de `Persona`)
Extiende `Persona` añadiendo la información laboral del empleado.

| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `Area` | `Area?` | Área a la que pertenece el empleado |
| `Cargo` | `string?` | Puesto o cargo que ocupa |
| `Sueldo` | `double?` | Remuneración mensual |
| `Activo` | `bool?` | Indica si el empleado está activo (`true`) o fue dado de baja (`false`) |

> **Concepto clave — Herencia:** `Empleado : Persona` significa que `Empleado` hereda todos los atributos de `Persona`. Esto evita duplicar código y modela correctamente la relación "un empleado *es* una persona".

#### `Area`
Representa un área o departamento de la empresa.

| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `IdArea` | `int` | Identificador único |
| `Name` | `string?` | Nombre del área (ej. "Recursos Humanos") |
| `Activa` | `bool` | Indica si el área está activa |

#### `EmpleadoDTO` (Data Transfer Object)
Versión simplificada de `Empleado` para mostrar datos en listados. En lugar de incluir el objeto `Area` completo, solo guarda el **nombre del área** como texto. Esto es más eficiente cuando solo se necesita mostrar información resumida.

```
EmpleadoDTO = DNI + Nombre + Apellido + FechaNacimiento + NombreDeArea + Cargo
              (sin datos de sueldo, sexo ni estado activo)
```

---

### 2. `SoftProgDBManager` — Gestión de Conexión a Base de Datos

Esta capa contiene una única clase: `DBManager`. Su responsabilidad es **centralizar y controlar el acceso a la base de datos**, independientemente del motor que se use (MySQL o SQL Server).

#### Patrón Singleton

`DBManager` implementa el patrón **Singleton**, lo que garantiza que solo exista **una única instancia** de la conexión configurada en toda la aplicación.

```csharp
// Inicialización única al arrancar la aplicación (en Program.cs)
DBManager.Initialize("MySQL", cadenaMySQL, cadenaMSSQLServer);

// En cualquier DAO, se obtiene la conexión así:
IDbConnection conn = DBManager.Instance.GetConnection();
```

#### Soporte Multi-Motor

El `DBManager` puede devolver conexiones a dos motores de base de datos según la configuración del archivo `appsettings.json`:

| Valor en config | Motor de BD | Clase de conexión |
|-----------------|-------------|-------------------|
| `"MySQL"` | MySQL / Amazon RDS | `MySqlConnection` |
| Cualquier otro | SQL Server | `SqlConnection` |

> **¿Por qué esto es útil?** Permite cambiar el motor de base de datos simplemente editando `appsettings.json`, sin tocar el código de los DAOs.

---

### 3. `SoftProgDAO` — Capa de Acceso a Datos

Esta capa es responsable de **ejecutar consultas SQL** contra la base de datos. Cada clase DAO sabe exactamente cómo insertar, actualizar, eliminar y consultar una entidad específica.

#### Interfaces

Cada DAO está definido primero como una **interfaz** (contrato) y luego como una **implementación concreta**:

- `IAreaDAO` → `AreaDAO`
- `IEmpleadoDAO` → `EmpleadoDAO`

Usar interfaces permite que la capa de negocio no dependa de la implementación específica, facilitando las pruebas y el mantenimiento.

#### `AreaDAO` — Operaciones sobre la tabla `area`

| Método | SQL ejecutado | Descripción |
|--------|--------------|-------------|
| `insertar(Area)` | `INSERT INTO area ...` | Crea un nuevo área |
| `modificar(Area)` | `UPDATE area SET ...` | Actualiza nombre y estado del área |
| `eliminar(int)` | `UPDATE area SET activa = 0 ...` | **Baja lógica**: no borra el registro, solo lo desactiva |
| `listarTodas()` | `SELECT ... WHERE activa = 1` | Devuelve todas las áreas activas |
| `obtenerPorId(int)` | `SELECT ... WHERE id_area = @id` | Devuelve un área por su ID |
| `listarPorNombre(string)` | `SELECT ... WHERE nombre LIKE @nombre` | Búsqueda por texto parcial |

#### `EmpleadoDAO` — Operaciones sobre las tablas `persona` y `empleado`

Como `Empleado` hereda de `Persona`, en la base de datos existen **dos tablas relacionadas**: `persona` y `empleado`. Las operaciones sobre empleados deben coordinar ambas tablas.

| Método | Descripción |
|--------|-------------|
| `insertar(Empleado)` | Primero inserta en `persona`, obtiene el ID generado, luego inserta en `empleado` con ese ID |
| `modificar(Empleado)` | Actualiza tanto la tabla `persona` como `empleado` en la misma conexión |
| `eliminar(int)` | Baja lógica: `UPDATE empleado SET activo = 0` — el registro se conserva en BD |
| `obtenerPorId(int)` | `JOIN` entre `persona`, `empleado` y `area` para obtener el objeto completo |
| `listar(nombre, apellido, idArea)` | Consulta con filtros dinámicos opcionales usando `LIKE` y comparación por ID de área |

> **Concepto clave — Baja lógica:** En lugar de borrar físicamente un registro de la BD, se marca como inactivo (`activo = 0`). Esto permite recuperar el historial si fuera necesario y es una práctica habitual en sistemas empresariales.

---

### 4. `SoftProgBL` — Capa de Lógica de Negocio

Esta capa actúa como **intermediaria** entre la interfaz web y el acceso a datos. Recibe las solicitudes desde los componentes Blazor, aplica las reglas de negocio necesarias y delega las operaciones de persistencia al DAO correspondiente.

En el estado actual del proyecto, `EmpleadoBL` y `AreaBL` delegan directamente al DAO, lo cual es correcto para la etapa inicial. En un sistema más complejo, aquí se agregarían validaciones adicionales (ej. "no se puede eliminar un área que tiene empleados activos").

```csharp
// Ejemplo: EmpleadoBL delega al DAO
public int insertar(Empleado empleado)
{
    return _empleadoDAO.insertar(empleado);  // Puede añadir validaciones antes
}
```

Las interfaces `IAreaBL` e `IEmpleadoBL` son las que se registran en el contenedor de inyección de dependencias de Blazor.

---

### 5. `BlazorAppSoftProg` — Proyecto Web (Blazor Server)

Es la **capa de presentación**: todo lo que el usuario ve e interactúa en el navegador. Usa **Blazor Server** con modo de renderizado interactivo (`@rendermode InteractiveServer`), lo que significa que la lógica se ejecuta en el servidor y el navegador se actualiza en tiempo real mediante una conexión WebSocket.

#### Configuración (`Program.cs` y `appsettings.json`)

**`appsettings.json`** define las cadenas de conexión a la base de datos:

```json
"ConnectionStrings": {
    "Type": "MySQL",
    "MySqlConnection":      "Server=...;Database=softprog;...",
    "MSSQLServerConnection": "Server=...;Database=softprog;..."
}
```

**`Program.cs`** es el punto de entrada. Sus responsabilidades son:

1. **Leer la configuración** y inicializar el `DBManager` (Singleton).
2. **Registrar los servicios** de la capa de negocio (`IAreaBL`, `IEmpleadoBL`) como `Scoped` en el contenedor de inyección de dependencias.
3. **Configurar el pipeline HTTP** de ASP.NET Core (manejo de errores, HTTPS, antiforgery).
4. **Mapear los componentes Razor** para que Blazor sirva las páginas.

```csharp
// Registro de servicios BL para inyección de dependencias
builder.Services.AddScoped<IAreaBL, AreaBL>();
builder.Services.AddScoped<IEmpleadoBL, EmpleadoBL>();
```

---

## 📄 Páginas y Componentes

### Rutas disponibles

| URL | Componente | Descripción |
|-----|-----------|-------------|
| `/` | `Home.razor` | Página de inicio |
| `/empleado` | `EmpleadoListMemory.razor` | Gestión de empleados con datos en memoria |
| `/employee-management` | `EmpleadoListBD.razor` | Gestión de empleados con base de datos real |
| `/employee-data` | `EmployeeData.razor` | Formulario para crear un nuevo empleado |
| `/employee-data/{id}` | `EmployeeData.razor` | Formulario para editar un empleado existente |

---

### `EmpleadoListMemory.razor` — Listado con datos en memoria

**Ruta:** `/empleado`

Esta página es la **versión de prototipo**: usa datos hardcodeados en lugar de la base de datos real. Es ideal para entender la lógica de la interfaz sin necesidad de una conexión a BD.

**¿Qué hace?**
- Muestra una tabla con 5 empleados de prueba precargados en el método `OnInitializedAsync`.
- Permite **filtrar en tiempo real** por nombre, apellido y área — el filtrado ocurre del lado del cliente usando LINQ sobre la lista en memoria. El filtro reactivo usa `@bind:event="oninput"` para reaccionar a cada tecla presionada.
- Permite **crear** un nuevo empleado mediante un modal emergente.
- Permite **ver y editar** un empleado existente abriendo el mismo modal con sus datos cargados.
- Los cambios se reflejan directamente en la lista en memoria (no se persisten en BD).

**Conceptos Blazor demostrados:**
- `@bind` y `@bind:event="oninput"` para binding bidireccional
- `@foreach` para renderizar filas de la tabla dinámicamente
- `@if` para mostrar/ocultar el modal y los mensajes de estado
- Propiedad computada `FilteredEmpleados` con LINQ

---

### `EmpleadoListBD.razor` — Listado con base de datos real

**Ruta:** `/employee-management`

Esta página es la **versión de producción**: se conecta a la base de datos real a través de la capa de negocio. Utiliza inyección de dependencias para obtener los servicios `IEmpleadoBL` e `IAreaBL`.

**¿Qué hace?**
- Carga la lista de empleados activos desde la BD al iniciar (`OnInitializedAsync`).
- Carga las áreas disponibles para el combo desplegable de filtros.
- Permite **filtrar** por nombre, apellido y área — la búsqueda se ejecuta contra la BD con parámetros.
- Muestra un **spinner de carga** mientras espera la respuesta de la BD.
- Permite **crear** un empleado nuevo mediante un modal emergente (se inserta en BD al guardar).
- Permite **editar** un empleado seleccionado — carga el objeto completo desde BD, lo muestra en el modal, y guarda los cambios.
- Permite **eliminar** un empleado (baja lógica).
- Demuestra **tres formas de navegar** a la página de formulario:
  - Con `NavigationManager` (desde código C#)
  - Con un `<a href="...">` (navegación declarativa en HTML)

**Conceptos Blazor demostrados:**
- `@inject` para inyectar servicios de negocio
- `await Task.Run(...)` para ejecutar operaciones de BD sin bloquear la UI
- Modal implementado manualmente con CSS de Bootstrap (sin librerías externas)
- Navegación programática con `NavigationManager`

---

### `EmployeeData.razor` — Formulario de alta/edición (página separada)

**Rutas:** `/employee-data` (nuevo) y `/employee-data/{Id:int}` (editar)

Este componente muestra cómo implementar un formulario en una **página propia** en lugar de un modal. Recibe el `Id` del empleado como **parámetro de ruta**.

**¿Cómo decide si es creación o edición?**

```razor
@page "/employee-data"          @* Sin ID → nuevo empleado *@
@page "/employee-data/{Id:int}" @* Con ID → editar empleado *@

@code {
    [Parameter] public int Id { get; set; } = 0;

    protected override async Task OnInitializedAsync()
    {
        if (Id != 0)
        {
            // Cargar empleado existente desde BD
            currentEmpleado = await Task.Run(() => empleadoBL.obtenerPorId(Id));
        }
        // Si Id == 0, el formulario queda en blanco para un nuevo empleado
    }
}
```

**¿Qué hace al guardar?**
- Si `Id == 0`: llama a `empleadoBL.insertar(...)` y redirige al listado.
- Si `Id != 0`: llama a `empleadoBL.modificar(...)` y redirige al listado.

**Conceptos Blazor demostrados:**
- Parámetros de ruta con restricción de tipo (`:int`)
- Lógica condicional según si el componente es de creación o edición
- `NavigationManager.NavigateTo(...)` para redirigir tras guardar
- Spinner de carga mientras se inicializa el componente

---

## 🔄 Flujo de Datos

El siguiente diagrama muestra cómo fluye una operación típica (por ejemplo, "guardar un empleado") a través de las capas:

```
[Usuario hace clic en "Guardar"]
        │
        ▼
[Componente Blazor (.razor)]
  Llama a: await Task.Run(() => empleadoBL.insertar(emp))
        │
        ▼
[EmpleadoBL] ← Capa de Negocio
  Delega a: _empleadoDAO.insertar(emp)
        │
        ▼
[EmpleadoDAO] ← Capa de Acceso a Datos
  1. INSERT INTO persona (...)
  2. Obtiene ID generado
  3. INSERT INTO empleado (id = ID_obtenido, ...)
        │
        ▼
[DBManager] ← Gestión de Conexión
  Devuelve: MySqlConnection o SqlConnection
        │
        ▼
[Base de Datos] ← MySQL o SQL Server (Amazon RDS)
```

---

## ⚙️ Requisitos para Ejecutar el Proyecto

1. **Visual Studio 2022** o superior (se recomienda **Visual Studio 2026**) con la carga de trabajo **ASP.NET y desarrollo web** instalada
2. **.NET 10 SDK** instalado
3. Conexión a la base de datos configurada en `appsettings.json`
4. Las tablas `persona`, `empleado` y `area` deben existir en la base de datos

Para ejecutar el proyecto, abrir el archivo `BlazorAppSoftProg.slnx` con Visual Studio y presionar **F5** o el botón **▶ Iniciar**.

La aplicación estará disponible en `https://localhost:5001` (o el puerto configurado en `launchSettings.json`).
