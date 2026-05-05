using Lab07_db_manager;
using Lab07_domain.entities;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Text;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Lab07_dao.impl
{
    public class ClienteDAO : IClienteDAO
    {
        public Cliente findByEmail(string email)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();
                string sql = "select id_cliente, nombre_cliente, apellido_cliente, email_cliente from cliente where email_cliente = @email";
                using (MySqlCommand command = new MySqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@email", email);
                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Cliente cliente = new Cliente();
                            cliente.Id = reader.GetInt32(0);
                            cliente.NombreCliente = reader.GetString(1);
                            cliente.ApellidoCliente = reader.GetString(2);
                            cliente.EmailCliente = reader.GetString(3);
                            return cliente;
                        }
                    }
                }
                return null;
            }
        }

        public Cliente registrar(Cliente cliente)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();

                using (MySqlCommand command = new MySqlCommand("INSERTAR_CLIENTE", connection))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    // Input parameter
                    command.Parameters.AddWithValue("_nombre_cliente", cliente.NombreCliente);
                    command.Parameters.AddWithValue("_apellido_cliente", cliente.ApellidoCliente);
                    command.Parameters.AddWithValue("_email_cliente", cliente.EmailCliente);

                    // Output parameter configuration
                    MySqlParameter outParam = new MySqlParameter("_id_cliente", MySqlDbType.Int32);
                    outParam.Direction = System.Data.ParameterDirection.Output;
                    command.Parameters.Add(outParam);

                    // Execute the stored procedure
                    command.ExecuteNonQuery();

                    // Read the output parameter value
                    int nuevoId = Convert.ToInt32(outParam.Value);
                    cliente.Id = nuevoId;
                    return cliente;
                }
            }
        }
    }
}
