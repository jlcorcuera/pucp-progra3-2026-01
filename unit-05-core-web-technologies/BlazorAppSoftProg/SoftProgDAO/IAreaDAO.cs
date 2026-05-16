using SoftProgDomain.RRRHH;
using System.Collections.Generic;

namespace SoftProgDAO
{
    public interface IAreaDAO
    {
        int insertar(Area area);
        int modificar(Area area);
        int eliminar(int idArea);
        Area obtenerPorId(int idArea);
        List<Area> listarTodas();
        List<Area> listarPorNombre(string nombre);
    }
}
