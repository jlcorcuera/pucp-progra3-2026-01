using System;

namespace SoftProg_WebApp.Models
{
    public class LineaOrdenVenta
    {
        public int NumeroLinea { get; set; }
        public Producto? Producto { get; set; }
        public int Cantidad { get; set; } = 1;
        public decimal PrecioUnitario { get; set; }

        public decimal Subtotal => Cantidad * PrecioUnitario;
    }
}
