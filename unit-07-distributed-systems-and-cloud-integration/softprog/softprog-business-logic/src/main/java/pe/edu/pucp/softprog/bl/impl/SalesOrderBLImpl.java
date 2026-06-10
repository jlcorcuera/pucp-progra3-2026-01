package pe.edu.pucp.softprog.bl.impl;

import pe.edu.pucp.softprog.bl.SalesOrderBL;
import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.dao.CustomerDAO;
import pe.edu.pucp.softprog.dao.EmployeeDAO;
import pe.edu.pucp.softprog.dao.ProductDAO;
import pe.edu.pucp.softprog.dao.SalesOrderDAO;
import pe.edu.pucp.softprog.dao.impl.CustomerDAOImpl;
import pe.edu.pucp.softprog.dao.impl.EmployeeDAOImpl;
import pe.edu.pucp.softprog.dao.impl.ProductDAOImpl;
import pe.edu.pucp.softprog.dao.impl.SalesOrderDAOImpl;
import pe.edu.pucp.softprog.dao.model.*;
import pe.edu.pucp.softprog.dao.transaction.TransactionContext;

import java.util.List;

public class SalesOrderBLImpl implements SalesOrderBL {

    private ProductDAO productDAO = new ProductDAOImpl();
    private EmployeeDAO employeeDAO = new EmployeeDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();
    private SalesOrderDAO salesOrderDAO = new SalesOrderDAOImpl();

    @Override
    public SalesOrder create(SalesOrder salesOrder) throws BusinessLogicException {
        try {
            List<SalesOrderLine> lines = salesOrder.getSalesOrderLineList();
            // 1. validating available stock
            for(SalesOrderLine line: lines) {
                Product product = productDAO.load(line.getProduct().getId());
                if (product.getStock() < line.getQuantity()) {
                    throw new BusinessLogicException("No hay stock para el producto " + product.getName());
                }
            }
            // 2. verifying whether the employee exists and is active
            Employee employee = employeeDAO.load(salesOrder.getEmployee().getId());
            if (employee == null || !employee.getActive()) {
                throw new BusinessLogicException("El empleado no existe o no esta activo.");
            }
            // 3. verifying whether the customer exists and is active
            Customer customer = customerDAO.load(salesOrder.getCustomer().getId());
            if (customer == null) {
                throw new BusinessLogicException("El cliente no existe.");
            }
            // 4. creating sales order
            salesOrderDAO.save(salesOrder);
            // 5. updating products' stock
            for(SalesOrderLine line: lines) {
                Product product = line.getProduct();
                Integer newStock = product.getStock() - line.getQuantity();
                product.setStock(newStock);
                productDAO.update(product);
            }
            // 6. At this point, I can commit all these changes
            TransactionContext.commit();
        } catch (Exception ex) {
            // In case of errors, I have to rollback the changes
            TransactionContext.rollback();
            if (ex instanceof BusinessLogicException) {
                throw (BusinessLogicException)ex;
            } else {
                throw new BusinessLogicException(ex);
            }
        } finally {
            // Do not forget to close the current connection
            TransactionContext.close();
        }
        return salesOrder;
    }
}
