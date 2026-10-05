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
    public class PokemonMigrator : IPokemonMigrator
    {
        public void migrate()
        {
            IPokemonTipoRawDAO pokemonTipoRawDAO = new PokemonTipoRawDAO();
            ITipoPokemonDAO tipoPokemonDAO = new TipoPokemonDAO();
            IPokemonDAO pokemonDAO = new PokemonDAO();
            List<PokemonTipoRawDTO> pokemonTipoRawDTOList = pokemonTipoRawDAO.GetAll();
            foreach (PokemonTipoRawDTO dto in pokemonTipoRawDTOList)
            {
                TipoPokemon tipoPokemon = tipoPokemonDAO.ObtenerPorNombre(dto.NombreTipo);
                if (tipoPokemon.Id == 0 || tipoPokemon.Id == null)
                {
                    tipoPokemonDAO.Registrar(tipoPokemon);
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
