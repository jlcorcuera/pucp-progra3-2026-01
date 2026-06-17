using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Text;

namespace CSharpRestClient
{
    public class AlumnoDTO
    {
        private String codigo;
        private String nombre;
        private String apellido;

        [JsonProperty("codigo")]
        public string Codigo { get => codigo; set => codigo = value; }
        [JsonProperty("nombre")]
        public string Nombre { get => nombre; set => nombre = value; }
        [JsonProperty("apellido")]
        public string Apellido { get => apellido; set => apellido = value; }
    }
}
