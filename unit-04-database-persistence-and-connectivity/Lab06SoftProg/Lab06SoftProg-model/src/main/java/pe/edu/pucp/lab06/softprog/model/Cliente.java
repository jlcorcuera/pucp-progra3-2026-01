package pe.edu.pucp.lab06.softprog.model;

public class Cliente {
    private Integer id;
    private Double lineaCredito;
    private String categoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLineaCredito() {
        return lineaCredito;
    }

    public void setLineaCredito(Double lineaCredito) {
        this.lineaCredito = lineaCredito;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
