using System;
using System.Collections.Generic;
using System.Text;

namespace SoftProgDomain.RRRHH
{
    public class Empleado : Persona
    {
        private Area? _area;
        private string? _cargo;
        private double? _sueldo;
        private bool? _activo;

        public Area? Area { get => _area; set => _area = value; }
        public string? Cargo { get => _cargo; set => _cargo = value; }
        public double? Sueldo { get => _sueldo; set => _sueldo = value; }
        public bool? Activo { get => _activo; set => _activo = value; }
    }
}
