# SoftProg WebApp — Visión General de la Arquitectura

Este repositorio contiene **SoftProg WebApp**, una aplicación web interactiva construida sobre Blazor Server (.NET 10). Está diseñada como referencia educativa para estudiantes universitarios de ingeniería de software que estudian patrones de diseño arquitectónico, seguridad web y reutilización de componentes.

A continuación se presenta una descripción general de los dos subsistemas principales implementados en el proyecto. Para explicaciones detalladas del código y guías paso a paso, consulta las referencias técnicas enlazadas.

---

## 🔑 Credenciales de Prueba y Control de Acceso

Para pruebas de autorización y verificación de roles, la aplicación incluye dos cuentas de usuario preconfiguradas con roles de dominio distintos:

| Usuario | Contraseña | Rol | Nombre de pantalla | Permisos clave |
| :--- | :--- | :--- | :--- | :--- |
| **admin** | `admin` | **Administrador** | Administrador Principal | Acceso completo a todas las secciones y gestión de órdenes de venta. |
| **employee** | `employee` | **Empleado** | Empleado de Ventas | Creación de órdenes de venta (bloqueado para gestionarlas). |

> [!IMPORTANT]
> Estas credenciales están codificadas de forma fija estrictamente con fines pedagógicos. En entornos de producción, las credenciales deben almacenarse de forma segura utilizando hash criptográfico de contraseñas (p. ej., ASP.NET Core Identity con BCrypt).

---

## 🛡️ Subsistema de Seguridad y Roles

La aplicación utiliza un esquema de autenticación basado en cookies HTTP, integrando la resolución de identidad directamente con el ciclo de vida del circuito de Blazor Server, mientras delega las operaciones de cookies a endpoints independientes de Minimal API:

- **Autenticación Basada en Cookies**: Resuelve la identidad a nivel del pipeline HTTP para garantizar compatibilidad fluida con el pre-renderizado del lado del servidor y evitar problemas de concurrencia.
- **Integración con Minimal API**: Orquesta el establecimiento de sesión (`POST /auth/login`) y su terminación (`GET /auth/logout`) antes de establecer la conexión WebSocket de Blazor (SignalR).
- **Políticas de Control de Acceso**: Restringe componentes de página completos mediante la directiva `@attribute [Authorize]` y actualiza condicionalmente las estructuras de la interfaz de usuario a través de `<AuthorizeView>`.

📖 **Para una descripción detallada del flujo de autenticación, registro de middleware y recorridos de código, consulta:**  
👉 **[Configuración de Seguridad — Referencia Técnica](README_Security_Configuration.md)**

---

## 📊 Subsistema de Paginación de Tabla Genérica

Para manejar grandes conjuntos de datos de manera eficiente, la aplicación incorpora un componente de presentación genérico:

- **Flujo de Datos Unidireccional**: El componente genérico `<PaginatedTable>` delega el particionado de colecciones, el filtrado y la lógica de consulta (`Skip`/`Take`) al contenedor padre mediante callbacks de eventos.
- **Resolución de Tipo Genérico (`@typeparam TItem`)**: Procesa dinámicamente colecciones de tipos de entidad arbitrarios (p. ej., Clientes, Productos, Pedidos) a través de fragmentos de plantilla personalizados (`RenderFragment`).
- **Sincronización de Estado (Igualdad por Referencia)**: Evalúa la igualdad de referencia del modelo para restablecer automáticamente el índice de página activa a 1 cuando los criterios de búsqueda externos se borran o actualizan.

📖 **Para patrones de implementación, detalles de rendimiento e instrucciones paso a paso de integración genérica, consulta:**  
👉 **[Paginación de Tablas — Referencia Técnica](README_Table_Pagination.md)**
