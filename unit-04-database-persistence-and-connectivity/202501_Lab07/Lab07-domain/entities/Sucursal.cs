using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_domain.entities
{
    public class Sucursal
    {
        /*
         * id_sucursal	int
nombre_sucursal	varchar(150)*/
        private int id;
        private String nombreSucursal;

        public int Id { get => id; set => id = value; }
        public string NombreSucursal { get => nombreSucursal; set => nombreSucursal = value; }
    }
}
