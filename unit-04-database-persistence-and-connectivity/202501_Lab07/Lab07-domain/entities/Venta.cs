using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_domain.entities
{
    public class Venta
    {
        /*
         id_venta	int
fid_cliente	int
fid_pelicula	int
fid_sucursal	int
fecha_venta	date
cantidad_asientos	int
total_venta	decimal(10,2)
         */
        private int id;
        private Cliente cliente;
        private Pelicula pelicula;
        private Sucursal sucursal;
        private DateTime fechaVenta;
        private int cantidadAsientos;
        private double totalVenta;

        public int Id { get => id; set => id = value; }
        public Cliente Cliente { get => cliente; set => cliente = value; }
        public Pelicula Pelicula { get => pelicula; set => pelicula = value; }
        public Sucursal Sucursal { get => sucursal; set => sucursal = value; }
        public DateTime FechaVenta { get => fechaVenta; set => fechaVenta = value; }
        public int CantidadAsientos { get => cantidadAsientos; set => cantidadAsientos = value; }
        public double TotalVenta { get => totalVenta; set => totalVenta = value; }
    }
}
