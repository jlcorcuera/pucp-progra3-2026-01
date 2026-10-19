# Table Pagination — Technical Reference

This document provides a comprehensive technical description of the generic paginated table system implemented in **SoftProg WebApp**, a Blazor Interactive Server application built on .NET 10. It is intended for undergraduate students enrolled in software engineering or web development courses, and assumes familiarity with C#, Blazor component life cycles, and basic data manipulation concepts (Skip/Take).

---

## 🗂️ Pagination File Structure

All components and models associated with the generic pagination feature are organized following the **separation of concerns** principle:

```
SoftProg-WebApp/
│
└── Components/
    ├── Shared/
    │   ├── PaginatedTable.razor            ← Generic paginated container component
    │   └── TablePage.cs                    ← Data contract models (TablePage<T> & PageQuery)
    │
    └── Pages/
        └── Clientes/
            └── AdministrarClientes.razor   ← Consumer page slicing client-side data
```

> [!NOTE]
> Separating the presentation UI (`PaginatedTable.razor`) from the data container structure (`TablePage.cs`) allows the paginator to remain completely generic. Any database or in-memory collection can easily be adapted and rendered using the exact same table component.

---

## 📦 Key Pagination Models

**File:** [`Components/Shared/TablePage.cs`](SoftProg-WebApp/Components/Shared/TablePage.cs)

Two data contracts coordinate pagination between the parent page (acting as the controller/data provider) and the child table component:

1. **`TablePage<T>`**: Represents the subset of records corresponding to the current active page, along with metadata indicating the overall size of the filtered dataset.
2. **`PageQuery`**: A C# record sent from the component to the parent representing the pagination state request (page number and page size).

```csharp
public class TablePage<T>
{
    public List<T> Items { get; set; } = new();
    public int TotalRecords { get; set; }
}

public record PageQuery(int Page, int PageSize);
```

---

## 🛠️ System Architecture — Data Flow & Event Cycle

The diagram below details the unidirectional data flow cycle that powers the paginated table system:

```text
[ Component UI Initialization ]
         │
         ▼  First render triggers OnParametersSet()
[ PaginatedTable.razor ]  ──► Renders top/bottom navigation & generic rows
         │
         ▼  User clicks "Next" or changes page size
[ Local State Update ]  ──► currentPage incremented (or reset)
         │
         ▼  FireChanged() triggers OnChanged.InvokeAsync(PageQuery)
[ Parent Page (AdministrarClientes.razor) ]  ──► Receives OnPageChanged(PageQuery)
         │
         ▼  Slices filtered source data: query.Skip(X).Take(Y)
[ State Propagation ]  ──► Updates paginaClientes (TablePage<Cliente>)
         │
         ▼  Triggers Blazor Parameter Update
[ PaginatedTable.razor ]  ──► Receives updated Page; performs reference-equality check
         │
         └── ✅ Reference changed internally  ──► Keeps currentPage; renders new items
         │
         └── ✅ Reference changed externally  ──► Resets currentPage = 1; renders items
```

This unidirectional model ensures that the **source of truth** for data slicing remains with the parent component, while the generic child component maintains localized navigation feedback.

---

## 📋 Implementation Walkthrough

### Step 1 — The Generic Component Definition

**File:** [`Components/Shared/PaginatedTable.razor`](SoftProg-WebApp/Components/Shared/PaginatedTable.razor)

The component utilizes **Generics** (`@typeparam TItem`) to process collections of any type. It uses **Render Fragments** to accept raw table structures (headers and rows) from the caller:

```razor
@typeparam TItem
@using SoftProg_WebApp.Models

<div>
    <!-- Top Bar Controls -->
    <div class="d-flex justify-content-between align-items-center mb-2">
        @RenderPageSizeSelector
        <span class="text-muted small">@SummaryRecords</span>
        @RenderPagination
    </div>

    <!-- Table Container -->
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

    <!-- Bottom Bar Controls -->
    <div class="d-flex justify-content-between align-items-center mt-3">
        @RenderPageSizeSelector
        <span class="text-muted small">@SummaryWithPage</span>
        @RenderPagination
    </div>
</div>
```

---

### Step 2 — Parameters and Component Settings

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

> **Why is `RowTemplate` declared as `RenderFragment<TItem>`?**  
> Unlike a standard `RenderFragment`, a `RenderFragment<T>` receives a contextual variable during rendering. This allows the calling component to output specific HTML columns for each item in the loop (e.g. referencing `@cliente.Name` via `Context="cliente"`).

---

### Step 3 — Self-Contained Reset Strategy

One common bug in custom Blazor pagination occurs when search filters are modified: the active page indicator remains on page 3 or 4, but the parent page slices the new filtered collection from page 1.

To resolve this without forcing the parent to explicitly track and bind two-way state parameters, `PaginatedTable` uses a **Reference-Equality tracking strategy** during the Blazor component lifecycle:

```csharp
private TablePage<TItem> _previousPage = null!;
private bool _isNavigatingInternally;

protected override void OnParametersSet()
{
    // Check if the parent component assigned a new TablePage instance
    if (!ReferenceEquals(Page, _previousPage))
    {
        _previousPage = Page;
        
        // If the update came from the table's own pagination navigation clicks, keep state.
        // Otherwise, it was caused by external search/filters; force back to page 1.
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

Whenever the component initiates navigation (e.g., clicking Next Page), it sets the internal navigation flag before firing the event to the parent:

```csharp
private Task FireChanged()
{
    _isNavigatingInternally = true;
    return OnChanged.InvokeAsync(new PageQuery(currentPage, pageSize));
}
```

---

### Step 4 — Performance Optimization Decisions

The table has been optimized to limit memory allocations and UI render delays in two ways:

1. **Sub-Renderer Code Deduplication**: The top and bottom control rows reuse `RenderFragment` properties (`RenderPageSizeSelector` and `RenderPagination`) declared in `@code`. This prevents HTML structure duplication, keeping the component layout highly readable.
2. **Page Range List Caching**: Instead of generating a new `RangeIterator` collection inside a property getter on every single UI refresh tick (which occurs twice per render due to double pagination bars), the visible page numbers are precalculated and cached in a private `visiblePages` list inside `UpdateVisiblePages()` only when data updates occur.

---

### Step 5 — Consuming the Generic Component

**File:** [`Components/Pages/Clientes/AdministrarClientes.razor`](SoftProg-WebApp/Components/Pages/Clientes/AdministrarClientes.razor)

The consumer page passes the sliced data block `paginaClientes` to the component, renders the custom columns, and updates its local slice inside the `OnPageChanged` method:

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

In the backing code:

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

## ➕ How to Paginate a New Entity

To reuse this pagination layout for another database or static entity collection (for example, `Product`):

1. Register `SoftProg_WebApp.Components.Shared` namespace in the target file (note that this is imported project-wide by default in `_Imports.razor`).
2. Declare local pagination states on the page component:
   ```csharp
   private TablePage<Product> productPage = new();
   private PageQuery query = new(1, 25);
   ```
3. Implement `ActualizarPagina()` using LINQ:
   ```csharp
   var skip = (query.Page - 1) * query.PageSize;
   productPage = new TablePage<Product>
   {
       Items = allProducts.Skip(skip).Take(query.PageSize).ToList(),
       TotalRecords = allProducts.Count
   };
   ```
4. Place the `<PaginatedTable TItem="Product">` tag, passing `Page="productPage"`, handling `OnChanged="OnPageChanged"`, and declaring custom headers and rows in `TableHeader` and `RowTemplate`.

---

## ⚙️ File Responsibilities Summary

| File | Responsibility |
|:---|:---|
| `Components/Shared/TablePage.cs` | Defines data transmission structures between generic UI tables and the parent pages. |
| `Components/Shared/PaginatedTable.razor` | Generic component rendering table layout, pagination buttons, page size select dropdowns, and record counts. |
| `Components/Pages/Clientes/AdministrarClientes.razor` | Handles filtering and local in-memory data slicing (Skip/Take) in response to component events. |
