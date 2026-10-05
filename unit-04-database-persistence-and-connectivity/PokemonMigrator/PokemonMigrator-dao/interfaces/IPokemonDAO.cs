using PokemonMigrator_model.model;
using System;
using System.Collections.Generic;
using System.Text;

namespace PokemonMigrator_dao.interfaces
{
    public interface IPokemonDAO
    {
        public void Registrar(Pokemon pokemon);
    }
}
