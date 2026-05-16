using System;

namespace SoftProgDomain.RRRHH
{
    public class EmpleadoDTO
    {
        private int _idEmpleado;
        private string? _dni;
        private string? _nombre;
        private string? _apellidoPaterno;
        private DateTime? _fechaNacimiento;
        private string? _area; // Representa el nombre del área
        private string? _cargo;
        private decimal? _sueldo;

        public int IdEmpleado { get => _idEmpleado; set => _idEmpleado = value; }
        public string? DNI { get => _dni; set => _dni = value; }
        public string? Nombre { get => _nombre; set => _nombre = value; }
        public string? ApellidoPaterno { get => _apellidoPaterno; set => _apellidoPaterno = value; }
        public DateTime? FechaNacimiento { get => _fechaNacimiento; set => _fechaNacimiento = value; }
        public string? Area { get => _area; set => _area = value; }
        public string? Cargo { get => _cargo; set => _cargo = value; }
        public decimal? Sueldo { get => _sueldo; set => _sueldo = value; }
    }
}
