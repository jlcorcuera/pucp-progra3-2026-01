package pe.edu.pucp.softprog.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesOrder {

    private Integer id;
    private Employee employee;
    private Customer customer;
    private Double total;
    private Date date;
    private Boolean active;

    private List<SalesOrderLine> salesOrderLineList;

    public SalesOrder() {
        this.salesOrderLineList = new ArrayList<>();
    }

    public void addLine(SalesOrderLine line) {
        this.salesOrderLineList.add(line);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<SalesOrderLine> getSalesOrderLineList() {
        return salesOrderLineList;
    }

    public void setSalesOrderLineList(List<SalesOrderLine> salesOrderLineList) {
        this.salesOrderLineList = salesOrderLineList;
    }
}
