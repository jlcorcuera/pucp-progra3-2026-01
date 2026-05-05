using Lab07_db_manager;
using Lab07_domain.dto;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao.impl
{
    public class VentaDNDAO : IVentaDNDAO
    {
        public List<VentaDNDTO> listAll()
        {
            List<VentaDNDTO> results = new List<VentaDNDTO>();
            using (MySqlConnection connection = DBManager.Instance.GetConnectionSource())
            {
                connection.Open();
                string sql = "select id_venta, nombre_cliente, apellido_cliente, email_cliente, nombre_pelicula, genero_pelicula, nombre_sucursal, fecha_venta, cantidad_asientos, total_venta from venta_dn";
                using (MySqlCommand command = new MySqlCommand(sql, connection))
                {
                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            VentaDNDTO item = new VentaDNDTO();
                            item.Id = reader.GetInt32(0);
                            item.NombreCliente = reader.GetString(1);
                            item.ApellidoCliente = reader.GetString(2);
                            item.EmailCliente = reader.GetString(3);
                            item.NombrePelicula = reader.GetString(4);
                            item.GeneroPelicula = reader.GetString(5);
                            item.NombreSucursal = reader.GetString(6);
                            item.FechaVenta = reader.GetDateTime(7);
                            item.CantidadAsientos = reader.GetInt32(8);
                            item.TotalVenta = reader.GetDouble(9);
                            results.Add(item);
                        }
                    }
                    return results;
                }
            }
        }
    }
}
