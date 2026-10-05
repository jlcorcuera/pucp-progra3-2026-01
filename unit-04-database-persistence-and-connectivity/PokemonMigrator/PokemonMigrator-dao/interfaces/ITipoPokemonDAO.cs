using PokemonMigrator_model.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace PokemonMigrator_dao.interfaces
{
    public interface ITipoPokemonDAO
    {
        public void Registrar(TipoPokemon tipoPokemon);
        public TipoPokemon ObtenerPorNombre(string nombre);
    }
}
