package pe.edu.pucp.lab06.softprog.dao.impl;

import pe.edu.pucp.lab06.softprog.dao.OrdenVentaDAO;
import pe.edu.pucp.lab06.softprog.dbmanager.TransactionContext;
import pe.edu.pucp.lab06.softprog.model.LineaOrdenVenta;
import pe.edu.pucp.lab06.softprog.model.OrdenVenta;

import java.sql.*;

public class OrdenVentaDAOImpl implements OrdenVentaDAO {
    @Override
    public OrdenVenta save(OrdenVenta ordenVenta) throws Exception {
        String sql = """
                insert into orden_venta(fid_empleado, fid_cliente, total, fecha_hora, activa)
                values (?, ?, ?, ?, ?);
        """;
        // NO TENEMOS QUE PONER LA CONNECTION DENTRO DEL TRY-WITH-RESOURCES
        // DE LO CONTRARIO LA CONEXION SE CERRARA
        Connection connection = TransactionContext.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, ordenVenta.getEmpleado().getId());
            pstmt.setInt(2, ordenVenta.getCliente().getId());
            pstmt.setDouble(3, ordenVenta.getTotal());
            pstmt.setDate(4, new java.sql.Date(ordenVenta.getFechaHora().getTime()));
            pstmt.setBoolean(5, ordenVenta.getActiva());
            pstmt.executeUpdate();
            Integer id = null;
            try(ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = pstmt.getGeneratedKeys().getInt(1);
                    ordenVenta.setId(id);
                }
            }
            String sqlLinea = """
                    insert into linea_orden_venta(fid_orden_venta, fid_producto, cantidad, subtotal, activa)
                    values (?, ?, ?, ?)
                    """;
            try(PreparedStatement pstmtLinea = connection.prepareStatement(sqlLinea)) {
                for(LineaOrdenVenta linea: ordenVenta.getDetalles()) {
                    pstmtLinea.setInt(1, ordenVenta.getId());
                    pstmtLinea.setInt(2, linea.getProducto().getId());
                    pstmtLinea.setLong(3, linea.getCantidad());
                    pstmtLinea.setDouble(4, linea.getSubtotal());
                    pstmtLinea.setBoolean(5, linea.getActiva());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void remove(OrdenVenta ordenVenta) {

    }

    @Override
    public OrdenVenta load(Integer integer) {
        return null;
    }

    @Override
    public OrdenVenta update(OrdenVenta ordenVenta) {
        return null;
    }
}
