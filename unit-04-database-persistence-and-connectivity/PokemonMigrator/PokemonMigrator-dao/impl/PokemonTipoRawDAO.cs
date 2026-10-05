using PokemonMigrator_dao.interfaces;
using PokemonMigrator_dbmanager;
using PokemonMigrator_model.dto;
using PokemonMigrator_model.enums;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace PokemonMigrator_dao.impl
{
    public class PokemonTipoRawDAO : IPokemonTipoRawDAO
    {
        public List<PokemonTipoRawDTO> GetAll()
        {
            List<PokemonTipoRawDTO> resultados = new List<PokemonTipoRawDTO>();

            using (IDbConnection connection = DBManager.Instance.GetConnection())
            {
                connection.Open();
                using (IDbCommand command = connection.CreateCommand())
                {
                    command.CommandText = "listar_pokemon_tipo_raw";
                    command.CommandType = CommandType.StoredProcedure;

                    using (IDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            PokemonTipoRawDTO instance = new PokemonTipoRawDTO();
                            instance.Id = reader.GetInt32(0);
                            instance.NombrePokemon = reader.GetString(1);
                            instance.Altura = reader.GetDouble(2);
                            instance.Peso = reader.GetDouble(3);
                            string estadoEvolutivoStr = reader.GetString(4);
                            EstadoEvolutivo estadoEvolutivo = (EstadoEvolutivo)Enum.Parse(typeof(EstadoEvolutivo), estadoEvolutivoStr);
                            instance.EstadoEvolutivo = estadoEvolutivo;
                            instance.NombreTipo = reader.GetString(5);
                            instance.DescripcionPokemon = reader.GetString(6);
                            resultados.Add(instance);
                        }
                    }
                }
            }
            return resultados;
        }
    }
}
