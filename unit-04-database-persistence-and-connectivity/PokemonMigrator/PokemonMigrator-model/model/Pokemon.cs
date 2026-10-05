using PokemonMigrator_model.enums;
using System;
using System.Collections.Generic;
using System.Text;

namespace PokemonMigrator_model.model
{
    public class Pokemon
    {
        private int _id;
        private TipoPokemon _tipoPokemon;
        private string _nombrePokemon;
        private double _altura;
        private double _peso;
        private EstadoEvolutivo _estadoEvolutivo;
        private string _descripcionPokemon;

        public int Id { get => _id; set => _id = value; }
        public TipoPokemon TipoPokemon { get => _tipoPokemon; set => _tipoPokemon = value; }
        public string NombrePokemon { get => _nombrePokemon; set => _nombrePokemon = value; }
        public double Altura { get => _altura; set => _altura = value; }
        public double Peso { get => _peso; set => _peso = value; }
        public EstadoEvolutivo EstadoEvolutivo { get => _estadoEvolutivo; set => _estadoEvolutivo = value; }
        public string DescripcionPokemon { get => _descripcionPokemon; set => _descripcionPokemon = value; }
    }
}
