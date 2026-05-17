using Lab07_domain.entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao
{
    public interface ISucursalDAO
    {
        Sucursal findByNombre(string nombre);
        Sucursal registrar(Sucursal sucursal);
    }
}
