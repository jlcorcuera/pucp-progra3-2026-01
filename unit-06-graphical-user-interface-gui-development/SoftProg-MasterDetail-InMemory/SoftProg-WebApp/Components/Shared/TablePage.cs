namespace SoftProg_WebApp.Components.Shared;

/// <summary>
/// Carries one page of data and the total record count across all pages.
/// The caller (page component) is responsible for slicing the data and setting TotalRecords.
/// Consumed by PaginatedTable&lt;TItem&gt; to render the table and drive pagination controls.
/// </summary>
public class TablePage<T>
{
    /// <summary>The items for the current page (pre-sliced by the caller).</summary>
    public List<T> Items { get; set; } = new();

    /// <summary>
    /// Total number of records in the full (filtered) dataset — not just the current page.
    /// Used by PaginatedTable to calculate TotalPages and the record-count summary.
    /// </summary>
    public int TotalRecords { get; set; }
}

/// <summary>
/// Passed to the OnChanged callback of PaginatedTable whenever the user navigates to a
/// different page or changes the page size. The parent should re-slice its data accordingly.
/// </summary>
public record PageQuery(int Page, int PageSize);
