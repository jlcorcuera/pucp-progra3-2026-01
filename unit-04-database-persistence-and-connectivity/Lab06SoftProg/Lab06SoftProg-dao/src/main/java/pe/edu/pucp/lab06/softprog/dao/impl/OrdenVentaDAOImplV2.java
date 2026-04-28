package pe.edu.pucp.lab06.softprog.dao.impl;

import pe.edu.pucp.lab06.softprog.dao.OrdenVentaDAO;
import pe.edu.pucp.lab06.softprog.dbmanager.TransactionContext;
import pe.edu.pucp.lab06.softprog.model.LineaOrdenVenta;
import pe.edu.pucp.lab06.softprog.model.OrdenVenta;

import java.sql.*;

public class OrdenVentaDAOImplV2 implements OrdenVentaDAO {
    @Override
<<<<<<< HEAD
    public OrdenVenta save(OrdenVenta ordenVenta) throws Exception {
        String sql = """
                {call INSERTAR_ORDEN_VENTA(?, ?, ?, ?)}
        """;
        // NO TENEMOS QUE PONER LA CONNECTION DENTRO DEL TRY-WITH-RESOURCES
        // DE LO CONTRARIO LA CONEXION SE CERRARA
        Connection connection = TransactionContext.getConnection();
        try(
            CallableStatement cstmt = connection.prepareCall(sql)) {
=======
    public OrdenVenta save(OrdenVenta ordenVenta) {
        String sql = """
                {call INSERTAR_ORDEN_VENTA(?, ?, ?, ?)}
        """;
        try(Connection connection = TransactionContext.getConnection();
            CallableStatement cstmt = connection.prepareCall(sql)) {

>>>>>>> 4015b73e8b8686efecc5246e9bc696a45b4c1a97
            cstmt.registerOutParameter(1, Types.INTEGER);

            cstmt.setInt(2, ordenVenta.getEmpleado().getId());
            cstmt.setInt(3, ordenVenta.getCliente().getId());
            cstmt.setDouble(4, ordenVenta.getTotal());

            cstmt.execute();
            Integer id = cstmt.getInt(1);
            ordenVenta.setId(id);

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
                    pstmtLinea.addBatch();
                }
                pstmtLinea.executeBatch();
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
