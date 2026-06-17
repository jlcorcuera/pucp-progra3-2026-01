package pe.pucp.progra3.rs.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pe.pucp.progra3.rs.client.utils.HttpClientUtils;
import pe.pucp.progra3.softprog.rs.dto.AlumnoDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TestRestClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/alumno/search?first_name=Jose&last_name=Corcuera";


        List<AlumnoDTO> resultado = new HttpClientUtils<List<AlumnoDTO>>().get(url, new TypeReference<List<AlumnoDTO>>() {});
        for(AlumnoDTO alumno: resultado) {
            System.out.println(alumno.getCodigo() + " " + alumno.getNombre());
        }

        url = "http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/alumno";

        AlumnoDTO nuevoAlumno = new AlumnoDTO();
        nuevoAlumno.setNombre("Alumno java");
        nuevoAlumno.setApellido("Apellido java");

        AlumnoDTO alumnoRespuesta = new HttpClientUtils<AlumnoDTO>().post(url, nuevoAlumno, new TypeReference<AlumnoDTO>() {});
        System.out.println(alumnoRespuesta.getCodigo());

    }
}
