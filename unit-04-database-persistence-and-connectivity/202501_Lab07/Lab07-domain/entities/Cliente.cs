using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_domain.entities
{
    public class Cliente
    {
        /*
         id_cliente	int
nombre_cliente	varchar(50)
apellido_cliente	varchar(50)
email_cliente	varchar(75)
         */
        private int id;
        private String nombreCliente;
        private String apellidoCliente;
        private String emailCliente;

        public int Id { get => id; set => id = value; }
        public string NombreCliente { get => nombreCliente; set => nombreCliente = value; }
        public string ApellidoCliente { get => apellidoCliente; set => apellidoCliente = value; }
        public string EmailCliente { get => emailCliente; set => emailCliente = value; }
    }
}
