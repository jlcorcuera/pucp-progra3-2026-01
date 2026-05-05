using Microsoft.VisualBasic;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_domain.dto
{
    public class VentaDNDTO
    {
        /*
         * id_venta	int
nombre_cliente	varchar(50)
apellido_cliente	varchar(50)
email_cliente	varchar(75)
nombre_pelicula	varchar(150)
genero_pelicula	enum('ACCION','ANIMACION','DRAMA','CIENCIA FICCION')
nombre_sucursal	varchar(150)
fecha_venta	date
cantidad_asientos	int
total_venta	decimal(10,2)
         */
        private int id;
        private String nombreCliente;
        private String apellidoCliente;
        private String emailCliente;
        private String nombrePelicula;
        private String generoPelicula;
        private String nombreSucursal;
        private DateTime fechaVenta;
        private int cantidadAsientos;
        private double totalVenta;

        public int Id { get => id; set => id = value; }
        public string NombreCliente { get => nombreCliente; set => nombreCliente = value; }
        public string ApellidoCliente { get => apellidoCliente; set => apellidoCliente = value; }
        public string EmailCliente { get => emailCliente; set => emailCliente = value; }
        public string NombrePelicula { get => nombrePelicula; set => nombrePelicula = value; }
        public string GeneroPelicula { get => generoPelicula; set => generoPelicula = value; }
        public string NombreSucursal { get => nombreSucursal; set => nombreSucursal = value; }
        public DateTime FechaVenta { get => fechaVenta; set => fechaVenta = value; }
        public int CantidadAsientos { get => cantidadAsientos; set => cantidadAsientos = value; }
        public double TotalVenta { get => totalVenta; set => totalVenta = value; }
    }
}
