package pe.edu.pucp.lab06.softprog.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdenVenta {

    private Integer id;
    private Empleado empleado;
    private Cliente cliente;
    private Double total;
    private Date fechaHora;
    private Boolean activa;

    private List<LineaOrdenVenta> detalles = new ArrayList<LineaOrdenVenta>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public List<LineaOrdenVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<LineaOrdenVenta> detalles) {
        this.detalles = detalles;
    }
}
