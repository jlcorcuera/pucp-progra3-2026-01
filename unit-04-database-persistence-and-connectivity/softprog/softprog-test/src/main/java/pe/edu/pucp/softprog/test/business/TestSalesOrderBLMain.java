package pe.edu.pucp.softprog.test.business;

import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.bl.impl.SalesOrderBLImpl;
import pe.edu.pucp.softprog.dao.impl.CustomerDAOImpl;
import pe.edu.pucp.softprog.dao.impl.EmployeeDAOImpl;
import pe.edu.pucp.softprog.dao.impl.ProductDAOImpl;
import pe.edu.pucp.softprog.dao.model.*;

import java.sql.SQLException;
import java.util.Date;

public class TestSalesOrderBLMain {

    public static void main(String[] args) throws SQLException {
        Employee employee = new EmployeeDAOImpl().load(4);
        System.out.println("Vendedor " + employee.getLastName() + " " + employee.getName());
        Customer customer = new CustomerDAOImpl().load(8);
        System.out.println("Cliente " + customer.getName() + " " + customer.getLastName());

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomer(customer);
        salesOrder.setEmployee(employee);
        salesOrder.setDate(new Date());
        salesOrder.setActive(true);
        Double total = 0.0;
        // let's add two products to my sales order
        SalesOrderLine line1 = new SalesOrderLine();
        Product product1 = new ProductDAOImpl().load(1);
        line1.setActive(true);
        line1.setSalesOrder(salesOrder);
        line1.setProduct(product1);
        line1.setQuantity(1);
        line1.setSubtotal(line1.getQuantity() * product1.getPrice());
        total = total + line1.getSubtotal();

        SalesOrderLine line2 = new SalesOrderLine();
        Product product2 = new ProductDAOImpl().load(2);
        line2.setActive(true);
        line2.setSalesOrder(salesOrder);
        line2.setProduct(product2);
        line2.setQuantity(2);
        line2.setSubtotal(line2.getQuantity() * product2.getPrice());
        total = total + line2.getSubtotal();

        salesOrder.addLine(line1);
        salesOrder.addLine(line2);
        salesOrder.setTotal(total);
        try {
            new SalesOrderBLImpl().create(salesOrder);
            System.out.println("Orden creada " + salesOrder.getId());
        } catch (BusinessLogicException e) {
            e.printStackTrace();
        }
    }
}
