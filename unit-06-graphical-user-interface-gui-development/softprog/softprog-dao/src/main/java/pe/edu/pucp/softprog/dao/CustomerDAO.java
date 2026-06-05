package pe.edu.pucp.softprog.dao;

import pe.edu.pucp.softprog.dao.base.BaseDAO;
import pe.edu.pucp.softprog.dao.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends BaseDAO<Customer, Integer> {

    int totalNumberOfRecords(String firstName, String lastName, String docNumber) throws SQLException;
    List<Customer> fetchPage(String firstName, String lastName, String docNumber, int page, int recordsPerPage) throws SQLException;
}
