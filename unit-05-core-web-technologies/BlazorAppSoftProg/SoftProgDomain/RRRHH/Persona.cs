using System;
using System.Collections.Generic;
using System.Text;

namespace SoftProgDomain.RRRHH
{
    public class Persona
    {
        private int _idPersona;
        private string? _dni;
        private string? _nombre;
        private string? _apellidoPaterno;
        private char? _sexo;
        private DateTime? _fechaNacimiento;

        public int IdPersona { get => _idPersona; set => _idPersona = value; }
        public string? DNI { get => _dni; set => _dni = value; }
        public string? Nombre { get => _nombre; set => _nombre = value; }
        public string? ApellidoPaterno { get => _apellidoPaterno; set => _apellidoPaterno = value; }
        public char? Sexo { get => _sexo; set => _sexo = value; }
        public DateTime? FechaNacimiento { get => _fechaNacimiento; set => _fechaNacimiento = value; }
    }
}
