package pe.pucp.progra3.testsoft.dao;

import pe.pucp.progra3.testsoft.model.Alumno;

import java.sql.SQLException;
import java.util.List;

public interface AlumnoDAO {

    public List<Alumno> leerTodos() throws SQLException;
}
