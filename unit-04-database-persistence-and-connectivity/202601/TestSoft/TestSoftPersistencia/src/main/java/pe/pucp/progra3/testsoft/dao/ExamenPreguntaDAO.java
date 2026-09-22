package pe.pucp.progra3.testsoft.dao;

import pe.pucp.progra3.testsoft.model.ExamenPregunta;

import java.sql.SQLException;
import java.util.List;

public interface ExamenPreguntaDAO {

    public void registrar(List<ExamenPregunta> examenesPregunta)
            throws SQLException;
}
