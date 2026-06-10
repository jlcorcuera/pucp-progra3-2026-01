package pe.pucp.progra3.rs;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import pe.pucp.progra3.rs.dto.SaludoDTO;

@Path("hello")
public class HelloWorldRest {

    @GET
    public String saludar() {
        return "Hello from my first rest service";
    }

    @GET
    @Path("hello")
    @Produces("application/json")
    public SaludoDTO saludar2() {
        SaludoDTO saludoDTO = new SaludoDTO();
        saludoDTO.setMessage("Hello from JSON object");
        return saludoDTO;
    }

}
