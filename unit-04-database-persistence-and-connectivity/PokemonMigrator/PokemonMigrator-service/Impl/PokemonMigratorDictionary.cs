using PokemonMigrator_dao.impl;
using PokemonMigrator_dao.interfaces;
using PokemonMigrator_model.dto;
using PokemonMigrator_model.model;
using PokemonMigrator_service.Interfaces;
using System;
using System.Collections.Generic;
using System.Text;

namespace PokemonMigrator_service.Impl
{
    public class PokemonMigratorDictionary : IPokemonMigrator
    {
        public void migrate()
        {
            Dictionary<string, TipoPokemon> cacheTipoPokemon = new Dictionary<string, TipoPokemon>();

            IPokemonTipoRawDAO pokemonTipoRawDAO = new PokemonTipoRawDAO();
            ITipoPokemonDAO tipoPokemonDAO = new TipoPokemonDAO();
            IPokemonDAO pokemonDAO = new PokemonDAO();
            List<PokemonTipoRawDTO> pokemonTipoRawDTOList = pokemonTipoRawDAO.GetAll();
            foreach (PokemonTipoRawDTO dto in pokemonTipoRawDTOList)
            {
                TipoPokemon tipoPokemon = null;
                if (cacheTipoPokemon.ContainsKey(dto.NombreTipo))
                {
                    tipoPokemon = cacheTipoPokemon[dto.NombreTipo];
                } else
                {
                    tipoPokemon = new TipoPokemon();
                    tipoPokemon.Nombre = dto.NombreTipo;
                    tipoPokemonDAO.Registrar(tipoPokemon);
                    cacheTipoPokemon.Add(dto.NombreTipo, tipoPokemon);
                }
                Pokemon pokemon = new Pokemon();
                pokemon.TipoPokemon = tipoPokemon;
                pokemon.NombrePokemon = dto.NombrePokemon;
                pokemon.Altura = dto.Altura;
                pokemon.Peso = dto.Peso;
                pokemon.EstadoEvolutivo = dto.EstadoEvolutivo;
                pokemon.DescripcionPokemon = dto.DescripcionPokemon;
                pokemonDAO.Registrar(pokemon);
            }
        }
    }
}
