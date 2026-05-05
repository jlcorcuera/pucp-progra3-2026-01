using Lab07_db_manager;
using Lab07_domain.entities;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao.impl
{
    public class PeliculaDAO : IPeliculaDAO
    {
        public Pelicula findByNombre(string nombre)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();
                string sql = "select id_pelicula, nombre_pelicula, genero_pelicula from pelicula where nombre_pelicula = @nombre";
                using (MySqlCommand command = new MySqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@nombre", nombre);
                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Pelicula pelicula = new Pelicula();
                            pelicula.Id = reader.GetInt32(0);
                            pelicula.NombrePelicula = reader.GetString(1);
                            pelicula.GeneroPelicula = reader.GetString(2);
                            return pelicula;
                        }
                    }
                }
                return null;
            }
        }

        public Pelicula registrar(Pelicula pelicula)
        {
            using (MySqlConnection connection = DBManager.Instance.GetConnectionDestination())
            {
                connection.Open();

                using (MySqlCommand command = new MySqlCommand("INSERTAR_PELICULA", connection))
                {
                    command.CommandType = System.Data.CommandType.StoredProcedure;

                    // Input parameter
                    command.Parameters.AddWithValue("_nombre_pelicula", pelicula.NombrePelicula);
                    command.Parameters.AddWithValue("_genero_pelicula", pelicula.GeneroPelicula);

                    // Output parameter configuration
                    MySqlParameter outParam = new MySqlParameter("_id_pelicula", MySqlDbType.Int32);
                    outParam.Direction = System.Data.ParameterDirection.Output;
                    command.Parameters.Add(outParam);

                    // Execute the stored procedure
                    command.ExecuteNonQuery();

                    // Read the output parameter value
                    int nuevoId = Convert.ToInt32(outParam.Value);
                    pelicula.Id = nuevoId;
                    return pelicula;
                }
            }
        }
    }
}
