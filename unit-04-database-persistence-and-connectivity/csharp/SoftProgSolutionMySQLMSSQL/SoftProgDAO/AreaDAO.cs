using SoftProgDBManager;
using SoftProgDomain.RRRHH;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace SoftProgDAO
{
    public class AreaDAO
    {
        // V1: Registro mediante INSERT directo
        public void registrarV1(Area area)
        {
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "INSERT INTO area(nombre, activa) VALUES (@nombre, 1)";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;

                    IDbDataParameter paramNombre = command.CreateParameter();
                    paramNombre.ParameterName = "@nombre";
                    paramNombre.Value = area.Name;
                    command.Parameters.Add(paramNombre);

                    int filasAfectadas = command.ExecuteNonQuery();
                    Console.WriteLine("Filas insertadas: " + filasAfectadas);
                }
            }
        }

        // V2: Registro mediante stored procedure INSERTAR_AREA (con parámetro OUTPUT)
        public void registrarV2(Area area)
        {
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = "INSERTAR_AREA";
                    command.CommandType = CommandType.StoredProcedure;

                    // Parámetro OUTPUT: @_id_area
                    IDbDataParameter paramIdArea = command.CreateParameter();
                    paramIdArea.ParameterName = "@_id_area";
                    paramIdArea.DbType = DbType.Int32;
                    paramIdArea.Direction = ParameterDirection.Output;
                    command.Parameters.Add(paramIdArea);

                    // Parámetro de entrada: @_nombre
                    IDbDataParameter paramNombre = command.CreateParameter();
                    paramNombre.ParameterName = "@_nombre";
                    paramNombre.DbType = DbType.String;
                    paramNombre.Size = 30;
                    paramNombre.Value = area.Name;
                    command.Parameters.Add(paramNombre);

                    command.ExecuteNonQuery();

                    // Recuperar el id generado desde el parámetro OUTPUT
                    area.IdArea = Convert.ToInt32(paramIdArea.Value);
                    Console.WriteLine("Área registrada con ID: " + area.IdArea);
                }
            }
        }

        // V1: Listado mediante SELECT directo
        public List<Area> listarV1()
        {
            List<Area> areas = new List<Area>();

            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = "SELECT id_area, nombre FROM area WHERE activa = 1";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;

                    using (IDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Area area = new Area();
                            area.IdArea = reader.GetInt32(0);
                            area.Name   = reader.GetString(1);
                            area.Activa = true;
                            areas.Add(area);
                        }
                    }
                }
            }

            return areas;
        }

        // V2: Listado mediante stored procedure LISTAR_AREAS_TODAS
        public List<Area> listarV2()
        {
            List<Area> areas = new List<Area>();

            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = "LISTAR_AREAS_TODAS";
                    command.CommandType = CommandType.StoredProcedure;

                    using (IDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            Area area = new Area();
                            area.IdArea = reader.GetInt32(0);
                            area.Name   = reader.GetString(1);
                            area.Activa = true;
                            areas.Add(area);
                        }
                    }
                }
            }

            return areas;
        }
    }
}

