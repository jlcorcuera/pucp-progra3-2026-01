package pe.pucp.progra3.testsoft.app;

import pe.pucp.progra3.testsoft.bo.AlumnoBO;
import pe.pucp.progra3.testsoft.bo.ExamenBO;
import pe.pucp.progra3.testsoft.bo.PreguntaBO;
import pe.pucp.progra3.testsoft.bo.impl.AlumnoBOImpl;
import pe.pucp.progra3.testsoft.bo.impl.ExamenBOImpl;
import pe.pucp.progra3.testsoft.bo.impl.PreguntaBOImpl;
import pe.pucp.progra3.testsoft.model.Alumno;
import pe.pucp.progra3.testsoft.model.Pregunta;

import java.util.List;

public class CrearExamenes {

    public static void main(String[] args) {
        AlumnoBO alumnoBO = new AlumnoBOImpl();
        ExamenBO examenBO = new ExamenBOImpl();
        PreguntaBO preguntaBO = new PreguntaBOImpl();

        List<Alumno> alumnos = alumnoBO.leerTodos();
        System.out.println("Alummnos " + alumnos.size());
        for (Alumno alumno : alumnos) {
            List<Pregunta> preguntasSeleccionadas = preguntaBO.seleccionarPreguntasAleatorias();
            examenBO.crearExamenConPreguntas(
                    alumno,
                    "Examen configurado para " + alumno.getCodigo(),
                    preguntasSeleccionadas
            );
        }

        System.out.println("Se generaron los exámenes.");
    }
}
