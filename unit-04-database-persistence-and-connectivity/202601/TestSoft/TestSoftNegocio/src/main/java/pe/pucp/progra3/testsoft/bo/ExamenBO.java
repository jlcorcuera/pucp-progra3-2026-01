package pe.pucp.progra3.testsoft.bo;

import pe.pucp.progra3.testsoft.model.Alumno;
import pe.pucp.progra3.testsoft.model.Pregunta;

import java.util.List;

public interface ExamenBO {

    public void crearExamenConPreguntas(Alumno alumno, String titulo, List<Pregunta> seleccionadas);
}
