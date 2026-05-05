using Lab07_domain.entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao
{
    public interface IPeliculaDAO
    {
        Pelicula findByNombre(string nombre);
        Pelicula registrar(Pelicula pelicula);
    }
}
