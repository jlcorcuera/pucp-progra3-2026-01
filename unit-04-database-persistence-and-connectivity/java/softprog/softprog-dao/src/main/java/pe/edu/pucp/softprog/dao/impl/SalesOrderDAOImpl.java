package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.SalesOrderDAO;
import pe.edu.pucp.softprog.dao.model.SalesOrder;
import pe.edu.pucp.softprog.dao.model.SalesOrderLine;
import pe.edu.pucp.softprog.dao.transaction.TransactionContext;

import java.sql.*;

public class SalesOrderDAOImpl implements SalesOrderDAO {
    @Override
    public SalesOrder load(Integer integer) throws SQLException {
        return null;
    }

    @Override
    public SalesOrder save(SalesOrder salesOrder) throws SQLException {
        String sql = """
            insert into orden_venta(fid_empleado, fid_cliente, total, fecha_hora, activa)
            values (?, ?, ?, ?, ?)
        """;
        Connection conn = TransactionContext.getConnection();
        // 1. creating the sales order and get its ID
        try (PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, salesOrder.getEmployee().getId());
            pstm.setInt(2, salesOrder.getCustomer().getId());
            pstm.setDouble(3, salesOrder.getTotal());
            pstm.setDate(4, new java.sql.Date(salesOrder.getDate().getTime()));
            pstm.setBoolean(5, salesOrder.getActive());
            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        salesOrder.setId(newId);
                    }
                }
            }
        }
        // 2. adding sales order lines to the previously created sales order
        sql = """
            insert into linea_orden_venta(fid_orden_venta, fid_producto, cantidad, subtotal, activa)
            values (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            for(SalesOrderLine salesOrderLine: salesOrder.getSalesOrderLineList()) {
                pstm.setInt(1, salesOrder.getId());
                pstm.setInt(2, salesOrderLine.getProduct().getId());
                pstm.setDouble(3, salesOrderLine.getQuantity());
                pstm.setDouble(4, salesOrderLine.getSubtotal());
                pstm.setBoolean(5, salesOrderLine.getActive());
                pstm.addBatch();
            }
            pstm.executeBatch();
        }
        return salesOrder;
    }

    @Override
    public SalesOrder update(SalesOrder salesOrder) {
        return null;
    }

    @Override
    public void remove(SalesOrder salesOrder) {

    }
}
