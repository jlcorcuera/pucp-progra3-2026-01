using Lab07_db_manager;
using Lab07_domain.entities;
using MySql.Data.MySqlClient;
using MySqlX.XDevAPI;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao.impl
{
    public class VentaDAO : IVentaDAO
    {
        public void registrar(Venta venta)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();

                using (MySqlCommand command = new MySqlCommand("INSERTAR_VENTA", connection))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    // Input parameter
                    command.Parameters.AddWithValue("_fid_cliente", venta.Cliente.Id);
                    command.Parameters.AddWithValue("_fid_pelicula", venta.Pelicula.Id);
                    command.Parameters.AddWithValue("_fid_sucursal", venta.Sucursal.Id);
                    command.Parameters.AddWithValue("_fecha_venta", venta.FechaVenta);
                    command.Parameters.AddWithValue("_cantidad_asientos", venta.CantidadAsientos);
                    command.Parameters.AddWithValue("_total_venta", venta.TotalVenta);

                    // Output parameter configuration
                    MySqlParameter outParam = new MySqlParameter("_id_venta", MySqlDbType.Int32);
                    outParam.Direction = System.Data.ParameterDirection.Output;
                    command.Parameters.Add(outParam);

                    // Execute the stored procedure
                    command.ExecuteNonQuery();

                    // Read the output parameter value
                    int nuevoId = Convert.ToInt32(outParam.Value);
                    venta.Id = nuevoId;
                }
            }
        }
    }
}
