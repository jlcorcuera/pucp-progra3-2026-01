using System;
using System.Collections.Generic;
using System.Text;

namespace PokemonMigrator_model.model
{
    public class TipoPokemon
    {
        private int _id;
        private string _nombre;

        public int Id { get => _id; set => _id = value; }
        public string Nombre { get => _nombre; set => _nombre = value; }
    }
}
