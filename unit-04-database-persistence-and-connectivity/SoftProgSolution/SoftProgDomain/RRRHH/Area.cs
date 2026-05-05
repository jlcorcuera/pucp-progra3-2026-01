using System;
using System.Collections.Generic;
using System.Text;

namespace SoftProgDomain.RRRHH
{
    public class Area
    {
        private int _idArea;
        private String? _name;
        private bool _activa;

        public int IdArea { get => _idArea; set => _idArea = value; }
        public string? Name { get => _name; set => _name = value; }
        public bool Activa { get => _activa; set => _activa = value; }
    }
}
