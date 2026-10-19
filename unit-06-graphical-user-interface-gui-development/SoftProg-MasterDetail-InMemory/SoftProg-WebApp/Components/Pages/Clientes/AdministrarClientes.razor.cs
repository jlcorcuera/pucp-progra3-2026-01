using SoftProg_WebApp.Components.Shared;
using SoftProg_WebApp.Models;

namespace SoftProg_WebApp.Components.Pages.Clientes
{
    public partial class AdministrarClientes
    {
        private List<Cliente> todosLosClientes = new();
        private List<Cliente> clientesFiltrados = new();

        private TablePage<Cliente> paginaClientes = new();
        private PageQuery queryActual = new(1, 3);

        // Filtros
        private string filtroNombre = "";
        private string filtroApellido = "";
        private string filtroDNI = "";
        //private CustomerWSClient customerWSClient = new CustomerWSClient();

        protected override void OnInitialized()
        {
            todosLosClientes = clienteLogic.listar();
            AplicarFiltros();
            ActualizarPagina();

        }

        private void VerDetalles(int idCliente)
        {
            Console.WriteLine($"Viendo detalles del cliente {idCliente}");
        }

        private void OnPageChanged(PageQuery query)
        {
            queryActual = query;
            AplicarFiltros();
            ActualizarPagina();
        }

        private void ActualizarPagina()
        {
            paginaClientes = new TablePage<Cliente>
            {
                Items = clientesFiltrados,
                TotalRecords = queryActual.PageSize
            };
        }

        private void AplicarFiltros()
        {
            var query = todosLosClientes.AsQueryable();

            if (!string.IsNullOrWhiteSpace(filtroNombre))
                query = query.Where(c => c.Nombre.Contains(filtroNombre, StringComparison.OrdinalIgnoreCase));

            if (!string.IsNullOrWhiteSpace(filtroApellido))
                query = query.Where(c => c.ApellidoPaterno.Contains(filtroApellido, StringComparison.OrdinalIgnoreCase));

            if (!string.IsNullOrWhiteSpace(filtroDNI))
                query = query.Where(c => c.DNI.Contains(filtroDNI, StringComparison.OrdinalIgnoreCase));

            clientesFiltrados = query.ToList();

            queryActual = new PageQuery(1, queryActual.PageSize);
            ActualizarPagina();
        }

        private void LimpiarFiltros()
        {
            filtroNombre = "";
            filtroApellido = "";
            filtroDNI = "";
            clientesFiltrados = new List<Cliente>(todosLosClientes);

            queryActual = new PageQuery(1, queryActual.PageSize);
            ActualizarPagina();
        }
    }
}
