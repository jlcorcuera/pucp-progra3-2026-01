using System;
using System.Collections.Generic;

namespace SoftProg_WebApp.Models
{
    public class Empleado
    {
        public int Id { get; set; }
        public string DNI { get; set; } = string.Empty;
        public string Nombre { get; set; } = string.Empty;
        public string ApellidoPaterno { get; set; } = string.Empty;

        public static List<Empleado> ObtenerEmpleadosEstaticos()
        {
            return new List<Empleado>
            {
                new Empleado { Id = 1, DNI = "70123456", Nombre = "Carlos", ApellidoPaterno = "Mendoza" },
                new Empleado { Id = 2, DNI = "60234567", Nombre = "Sofía", ApellidoPaterno = "Torres" },
                new Empleado { Id = 3, DNI = "50987654", Nombre = "Pedro", ApellidoPaterno = "Castillo" },
                new Empleado { Id = 4, DNI = "40876543", Nombre = "Ana", ApellidoPaterno = "Rojas" },
                new Empleado { Id = 5, DNI = "30765432", Nombre = "Vendedor", ApellidoPaterno = "Uno" },
                new Empleado { Id = 6, DNI = "20654321", Nombre = "Vendedor", ApellidoPaterno = "Dos" }
            };
        }
    }
}
