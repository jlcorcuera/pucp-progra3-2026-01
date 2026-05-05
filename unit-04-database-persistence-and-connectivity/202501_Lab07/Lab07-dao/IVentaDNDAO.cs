using Lab07_domain.dto;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_dao
{
    public interface IVentaDNDAO
    {
        public List<VentaDNDTO> listAll();
    }
}
