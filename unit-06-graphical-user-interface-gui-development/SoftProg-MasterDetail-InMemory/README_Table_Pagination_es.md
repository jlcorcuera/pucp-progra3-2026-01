# Paginación de Tablas — Referencia Técnica

Este documento proporciona una descripción técnica exhaustiva del sistema de tabla paginada genérica implementado en **SoftProg WebApp**, una aplicación Blazor Interactive Server construida sobre .NET 10. Está dirigido a estudiantes universitarios matriculados en cursos de ingeniería de software o desarrollo web, y presupone familiaridad con C#, los ciclos de vida de los componentes Blazor y los conceptos básicos de manipulación de datos (`Skip`/`Take`).

---

## 🗂️ Estructura de Archivos de Paginación

Todos los componentes y modelos asociados con la funcionalidad de paginación genérica están organizados siguiendo el principio de **separación de responsabilidades**:

```
SoftProg-WebApp/
│
└── Components/
    ├── Shared/
    │   ├── PaginatedTable.razor            ← Componente contenedor paginado genérico
    │   └── TablePage.cs                    ← Modelos de contrato de datos (TablePage<T> & PageQuery)
    │
    └── Pages/
        └── Clientes/
            └── AdministrarClientes.razor   ← Página consumidora que particiona datos del lado del cliente
```

> [!NOTE]
> Separar la interfaz de presentación (`PaginatedTable.razor`) de la estructura contenedora de datos (`TablePage.cs`) permite que el paginador permanezca completamente genérico. Cualquier colección en memoria o proveniente de base de datos puede adaptarse y renderizarse fácilmente utilizando el mismo componente de tabla.

---

## 📦 Modelos Clave de Paginación

**Archivo:** [`Components/Shared/TablePage.cs`](SoftProg-WebApp/Components/Shared/TablePage.cs)

Dos contratos de datos coordinan la paginación entre la página padre (que actúa como controlador/proveedor de datos) y el componente de tabla hijo:

1. **`TablePage<T>`**: Representa el subconjunto de registros correspondiente a la página activa actual, junto con metadatos que indican el tamaño total del conjunto de datos filtrado.
2. **`PageQuery`**: Un registro de C# enviado desde el componente al padre, que representa la solicitud del estado de paginación (número de página y tamaño de página).

```csharp
public class TablePage<T>
{
    public List<T> Items { get; set; } = new();
    public int TotalRecords { get; set; }
}

public record PageQuery(int Page, int PageSize);
```

---

## 🛠️ Arquitectura del Sistema — Flujo de Datos y Ciclo de Eventos

El diagrama a continuación detalla el ciclo de flujo de datos unidireccional que impulsa el sistema de tabla paginada:

```text
[ Inicialización de la Interfaz del Componente ]
         │
         ▼  El primer renderizado activa OnParametersSet()
[ PaginatedTable.razor ]  ──► Renderiza navegación superior/inferior y filas genéricas
         │
         ▼  El usuario hace clic en "Siguiente" o cambia el tamaño de página
[ Actualización de Estado Local ]  ──► currentPage se incrementa (o se reinicia)
         │
         ▼  FireChanged() activa OnChanged.InvokeAsync(PageQuery)
[ Página Padre (AdministrarClientes.razor) ]  ──► Recibe OnPageChanged(PageQuery)
         │
         ▼  Particiona los datos fuente filtrados: query.Skip(X).Take(Y)
[ Propagación de Estado ]  ──► Actualiza paginaClientes (TablePage<Cliente>)
         │
         ▼  Activa la Actualización de Parámetros de Blazor
[ PaginatedTable.razor ]  ──► Recibe el Page actualizado; realiza verificación de igualdad por referencia
         │
         └── ✅ Referencia cambiada internamente  ──► Mantiene currentPage; renderiza nuevos elementos
         │
         └── ✅ Referencia cambiada externamente  ──► Reinicia currentPage = 1; renderiza elementos
```

Este modelo unidireccional garantiza que la **fuente de verdad** para el particionado de datos permanezca en el componente padre, mientras que el componente hijo genérico mantiene la retroalimentación de navegación de manera localizada.

---

## 📋 Recorrido de Implementación

### Paso 1 — Definición del Componente Genérico

**Archivo:** [`Components/Shared/PaginatedTable.razor`](SoftProg-WebApp/Components/Shared/PaginatedTable.razor)

El componente utiliza **Genéricos** (`@typeparam TItem`) para procesar colecciones de cualquier tipo. Emplea **Fragmentos de Renderizado** para aceptar estructuras de tabla sin procesar (encabezados y filas) desde el componente invocador:

```razor
@typeparam TItem
@using SoftProg_WebApp.Models

<div>
    <!-- Controles de la Barra Superior -->
    <div class="d-flex justify-content-between align-items-center mb-2">
        @RenderPageSizeSelector
        <span class="text-muted small">@SummaryRecords</span>
        @RenderPagination
    </div>

    <!-- Contenedor de la Tabla -->
    <div class="table-responsive shadow-sm rounded">
        <table class="table table-hover align-middle mb-0 bg-white">
            <thead class="table-dark">
                @TableHeader
            </thead>
            <tbody>
                @if (Page.Items.Any())
                {
                    @foreach (var item in Page.Items)
                    {
                        @RowTemplate(item)
                    }
                }
                else
                {
                    <tr>
                        <td colspan="@ColSpan" class="text-center py-5 text-muted">
                            <i class="bi bi-inbox me-2"></i>@EmptyMessage
                        </td>
                    </tr>
                }
            </tbody>
        </table>
    </div>

    <!-- Controles de la Barra Inferior -->
    <div class="d-flex justify-content-between align-items-center mt-3">
        @RenderPageSizeSelector
        <span class="text-muted small">@SummaryWithPage</span>
        @RenderPagination
    </div>
</div>
```

---

### Paso 2 — Parámetros y Configuración del Componente

```csharp
@code {
    [Parameter] public TablePage<TItem> Page { get; set; } = new();
    [Parameter] public RenderFragment? TableHeader { get; set; }
    [Parameter] public RenderFragment<TItem> RowTemplate { get; set; } = default!;
    [Parameter] public EventCallback<PageQuery> OnChanged { get; set; }
    [Parameter] public int ColSpan { get; set; } = 5;
    [Parameter] public string EmptyMessage { get; set; } = "No se encontraron registros.";
}
```

> **¿Por qué se declara `RowTemplate` como `RenderFragment<TItem>`?**  
> A diferencia de un `RenderFragment` estándar, un `RenderFragment<T>` recibe una variable contextual durante el renderizado. Esto permite al componente invocador generar columnas HTML específicas para cada elemento del bucle (p. ej., referenciando `@cliente.Name` mediante `Context="cliente"`).

---

### Paso 3 — Estrategia de Reinicio Autocontenida

Un error frecuente en las implementaciones de paginación personalizadas en Blazor ocurre cuando se modifican los filtros de búsqueda: el indicador de página activa permanece en la página 3 o 4, pero la página padre particiona la nueva colección filtrada desde la página 1.

Para resolver esta situación sin obligar al componente padre a rastrear y vincular explícitamente parámetros de estado bidireccionales, `PaginatedTable` emplea una **estrategia de seguimiento por igualdad de referencia** durante el ciclo de vida del componente Blazor:

```csharp
private TablePage<TItem> _previousPage = null!;
private bool _isNavigatingInternally;

protected override void OnParametersSet()
{
    // Verifica si el componente padre asignó una nueva instancia de TablePage
    if (!ReferenceEquals(Page, _previousPage))
    {
        _previousPage = Page;
        
        // Si la actualización provino de los clics de navegación propios de la tabla, conservar el estado.
        // En caso contrario, fue causada por búsqueda/filtros externos; forzar el retorno a la página 1.
        if (_isNavigatingInternally)
        {
            _isNavigatingInternally = false;
        }
        else
        {
            currentPage = 1;
        }
        UpdateVisiblePages();
    }
}
```

Cada vez que el componente inicia una navegación (p. ej., al hacer clic en Página Siguiente), establece el indicador de navegación interna antes de disparar el evento hacia el padre:

```csharp
private Task FireChanged()
{
    _isNavigatingInternally = true;
    return OnChanged.InvokeAsync(new PageQuery(currentPage, pageSize));
}
```

---

### Paso 4 — Decisiones de Optimización del Rendimiento

La tabla ha sido optimizada para limitar las asignaciones de memoria y los retardos de renderizado de la interfaz de usuario de dos maneras:

1. **Deduplicación de Código mediante Sub-Renderizadores**: Las filas de control superior e inferior reutilizan propiedades `RenderFragment` (`RenderPageSizeSelector` y `RenderPagination`) declaradas en `@code`. Esto evita la duplicación de la estructura HTML, manteniendo el diseño del componente altamente legible.
2. **Caché de la Lista de Rango de Páginas**: En lugar de generar una nueva colección `RangeIterator` dentro de un getter de propiedad en cada ciclo de actualización de la interfaz (lo cual ocurre dos veces por renderizado debido a las barras de paginación dobles), los números de página visibles se precalculan y almacenan en caché en una lista privada `visiblePages` dentro de `UpdateVisiblePages()`, solo cuando ocurren actualizaciones de datos.

---

### Paso 5 — Consumo del Componente Genérico

**Archivo:** [`Components/Pages/Clientes/AdministrarClientes.razor`](SoftProg-WebApp/Components/Pages/Clientes/AdministrarClientes.razor)

La página consumidora pasa el bloque de datos particionado `paginaClientes` al componente, renderiza las columnas personalizadas y actualiza su partición local dentro del método `OnPageChanged`:

```razor
<PaginatedTable TItem="Cliente" Page="paginaClientes" OnChanged="OnPageChanged">
    <TableHeader>
        <tr>
            <th class="ps-3">ID</th>
            <th>DNI</th>
            <th>Nombre</th>
            <th>Apellido Paterno</th>
            <th class="text-center pe-3">Acciones</th>
        </tr>
    </TableHeader>
    <RowTemplate Context="cliente">
        <tr>
            <td class="ps-3"><span class="badge bg-secondary">#@cliente.Id</span></td>
            <td><span class="badge bg-light text-secondary border">@cliente.DNI</span></td>
            <td class="fw-medium">@cliente.Nombre</td>
            <td>@cliente.ApellidoPaterno</td>
            <td class="text-center pe-3">
                <button class="btn btn-sm btn-outline-primary" @onclick="() => VerDetalles(cliente.Id)">
                    <i class="bi bi-eye me-1"></i>Ver
                </button>
            </td>
        </tr>
    </RowTemplate>
</PaginatedTable>
```

En el código subyacente:

```csharp
private TablePage<Cliente> paginaClientes = new();
private PageQuery queryActual = new(1, 25);

private void OnPageChanged(PageQuery query)
{
    queryActual = query;
    ActualizarPagina();
}

private void ActualizarPagina()
{
    var total = clientesFiltrados.Count;
    var skip = (queryActual.Page - 1) * queryActual.PageSize;
    var items = clientesFiltrados.Skip(skip).Take(queryActual.PageSize).ToList();

    paginaClientes = new TablePage<Cliente>
    {
        Items = items,
        TotalRecords = total
    };
}
```

---

## ➕ Cómo Paginar una Nueva Entidad

Para reutilizar este esquema de paginación con otra entidad de base de datos o colección estática (por ejemplo, `Product`):

1. Registrar el espacio de nombres `SoftProg_WebApp.Components.Shared` en el archivo destino (nótese que por defecto se importa a nivel de proyecto en `_Imports.razor`).
2. Declarar los estados de paginación locales en el componente de página:
   ```csharp
   private TablePage<Product> productPage = new();
   private PageQuery query = new(1, 25);
   ```
3. Implementar `ActualizarPagina()` utilizando LINQ:
   ```csharp
   var skip = (query.Page - 1) * query.PageSize;
   productPage = new TablePage<Product>
   {
       Items = allProducts.Skip(skip).Take(query.PageSize).ToList(),
       TotalRecords = allProducts.Count
   };
   ```
4. Colocar la etiqueta `<PaginatedTable TItem="Product">`, pasando `Page="productPage"`, gestionando `OnChanged="OnPageChanged"`, y declarando los encabezados y filas personalizados en `TableHeader` y `RowTemplate`.

---

## ⚙️ Resumen de Responsabilidades por Archivo

| Archivo | Responsabilidad |
|:---|:---|
| `Components/Shared/TablePage.cs` | Define las estructuras de transmisión de datos entre las tablas genéricas de la interfaz y las páginas padre. |
| `Components/Shared/PaginatedTable.razor` | Componente genérico que renderiza el diseño de la tabla, los botones de paginación, los selectores desplegables de tamaño de página y los contadores de registros. |
| `Components/Pages/Clientes/AdministrarClientes.razor` | Gestiona el filtrado y el particionado local de datos en memoria (Skip/Take) en respuesta a los eventos del componente. |
