using System;
using System.Collections.Generic;

namespace SoftProg_WebApp.Models
{
    public class Producto
    {
        public int Id { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public decimal PrecioUnitario { get; set; }

        private static List<Producto>? _productos;

        public static List<Producto> ObtenerProductosEstaticos()
        {
            if (_productos == null)
            {
                _productos = new List<Producto>
                {
                    new Producto { Id = 101, Nombre = "Laptop HP Pavilion 15.6\"", PrecioUnitario = 799.99m },
                    new Producto { Id = 102, Nombre = "Mouse Inalámbrico Logitech M170", PrecioUnitario = 14.99m },
                    new Producto { Id = 103, Nombre = "Teclado Mecánico RGB Redragon", PrecioUnitario = 49.99m },
                    new Producto { Id = 104, Nombre = "Monitor IPS Dell 24\" FHD", PrecioUnitario = 129.99m },
                    new Producto { Id = 105, Nombre = "Auriculares Gamer HyperX Cloud", PrecioUnitario = 59.99m },
                    new Producto { Id = 106, Nombre = "Memoria USB 3.2 Kingston 128GB", PrecioUnitario = 18.50m },
                    new Producto { Id = 107, Nombre = "Unidad SSD Interna Samsung 1TB", PrecioUnitario = 99.00m },
                    new Producto { Id = 108, Nombre = "Cámara Web Full HD Logitech C920", PrecioUnitario = 74.99m },
                    new Producto { Id = 109, Nombre = "Mochila Impermeable para Laptop 15\"", PrecioUnitario = 29.99m },
                    new Producto { Id = 110, Nombre = "Hub USB-C 7 en 1 con HDMI y SD", PrecioUnitario = 34.99m },
                    new Producto { Id = 111, Nombre = "Pad Mouse Gamer Extendido 90x40cm", PrecioUnitario = 12.00m },
                    new Producto { Id = 112, Nombre = "Cable HDMI 2.1 Ultra Alta Velocidad", PrecioUnitario = 9.99m }
                };
            }
            return _productos;
        }
    }
}
