package pe.pucp.progra3.testsoft.bo.impl;

import pe.pucp.progra3.testsoft.bo.PreguntaBO;
import pe.pucp.progra3.testsoft.dao.PreguntaDAO;
import pe.pucp.progra3.testsoft.dao.impl.PreguntaDAOImpl;
import pe.pucp.progra3.testsoft.model.Pregunta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreguntaBOImpl implements PreguntaBO {

    private PreguntaDAO preguntaDAO = new PreguntaDAOImpl();

    @Override
    public List<Pregunta> seleccionarPreguntasAleatorias() {
        List<Pregunta> copia = null;
        try {
            copia = new ArrayList<>(preguntaDAO.listar());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Collections.shuffle(copia);
        return new ArrayList<>(copia.subList(0, 10));
    }
}
