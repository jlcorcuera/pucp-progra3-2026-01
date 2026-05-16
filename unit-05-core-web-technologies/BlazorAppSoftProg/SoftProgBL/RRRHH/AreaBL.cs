using SoftProgDAO;
using SoftProgDomain.RRRHH;
using System.Collections.Generic;

namespace SoftProgBL.RRRHH
{
    public class AreaBL : IAreaBL
    {
        private readonly IAreaDAO _areaDAO;

        public AreaBL()
        {
            _areaDAO = new AreaDAO();
        }

        public int insertar(Area area)
        {
            return _areaDAO.insertar(area);
        }

        public int modificar(Area area)
        {
            return _areaDAO.modificar(area);
        }

        public int eliminar(int idArea)
        {
            return _areaDAO.eliminar(idArea);
        }

        public Area obtenerPorId(int idArea)
        {
            return _areaDAO.obtenerPorId(idArea);
        }

        public List<Area> listarTodas()
        {
            return _areaDAO.listarTodas();
        }

        public List<Area> listarPorNombre(string nombre)
        {
            return _areaDAO.listarPorNombre(nombre);
        }
    }
}
