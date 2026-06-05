package pe.edu.pucp.softprog.bl;

import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.dao.model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBL {
    int totalNumberOfRecords(String firstName, String lastName, String docNumber) throws BusinessLogicException;
    List<Customer> fetchPage(String firstName, String lastName, String docNumber, int page, int recordsPerPage) throws BusinessLogicException;
}
