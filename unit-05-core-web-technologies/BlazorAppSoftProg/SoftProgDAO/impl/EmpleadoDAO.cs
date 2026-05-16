using SoftProgDBManager;
using SoftProgDomain.RRRHH;
using System;
using System.Collections.Generic;
using System.Data;

namespace SoftProgDAO
{
    public class EmpleadoDAO : IEmpleadoDAO
    {
        public int insertar(Empleado empleado)
        {
            int idGenerado = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                // 1. Insertar Persona y recuperar el ID generado
                string sqlPersona = "INSERT INTO persona(DNI, nombre, apellido_paterno, sexo, fecha_nacimiento) VALUES (@dni, @nombre, @apellido, @sexo, @fecha); SELECT @@IDENTITY;";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sqlPersona;

                    var pDni = command.CreateParameter(); pDni.ParameterName = "@dni"; pDni.Value = empleado.DNI ?? (object)DBNull.Value; command.Parameters.Add(pDni);
                    var pNombre = command.CreateParameter(); pNombre.ParameterName = "@nombre"; pNombre.Value = empleado.Nombre ?? (object)DBNull.Value; command.Parameters.Add(pNombre);
                    var pApellido = command.CreateParameter(); pApellido.ParameterName = "@apellido"; pApellido.Value = empleado.ApellidoPaterno ?? (object)DBNull.Value; command.Parameters.Add(pApellido);
                    var pSexo = command.CreateParameter(); pSexo.ParameterName = "@sexo"; pSexo.Value = empleado.Sexo ?? (object)DBNull.Value; command.Parameters.Add(pSexo);
                    var pFecha = command.CreateParameter(); pFecha.ParameterName = "@fecha"; pFecha.Value = empleado.FechaNacimiento ?? (object)DBNull.Value; command.Parameters.Add(pFecha);

                    object result = command.ExecuteScalar();
                    if (result != null && result != DBNull.Value)
                    {
                        idGenerado = Convert.ToInt32(result);
                    }
                }

                if (idGenerado > 0)
                {
                    // 2. Insertar Empleado (extiende de persona)
                    string sqlEmpleado = "INSERT INTO empleado(id_empleado, fid_area, cargo, sueldo, activo) VALUES (@id_empleado, @fid_area, @cargo, @sueldo, @activo)";
                    using (IDbCommand commandEmp = connection.CreateCommand())
                    {
                        commandEmp.CommandText = sqlEmpleado;

                        var pId = commandEmp.CreateParameter(); pId.ParameterName = "@id_empleado"; pId.Value = idGenerado; commandEmp.Parameters.Add(pId);
                        
                        // Si el area tiene un ID válido mayor a 0, lo guardamos, sino será nulo
                        var pArea = commandEmp.CreateParameter(); pArea.ParameterName = "@fid_area"; 
                        pArea.Value = (empleado.Area != null && empleado.Area.IdArea > 0) ? empleado.Area.IdArea : (object)DBNull.Value; 
                        commandEmp.Parameters.Add(pArea);
                        
                        var pCargo = commandEmp.CreateParameter(); pCargo.ParameterName = "@cargo"; pCargo.Value = empleado.Cargo ?? (object)DBNull.Value; commandEmp.Parameters.Add(pCargo);
                        var pSueldo = commandEmp.CreateParameter(); pSueldo.ParameterName = "@sueldo"; pSueldo.Value = empleado.Sueldo ?? (object)DBNull.Value; commandEmp.Parameters.Add(pSueldo);
                        var pActivo = commandEmp.CreateParameter(); pActivo.ParameterName = "@activo"; pActivo.Value = empleado.Activo.HasValue && empleado.Activo.Value ? 1 : 0; commandEmp.Parameters.Add(pActivo);

                        commandEmp.ExecuteNonQuery();
                    }
                }
            }
            return idGenerado;
        }

        public int modificar(Empleado empleado)
        {
            int resultado = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                
                // Actualizar Persona
                string sqlPersona = "UPDATE persona SET DNI = @dni, nombre = @nombre, apellido_paterno = @apellido, sexo = @sexo, fecha_nacimiento = @fecha WHERE id_persona = @id_persona";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sqlPersona;

                    var pId = command.CreateParameter(); pId.ParameterName = "@id_persona"; pId.Value = empleado.IdPersona; command.Parameters.Add(pId);
                    var pDni = command.CreateParameter(); pDni.ParameterName = "@dni"; pDni.Value = empleado.DNI ?? (object)DBNull.Value; command.Parameters.Add(pDni);
                    var pNombre = command.CreateParameter(); pNombre.ParameterName = "@nombre"; pNombre.Value = empleado.Nombre ?? (object)DBNull.Value; command.Parameters.Add(pNombre);
                    var pApellido = command.CreateParameter(); pApellido.ParameterName = "@apellido"; pApellido.Value = empleado.ApellidoPaterno ?? (object)DBNull.Value; command.Parameters.Add(pApellido);
                    var pSexo = command.CreateParameter(); pSexo.ParameterName = "@sexo"; pSexo.Value = empleado.Sexo ?? (object)DBNull.Value; command.Parameters.Add(pSexo);
                    var pFecha = command.CreateParameter(); pFecha.ParameterName = "@fecha"; pFecha.Value = empleado.FechaNacimiento ?? (object)DBNull.Value; command.Parameters.Add(pFecha);

                    resultado += command.ExecuteNonQuery();
                }

                // Actualizar Empleado
                string sqlEmpleado = "UPDATE empleado SET fid_area = @fid_area, cargo = @cargo, sueldo = @sueldo, activo = @activo WHERE id_empleado = @id_empleado";
                using (IDbCommand commandEmp = connection.CreateCommand())
                {
                    commandEmp.CommandText = sqlEmpleado;

                    var pId = commandEmp.CreateParameter(); pId.ParameterName = "@id_empleado"; pId.Value = empleado.IdPersona; commandEmp.Parameters.Add(pId);
                    
                    var pArea = commandEmp.CreateParameter(); pArea.ParameterName = "@fid_area"; 
                    pArea.Value = (empleado.Area != null && empleado.Area.IdArea > 0) ? empleado.Area.IdArea : (object)DBNull.Value; 
                    commandEmp.Parameters.Add(pArea);
                    
                    var pCargo = commandEmp.CreateParameter(); pCargo.ParameterName = "@cargo"; pCargo.Value = empleado.Cargo ?? (object)DBNull.Value; commandEmp.Parameters.Add(pCargo);
                    var pSueldo = commandEmp.CreateParameter(); pSueldo.ParameterName = "@sueldo"; pSueldo.Value = empleado.Sueldo ?? (object)DBNull.Value; commandEmp.Parameters.Add(pSueldo);
                    var pActivo = commandEmp.CreateParameter(); pActivo.ParameterName = "@activo"; pActivo.Value = empleado.Activo.HasValue && empleado.Activo.Value ? 1 : 0; commandEmp.Parameters.Add(pActivo);

                    resultado += commandEmp.ExecuteNonQuery();
                }
            }
            return resultado;
        }

        public int eliminar(int idEmpleado)
        {
            int resultado = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                
                // Eliminación lógica: solo actualizamos la tabla empleado
                string sqlEmpleado = "UPDATE empleado SET activo = 0 WHERE id_empleado = @id";
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sqlEmpleado;
                    var pId = command.CreateParameter(); pId.ParameterName = "@id"; pId.Value = idEmpleado; command.Parameters.Add(pId);
                    resultado += command.ExecuteNonQuery();
                }
            }
            return resultado;
        }

        public Empleado obtenerPorId(int idEmpleado)
        {
            Empleado empleado = null;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                string sql = @"
                    SELECT 
                        e.id_empleado, 
                        p.DNI, 
                        p.nombre, 
                        p.apellido_paterno, 
                        p.sexo,
                        p.fecha_nacimiento, 
                        e.fid_area,
                        a.nombre as area_nombre,
                        a.activa as area_activa,
                        e.cargo,
                        e.sueldo,
                        e.activo
                    FROM empleado e
                    INNER JOIN persona p ON e.id_empleado = p.id_persona
                    LEFT JOIN area a ON e.fid_area = a.id_area
                    WHERE e.id_empleado = @id";

                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = sql;
                    var pId = command.CreateParameter(); pId.ParameterName = "@id"; pId.Value = idEmpleado; command.Parameters.Add(pId);
                    
                    using (IDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            empleado = new Empleado();
                            empleado.IdPersona = reader.IsDBNull(reader.GetOrdinal("id_empleado")) ? 0 : Convert.ToInt32(reader["id_empleado"]);
                            empleado.DNI = reader.IsDBNull(reader.GetOrdinal("DNI")) ? null : reader["DNI"].ToString();
                            empleado.Nombre = reader.IsDBNull(reader.GetOrdinal("nombre")) ? null : reader["nombre"].ToString();
                            empleado.ApellidoPaterno = reader.IsDBNull(reader.GetOrdinal("apellido_paterno")) ? null : reader["apellido_paterno"].ToString();
                            string sexoStr = reader.IsDBNull(reader.GetOrdinal("sexo")) ? null : reader["sexo"].ToString();
                            empleado.Sexo = string.IsNullOrEmpty(sexoStr) ? (char?)null : sexoStr[0];
                            empleado.FechaNacimiento = reader.IsDBNull(reader.GetOrdinal("fecha_nacimiento")) ? (DateTime?)null : Convert.ToDateTime(reader["fecha_nacimiento"]);
                            empleado.Cargo = reader.IsDBNull(reader.GetOrdinal("cargo")) ? null : reader["cargo"].ToString();
                            empleado.Sueldo = reader.IsDBNull(reader.GetOrdinal("sueldo")) ? (double?)null : Convert.ToDouble(reader["sueldo"]);
                            empleado.Activo = !reader.IsDBNull(reader.GetOrdinal("activo")) && Convert.ToInt32(reader["activo"]) == 1;
                            
                            Area area = new Area();
                            if (!reader.IsDBNull(reader.GetOrdinal("fid_area")))
                            {
                                area.IdArea = Convert.ToInt32(reader["fid_area"]);
                                area.Name = reader.IsDBNull(reader.GetOrdinal("area_nombre")) ? null : reader["area_nombre"].ToString();
                                area.Activa = !reader.IsDBNull(reader.GetOrdinal("area_activa")) && Convert.ToInt32(reader["area_activa"]) == 1;
                            }
                            empleado.Area = area;
                        }
                    }
                }
            }
            return empleado;
        }

        public List<EmpleadoDTO> listar(string nombre, string apellidoPaterno, int idArea)
        {
            List<EmpleadoDTO> empleados = new List<EmpleadoDTO>();
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                
                // Generamos la consulta SQL base, join persona, empleado y area
                string sql = @"
                    SELECT 
                        e.id_empleado, 
                        p.DNI, 
                        p.nombre, 
                        p.apellido_paterno, 
                        p.fecha_nacimiento, 
                        a.nombre as area_nombre, 
                        e.cargo
                    FROM empleado e
                    INNER JOIN persona p ON e.id_empleado = p.id_persona
                    LEFT JOIN area a ON e.fid_area = a.id_area
                    WHERE e.activo = 1 ";

                using (IDbCommand command = connection.CreateCommand())
                {
                    // Anhadimos filtros de manera dinamica
                    if (!string.IsNullOrWhiteSpace(nombre))
                    {
                        sql += " AND p.nombre LIKE @nombre ";
                        var pNombre = command.CreateParameter(); pNombre.ParameterName = "@nombre"; pNombre.Value = "%" + nombre + "%"; command.Parameters.Add(pNombre);
                    }
                    
                    if (!string.IsNullOrWhiteSpace(apellidoPaterno))
                    {
                        sql += " AND p.apellido_paterno LIKE @apellido ";
                        var pApellido = command.CreateParameter(); pApellido.ParameterName = "@apellido"; pApellido.Value = "%" + apellidoPaterno + "%"; command.Parameters.Add(pApellido);
                    }
                    
                    if (idArea > 0)
                    {
                        sql += " AND e.fid_area = @area ";
                        var pArea = command.CreateParameter(); pArea.ParameterName = "@area"; pArea.Value = idArea; command.Parameters.Add(pArea);
                    }

                    command.CommandText = sql;

                    using (IDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            EmpleadoDTO dto = new EmpleadoDTO();
                            dto.IdEmpleado = reader.IsDBNull(reader.GetOrdinal("id_empleado")) ? 0 : Convert.ToInt32(reader["id_empleado"]);
                            dto.DNI = reader.IsDBNull(reader.GetOrdinal("DNI")) ? null : reader["DNI"].ToString();
                            dto.Nombre = reader.IsDBNull(reader.GetOrdinal("nombre")) ? null : reader["nombre"].ToString();
                            dto.ApellidoPaterno = reader.IsDBNull(reader.GetOrdinal("apellido_paterno")) ? null : reader["apellido_paterno"].ToString();
                            dto.FechaNacimiento = reader.IsDBNull(reader.GetOrdinal("fecha_nacimiento")) ? (DateTime?)null : Convert.ToDateTime(reader["fecha_nacimiento"]);
                            dto.Area = reader.IsDBNull(reader.GetOrdinal("area_nombre")) ? null : reader["area_nombre"].ToString();
                            dto.Cargo = reader.IsDBNull(reader.GetOrdinal("cargo")) ? null : reader["cargo"].ToString();
                            
                            empleados.Add(dto);
                        }
                    }
                }
            }
            return empleados;
        }
    }
}
