using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_domain.entities
{
    public class Pelicula
    {
        /*
         id_pelicula	int
nombre_pelicula	varchar(150)
genero_pelicula	enum('ACCION','ANIMACION','DRAMA','CIENCIA FICCION')
         */
        private int id;
        private String nombrePelicula;
        private String generoPelicula;

        public int Id { get => id; set => id = value; }
        public string NombrePelicula { get => nombrePelicula; set => nombrePelicula = value; }
        public string GeneroPelicula { get => generoPelicula; set => generoPelicula = value; }
    }
}
