using SoftProgDAO;
using SoftProgDomain.RRRHH;
using System.Collections.Generic;

namespace SoftProgBL.RRRHH
{
    public class EmpleadoBL : IEmpleadoBL
    {
        private readonly IEmpleadoDAO _empleadoDAO;

        public EmpleadoBL()
        {
            _empleadoDAO = new EmpleadoDAO();
        }

        public int insertar(Empleado empleado)
        {
            return _empleadoDAO.insertar(empleado);
        }

        public int modificar(Empleado empleado)
        {
            return _empleadoDAO.modificar(empleado);
        }

        public int eliminar(int idEmpleado)
        {
            return _empleadoDAO.eliminar(idEmpleado);
        }

        public Empleado obtenerPorId(int idEmpleado)
        {
            return _empleadoDAO.obtenerPorId(idEmpleado);
        }

        public List<EmpleadoDTO> listar(string nombre, string apellidoPaterno, int idArea)
        {
            return _empleadoDAO.listar(nombre, apellidoPaterno, idArea);
        }
    }
}
