using PokemonMigrator_dao.interfaces;
using PokemonMigrator_dbmanager;
using PokemonMigrator_model.model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace PokemonMigrator_dao.impl
{
    public class PokemonDAO : IPokemonDAO
    {
        public void Registrar(Pokemon pokemon)
        {
            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = "insertar_pokemon";
                    command.CommandType = CommandType.StoredProcedure;

                    // Parámetro OUTPUT: @_p_id_tipo
                    IDbDataParameter paramId = command.CreateParameter();
                    paramId.ParameterName = "@p_id_pokemon";
                    paramId.DbType = DbType.Int32;
                    paramId.Direction = ParameterDirection.Output;
                    command.Parameters.Add(paramId);

                    // Parámetro de entrada: @_nombre
                    IDbDataParameter paramNombre = command.CreateParameter();
                    paramNombre.ParameterName = "@p_nombre";
                    paramNombre.DbType = DbType.String;
                    paramNombre.Size = 50;
                    paramNombre.Value = pokemon.NombrePokemon;
                    command.Parameters.Add(paramNombre);

                    IDbDataParameter paramTipo = command.CreateParameter();
                    paramTipo.ParameterName = "@p_fid_tipo";
                    paramTipo.DbType = DbType.Int32;
                    paramTipo.Value = pokemon.TipoPokemon.Id;
                    command.Parameters.Add(paramTipo);


                    IDbDataParameter paramAltura = command.CreateParameter();
                    paramAltura.ParameterName = "@p_altura";
                    paramAltura.DbType = DbType.Double;
                    paramAltura.Value = pokemon.Altura;
                    command.Parameters.Add(paramAltura);

                    IDbDataParameter paramPeso = command.CreateParameter();
                    paramPeso.ParameterName = "@p_peso";
                    paramPeso.DbType = DbType.Double;
                    paramPeso.Value = pokemon.Peso;
                    command.Parameters.Add(paramPeso);

                    string estadoEvolutivoStr = pokemon.EstadoEvolutivo.ToString();
                    IDbDataParameter paramEstadoEvolutivo = command.CreateParameter();
                    paramEstadoEvolutivo.ParameterName = "@p_estado_evolutivo";
                    paramEstadoEvolutivo.DbType = DbType.String;
                    paramEstadoEvolutivo.Size = 50;
                    paramEstadoEvolutivo.Value = estadoEvolutivoStr;
                    command.Parameters.Add(paramEstadoEvolutivo);

                    IDbDataParameter paramDescripcion = command.CreateParameter();
                    paramDescripcion.ParameterName = "@p_descripcion";
                    paramDescripcion.DbType = DbType.String;
                    paramDescripcion.Size = 255;
                    paramDescripcion.Value = pokemon.DescripcionPokemon;
                    command.Parameters.Add(paramDescripcion);

                    command.ExecuteNonQuery();

                    pokemon.Id = Convert.ToInt32(paramId.Value);
                }
            }
        }
    }
    
}
