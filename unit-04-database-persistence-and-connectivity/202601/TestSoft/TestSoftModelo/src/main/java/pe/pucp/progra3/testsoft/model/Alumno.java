package pe.pucp.progra3.testsoft.model;

public class Alumno {

    private Integer id;
    private String codigo;
    private String nombre;
    private String correo;

    public Alumno(Integer id, String codigo, String nombre, String correo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Alumno() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
