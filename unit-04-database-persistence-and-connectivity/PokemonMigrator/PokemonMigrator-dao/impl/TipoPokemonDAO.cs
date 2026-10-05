using PokemonMigrator_dao.interfaces;
using PokemonMigrator_dbmanager;
using PokemonMigrator_model.model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace PokemonMigrator_dao.impl
{
    public class TipoPokemonDAO : ITipoPokemonDAO
    {
        public TipoPokemon ObtenerPorNombre(string nombre)
        {
            int idTipo = 0;
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = "obtener_tipo_pokemon_por_nombre";
                    command.CommandType = CommandType.StoredProcedure;

                    // Parámetro de entrada: @_nombre
                    IDbDataParameter paramNombre = command.CreateParameter();
                    paramNombre.ParameterName = "@p_nombre";
                    paramNombre.DbType = DbType.String;
                    paramNombre.Size = 50;
                    paramNombre.Value = nombre;
                    command.Parameters.Add(paramNombre);

                    // Parámetro OUTPUT: @_p_id_tipo
                    IDbDataParameter paramId = command.CreateParameter();
                    paramId.ParameterName = "@p_id_tipo";
                    paramId.DbType = DbType.Int32;
                    paramId.Direction = ParameterDirection.Output;
                    command.Parameters.Add(paramId);

                    command.ExecuteNonQuery();

                    if (DBNull.Value != paramId.Value)
                    {
                        idTipo = Convert.ToInt32(paramId.Value);
                    }

                    
                }
            }
            TipoPokemon tipoPokemon = new TipoPokemon();
            tipoPokemon.Nombre = nombre;
            tipoPokemon.Id = idTipo;
            return tipoPokemon;
        }

        public void Registrar(TipoPokemon tipoPokemon)
        {
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = "insertar_tipo_pokemon";
                    command.CommandType = CommandType.StoredProcedure;

                    // Parámetro de entrada: @_nombre
                    IDbDataParameter paramNombre = command.CreateParameter();
                    paramNombre.ParameterName = "@p_nombre";
                    paramNombre.DbType = DbType.String;
                    paramNombre.Size = 50;
                    paramNombre.Value = tipoPokemon.Nombre;
                    command.Parameters.Add(paramNombre);

                    // Parámetro OUTPUT: @_p_id_tipo
                    IDbDataParameter paramId = command.CreateParameter();
                    paramId.ParameterName = "@p_id_tipo";
                    paramId.DbType = DbType.Int32;
                    paramId.Direction = ParameterDirection.Output;
                    command.Parameters.Add(paramId);

                    command.ExecuteNonQuery();

                    tipoPokemon.Id = Convert.ToInt32(paramId.Value);
                }
            }
        }
    }
}
