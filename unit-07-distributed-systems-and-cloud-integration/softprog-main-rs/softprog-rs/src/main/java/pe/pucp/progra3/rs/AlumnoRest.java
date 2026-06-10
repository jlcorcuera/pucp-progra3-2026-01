package pe.pucp.progra3.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.rs.dto.AlumnoDTO;

import java.util.ArrayList;
import java.util.List;

@Path("/alumno")
@Consumes("application/json")
@Produces("application/json")
public class AlumnoRest {

    @POST
    public AlumnoDTO registrar(AlumnoDTO alumno) {
        alumno.setCodigo("NUEVO CODIGO");
        return alumno;
    }

    @Path("{id}")
    @GET
    public AlumnoDTO obtenerPorId(@PathParam("id") Integer id) {
        AlumnoDTO alumno = new AlumnoDTO();
        alumno.setCodigo(id + "");
        alumno.setNombre("Jose");
        return alumno;
    }

    @Path("{id_alumno}/apoderado/{id_apoderado}")
    @GET
    public AlumnoDTO obtenerApoderado(@PathParam("id_alumno") Integer idAlumno,
                                      @PathParam("id_apoderado") Integer idApoderado) {
        AlumnoDTO alumno = new AlumnoDTO();
        alumno.setCodigo(idAlumno + "");
        alumno.setNombre("Jose");
        return alumno;
    }

    @Path("delete/{id}")
    @DELETE
    public Response eliminarAlumno(@PathParam("id") Integer id) {
        // LOGICA PARA ELIMINAR AL ALUMNO
        return Response.ok().build();
    }

    @Path("search")
    @GET
    public List<AlumnoDTO> listar(@QueryParam("first_name") String firstName,
                                  @QueryParam("last_name") String lastName) {
        List<AlumnoDTO> myList = new ArrayList<>();
        int n = 10;
        for(int i = 1; i <= n; i++) {
            AlumnoDTO alumnoDTO = new AlumnoDTO();
            alumnoDTO.setCodigo(i + "");
            alumnoDTO.setNombre("Nombre " + i);
            myList.add(alumnoDTO);
        }
        return myList;
    }
}
