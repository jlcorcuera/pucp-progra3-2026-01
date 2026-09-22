package pe.pucp.progra3.testsoft.bo.impl;

import pe.pucp.progra3.testsoft.bo.ExamenBO;
import pe.pucp.progra3.testsoft.dao.ExamenDAO;
import pe.pucp.progra3.testsoft.dao.ExamenPreguntaDAO;
import pe.pucp.progra3.testsoft.dao.impl.ExamenDAOImpl;
import pe.pucp.progra3.testsoft.dao.impl.ExamenPreguntaDAOImpl;
import pe.pucp.progra3.testsoft.dbmanager.TransactionContext;
import pe.pucp.progra3.testsoft.model.Alumno;
import pe.pucp.progra3.testsoft.model.Examen;
import pe.pucp.progra3.testsoft.model.ExamenPregunta;
import pe.pucp.progra3.testsoft.model.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class ExamenBOImpl implements ExamenBO {

    private ExamenDAO examenDAO = new ExamenDAOImpl();
    private ExamenPreguntaDAO examenPreguntaDAO = new ExamenPreguntaDAOImpl();

    @Override
    public void crearExamenConPreguntas(Alumno alumno, String titulo, List<Pregunta> seleccionadas) {
        try {
            Examen examen = new Examen();
            examen.setAlumno(alumno);
            examen.setTitulo(titulo);
            examenDAO.registrar(examen);

            List<ExamenPregunta> examenPreguntas = new ArrayList<>();
            int i = 1;
            for(Pregunta pregunta: seleccionadas) {
                ExamenPregunta examenPregunta = new ExamenPregunta();
                examenPregunta.setExamen(examen);
                examenPregunta.setPregunta(pregunta);
                examenPregunta.setOrden(i);
                i++;
                examenPreguntas.add(examenPregunta);
            }
            examenPreguntaDAO.registrar(examenPreguntas);

            TransactionContext.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            TransactionContext.rollback();
        } finally {
            TransactionContext.close();
        }

    }
}
