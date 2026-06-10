package pe.edu.pucp.softprog.bl.impl;

import pe.edu.pucp.softprog.bl.CustomerBL;
import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.dao.CustomerDAO;
import pe.edu.pucp.softprog.dao.impl.CustomerDAOImpl;
import pe.edu.pucp.softprog.dao.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerBLImpl implements CustomerBL {

    private CustomerDAO customerDAO = new CustomerDAOImpl();
    @Override
    public int totalNumberOfRecords(String firstName, String lastName, String docNumber) throws BusinessLogicException {
        try {
            return customerDAO.totalNumberOfRecords(firstName, lastName, docNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> fetchPage(String firstName, String lastName, String docNumber, int page, int recordsPerPage) throws BusinessLogicException {
        try {
            return customerDAO.fetchPage(firstName, lastName, docNumber, page, recordsPerPage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
