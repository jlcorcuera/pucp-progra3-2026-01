using SoftProgDBManager;
using SoftProgDomain.RRRHH;
using System;
using System.Collections.Generic;
using System.Data;

namespace SoftProgDAO
{
    public class AreaDAO : IAreaDAO
    {
        public int insertar(Area area)
        {
            int resultado = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "INSERT INTO area(nombre, activa) VALUES (@nombre, @activa)";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;

                    IDbDataParameter param1 = command.CreateParameter();
                    param1.ParameterName = "@nombre";
                    param1.Value = area.Name ?? (object)DBNull.Value;
                    command.Parameters.Add(param1);

                    IDbDataParameter param2 = command.CreateParameter();
                    param2.ParameterName = "@activa";
                    param2.Value = area.Activa ? 1 : 0;
                    command.Parameters.Add(param2);

                    resultado = command.ExecuteNonQuery();
                }
            }
            return resultado;
        }

        public int modificar(Area area)
        {
            int resultado = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "UPDATE area SET nombre = @nombre, activa = @activa WHERE id_area = @id_area";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;

                    IDbDataParameter param1 = command.CreateParameter();
                    param1.ParameterName = "@nombre";
                    param1.Value = area.Name ?? (object)DBNull.Value;
                    command.Parameters.Add(param1);

                    IDbDataParameter param2 = command.CreateParameter();
                    param2.ParameterName = "@activa";
                    param2.Value = area.Activa ? 1 : 0;
                    command.Parameters.Add(param2);

                    IDbDataParameter param3 = command.CreateParameter();
                    param3.ParameterName = "@id_area";
                    param3.Value = area.IdArea;
                    command.Parameters.Add(param3);

                    resultado = command.ExecuteNonQuery();
                }
            }
            return resultado;
        }

        public int eliminar(int idArea)
        {
            int resultado = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "UPDATE area SET activa = 0 WHERE id_area = @id_area";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;

                    IDbDataParameter param1 = command.CreateParameter();
                    param1.ParameterName = "@id_area";
                    param1.Value = idArea;
                    command.Parameters.Add(param1);

                    resultado = command.ExecuteNonQuery();
                }
            }
            return resultado;
        }

        public List<Area> listarTodas()
        {
            List<Area> areas = new List<Area>();
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "SELECT id_area, nombre, activa FROM area WHERE activa = 1";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;
                    using (IDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Area area = new Area();
                            area.IdArea = reader.IsDBNull(reader.GetOrdinal("id_area")) ? 0 : Convert.ToInt32(reader["id_area"]);
                            area.Name = reader.IsDBNull(reader.GetOrdinal("nombre")) ? null : reader["nombre"].ToString();
                            area.Activa = reader.IsDBNull(reader.GetOrdinal("activa")) ? false : Convert.ToBoolean(reader["activa"]);
                            areas.Add(area);
                        }
                    }
                }
            }
            return areas;
        }

        public Area obtenerPorId(int idArea)
        {
            Area area = null;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "SELECT id_area, nombre, activa FROM area WHERE id_area = @id";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;
                    var pId = command.CreateParameter(); pId.ParameterName = "@id"; pId.Value = idArea; command.Parameters.Add(pId);
                    
                    using (IDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            area = new Area();
                            area.IdArea = reader.IsDBNull(reader.GetOrdinal("id_area")) ? 0 : Convert.ToInt32(reader["id_area"]);
                            area.Name = reader.IsDBNull(reader.GetOrdinal("nombre")) ? null : reader["nombre"].ToString();
                            area.Activa = reader.IsDBNull(reader.GetOrdinal("activa")) ? false : Convert.ToBoolean(reader["activa"]);
                        }
                    }
                }
            }
            return area;
        }

        public List<Area> listarPorNombre(string nombre)
        {
            List<Area> areas = new List<Area>();
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "SELECT id_area, nombre, activa FROM area WHERE nombre LIKE @nombre AND activa = 1";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;

                    IDbDataParameter param1 = command.CreateParameter();
                    param1.ParameterName = "@nombre";
                    // Se agrega % antes y despues para buscar el texto en cualquier parte del nombre
                    param1.Value = "%" + nombre + "%";
                    command.Parameters.Add(param1);

                    using (IDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Area area = new Area();
                            area.IdArea = reader.IsDBNull(reader.GetOrdinal("id_area")) ? 0 : Convert.ToInt32(reader["id_area"]);
                            area.Name = reader.IsDBNull(reader.GetOrdinal("nombre")) ? null : reader["nombre"].ToString();
                            area.Activa = reader.IsDBNull(reader.GetOrdinal("activa")) ? false : Convert.ToBoolean(reader["activa"]);
                            areas.Add(area);
                        }
                    }
                }
            }
            return areas;
        }
    }
}
