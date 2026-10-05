using PokemonMigrator_model.dto;
using System;
using System.Collections.Generic;
using System.Text;

namespace PokemonMigrator_dao.interfaces
{
    public interface IPokemonTipoRawDAO
    {
        List<PokemonTipoRawDTO> GetAll();
    }
}
