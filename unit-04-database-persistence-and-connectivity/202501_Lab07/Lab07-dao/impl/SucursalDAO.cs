using Lab07_db_manager;
using Lab07_domain.dto;
using Lab07_domain.entities;
using MySql.Data.MySqlClient;
using MySqlX.XDevAPI.Common;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao.impl
{
    public class SucursalDAO : ISucursalDAO
    {
        public Sucursal findByNombre(string nombre)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();
                string sql = "select id_sucursal, nombre_sucursal from sucursal where nombre_sucursal = @nombre";
                using (MySqlCommand command = new MySqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@nombre", nombre);
                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Sucursal sucursal = new Sucursal();
                            sucursal.Id = reader.GetInt32(0);
                            sucursal.NombreSucursal = reader.GetString(1);
                            return sucursal;
                        }
                    }
                }
                return null;
            }
        }

        public Sucursal registrar(Sucursal sucursal)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();

                using (MySqlCommand command = new MySqlCommand("INSERTAR_SUCURSAL", connection))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    // Input parameter
                    command.Parameters.AddWithValue("_nombre_sucursal", sucursal.NombreSucursal);

                    // Output parameter configuration
                    MySqlParameter outParam = new MySqlParameter("_id_sucursal", MySqlDbType.Int32);
                    outParam.Direction = System.Data.ParameterDirection.Output;
                    command.Parameters.Add(outParam);   

                    // Execute the stored procedure
                    command.ExecuteNonQuery();

                    // Read the output parameter value
                    int nuevoId = Convert.ToInt32(outParam.Value);
                    sucursal.Id = nuevoId;
                    return sucursal;
                }
            }
        }
    }
}
