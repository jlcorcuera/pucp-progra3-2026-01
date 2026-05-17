using Lab07_dao;
using Lab07_dao.impl;
using Lab07_domain.dto;
using Lab07_domain.entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_business_logic
{
    public class MigratorBL : IMigratorBL
    {
        public void run()
        {
            ISucursalDAO sucursalDAO = new SucursalDAO();
            IPeliculaDAO peliculaDAO = new PeliculaDAO();
            IClienteDAO clienteDAO = new ClienteDAO();
            IVentaDAO ventaDAO = new VentaDAO();

            List<VentaDNDTO> ventas = new VentaDNDAO().listAll();
            foreach(VentaDNDTO item in ventas)
            {
                string nombreSucursal = item.NombreSucursal;
                Sucursal sucursal = sucursalDAO.findByNombre(nombreSucursal);
                if (sucursal == null)
                {
                    sucursal = new Sucursal();
                    sucursal.NombreSucursal = nombreSucursal;
                    sucursal = sucursalDAO.registrar(sucursal);
                }

                string nombrePelicula = item.NombrePelicula;
                Pelicula pelicula = peliculaDAO.findByNombre(nombrePelicula);
                if (pelicula == null)
                {
                    pelicula = new Pelicula();
                    pelicula.NombrePelicula = item.NombrePelicula;
                    pelicula.GeneroPelicula = item.GeneroPelicula;
                    pelicula = peliculaDAO.registrar(pelicula);
                }

                string email = item.EmailCliente;
                Cliente cliente = clienteDAO.findByEmail(email);
                if (cliente == null)
                {
                    cliente = new Cliente();
                    cliente.NombreCliente = item.NombreCliente;
                    cliente.ApellidoCliente = item.ApellidoCliente;
                    cliente.EmailCliente = item.EmailCliente;
                    cliente = clienteDAO.registrar(cliente);
                }

                Venta venta = new Venta();
                venta.TotalVenta = item.TotalVenta;
                venta.CantidadAsientos = item.CantidadAsientos;
                venta.FechaVenta = item.FechaVenta;
                venta.Sucursal = sucursal;
                venta.Cliente = cliente;
                venta.Pelicula = pelicula;

                ventaDAO.registrar(venta);

            }
        }
    }
}
