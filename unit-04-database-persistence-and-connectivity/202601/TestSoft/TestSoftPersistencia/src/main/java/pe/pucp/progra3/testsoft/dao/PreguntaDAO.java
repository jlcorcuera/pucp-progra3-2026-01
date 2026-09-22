package pe.pucp.progra3.testsoft.dao;

import pe.pucp.progra3.testsoft.model.Pregunta;

import java.sql.SQLException;
import java.util.List;

public interface PreguntaDAO {

    public List<Pregunta> listar() throws SQLException;
}
