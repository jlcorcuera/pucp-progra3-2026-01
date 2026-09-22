package pe.pucp.progra3.testsoft.dao;

import pe.pucp.progra3.testsoft.model.Examen;

import java.sql.SQLException;

public interface ExamenDAO {

    public void registrar(Examen examen) throws SQLException;
}
