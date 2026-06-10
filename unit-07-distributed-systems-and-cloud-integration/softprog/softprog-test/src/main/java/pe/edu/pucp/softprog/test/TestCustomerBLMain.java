package pe.edu.pucp.softprog.test;

import pe.edu.pucp.softprog.bl.CustomerBL;
import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.bl.impl.CustomerBLImpl;
import pe.edu.pucp.softprog.dao.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class TestCustomerBLMain {

    public static void main(String[] args) throws BusinessLogicException {
        CustomerBL customerBL = new CustomerBLImpl();
        String firstName = "";
        String lastName = "";
        String docNumber = "";
        int currentPage = 2;
        int pageSize = 3;
        int totalNumberOfRecords = customerBL.totalNumberOfRecords(firstName, lastName, docNumber);
        System.out.println("totalNumberOfRecords " + totalNumberOfRecords);
        List<Customer> customers =  customerBL.fetchPage(firstName, lastName, docNumber, currentPage, pageSize);
        for(Customer customer: customers) {
            System.out.println(customer);
        }
    }
}
