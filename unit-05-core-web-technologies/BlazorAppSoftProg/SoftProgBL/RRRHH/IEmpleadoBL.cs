using SoftProgDomain.RRRHH;
using System.Collections.Generic;

namespace SoftProgBL.RRRHH
{
    public interface IEmpleadoBL
    {
        int insertar(Empleado empleado);
        int modificar(Empleado empleado);
        int eliminar(int idEmpleado);
        Empleado obtenerPorId(int idEmpleado);
        List<EmpleadoDTO> listar(string nombre, string apellidoPaterno, int idArea);
    }
}
