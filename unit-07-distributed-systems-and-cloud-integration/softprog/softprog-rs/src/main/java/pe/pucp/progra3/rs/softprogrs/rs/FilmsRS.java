package pe.pucp.progra3.rs.softprogrs.rs;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Path("films")
public class FilmsRS {

    @GET
    @Produces("application/json")
    public List<String> films() {
        List<String> films = new ArrayList<>();
        films.add("Batman");
        films.add("Crepusculo");
        return films;
    }
}
