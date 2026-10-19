using System;
using System.Collections.Generic;

namespace SoftProg_WebApp.Models
{
    public class OrdenVenta
    {
        public int Id { get; set; }
        
        public Cliente? Cliente { get; set; }
        public Empleado? Empleado { get; set; }

        public DateTime FechaHora { get; set; }
        public decimal MontoTotal { get; set; }
        public List<LineaOrdenVenta> Lineas { get; set; } = new();

        private static List<OrdenVenta> _ordenes;

        public static List<OrdenVenta> ObtenerDatosEstaticos()
        {
            if (_ordenes == null)
            {
                _ordenes = new List<OrdenVenta>();
                var random = new Random(42); // Seed para que sea predecible en las pruebas
                
                var listClientes = Cliente.ObtenerClientesEstaticos();
                var listEmpleados = Empleado.ObtenerEmpleadosEstaticos();

                for (int i = 1; i <= 50; i++)
                {
                    var selectedCliente = listClientes[random.Next(listClientes.Count)];
                    var selectedEmpleado = listEmpleados[random.Next(listEmpleados.Count)];

                    _ordenes.Add(new OrdenVenta
                    {
                        Id = 1000 + i,
                        Cliente = selectedCliente,
                        Empleado = selectedEmpleado,
                        // Restamos días aleatorios para tener fechas en el último año
                        FechaHora = DateTime.Now.AddDays(-random.Next(1, 365)).AddHours(random.Next(8, 18)),
                        // Monto entre 100 y 5100
                        MontoTotal = (decimal)(random.NextDouble() * 5000) + 100
                    });
                }

                // Ordenar por fecha descendente por defecto
                _ordenes.Sort((x, y) => y.FechaHora.CompareTo(x.FechaHora));
            }

            return _ordenes;
        }

        public static void AgregarOrden(OrdenVenta nuevaOrden)
        {
            if (_ordenes == null) ObtenerDatosEstaticos();

            int maxId = 1000;
            foreach (var o in _ordenes)
            {
                if (o.Id > maxId) maxId = o.Id;
            }
            nuevaOrden.Id = maxId + 1;

            _ordenes.Insert(0, nuevaOrden); // Insertamos al principio para que se vea primero
        }
    }
}
