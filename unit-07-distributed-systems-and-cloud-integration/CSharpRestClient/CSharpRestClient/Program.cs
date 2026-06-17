using System;
using System.IO;
using System.Net;
using System.ComponentModel;
using Newtonsoft.Json;
using CSharpRestClient;

class Program
{
    private const string BASE_URL = "http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/alumno/search?first_name=Jose&last_name=Corcuera";

    static void Main(string[] args)
    {
        List<AlumnoDTO> results = new HttpClientUtils<List<AlumnoDTO>>().get(BASE_URL);
        foreach (AlumnoDTO alumno in results)
        {
            Console.WriteLine(alumno.Codigo + ". " + alumno.Nombre);
        }

        String urlCrearAlumno = "http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/alumno";
        AlumnoDTO nuevoAlumno = new AlumnoDTO();
        nuevoAlumno.Nombre = "Nombre Java";
        nuevoAlumno.Apellido = "Apellido Java";
        AlumnoDTO response = new HttpClientUtils<AlumnoDTO>().post(urlCrearAlumno, nuevoAlumno);
        Console.WriteLine(response.Codigo);
    }
}