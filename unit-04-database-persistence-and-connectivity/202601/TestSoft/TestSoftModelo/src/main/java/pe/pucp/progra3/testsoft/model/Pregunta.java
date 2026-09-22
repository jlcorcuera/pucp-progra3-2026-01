package pe.pucp.progra3.testsoft.model;

public class Pregunta {
    private Integer id;
    private String enunciado;

    public Pregunta(Integer id, String enunciado) {
        this.id = id;
        this.enunciado = enunciado;
    }

    public Pregunta() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }
}
