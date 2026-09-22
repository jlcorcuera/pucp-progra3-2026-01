package pe.pucp.progra3.testsoft.bo.impl;

import pe.pucp.progra3.testsoft.bo.AlumnoBO;
import pe.pucp.progra3.testsoft.dao.AlumnoDAO;
import pe.pucp.progra3.testsoft.dao.impl.AlumnoDAOImpl;
import pe.pucp.progra3.testsoft.model.Alumno;

import java.sql.SQLException;
import java.util.List;

public class AlumnoBOImpl implements AlumnoBO {

    private AlumnoDAO alumnoDAO = new AlumnoDAOImpl();

    public List<Alumno> leerTodos() {
        try {
            return alumnoDAO.leerTodos();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
