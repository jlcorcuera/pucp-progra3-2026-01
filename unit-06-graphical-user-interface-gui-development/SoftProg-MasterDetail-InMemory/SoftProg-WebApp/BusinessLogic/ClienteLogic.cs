using SoftProg_WebApp.Models;

namespace SoftProg_WebApp.BusinessLogic
{
    public class ClienteLogic
    {
        public List<Cliente> listar()
        {
            return Cliente.ObtenerClientesEstaticos();
        }
    }
}
