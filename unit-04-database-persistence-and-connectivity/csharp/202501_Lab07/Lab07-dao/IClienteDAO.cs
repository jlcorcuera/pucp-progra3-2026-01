using Lab07_domain.entities;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao
{
    public interface IClienteDAO
    {
        Cliente findByEmail(string email);
        Cliente registrar(Cliente cliente);
    }
}
