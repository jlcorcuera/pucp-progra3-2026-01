package pe.pucp.progra3.testsoft.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Examen {
    private Integer id;
    private Alumno alumno;
    private String titulo;
    private Date fechaCreacion;

    private List<ExamenPregunta> examenPreguntas;

    public List<Pregunta> getPreguntas() {
        List<Pregunta> preguntas = new ArrayList<>();
        for(ExamenPregunta examenPregunta: examenPreguntas){
            preguntas.add(examenPregunta.getPregunta());
        }
        return preguntas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
