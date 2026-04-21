package pe.edu.pucp.softprog.bl;

import pe.edu.pucp.softprog.bl.exception.BusinessLogicException;
import pe.edu.pucp.softprog.dao.model.SalesOrder;

public interface SalesOrderBL {

    public SalesOrder create(SalesOrder salesOrder) throws BusinessLogicException;

}
