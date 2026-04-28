package pe.edu.pucp.lab06.softprog.dao.impl;

import pe.edu.pucp.lab06.softprog.dao.ProductoDAO;
import pe.edu.pucp.lab06.softprog.dbmanager.TransactionContext;
import pe.edu.pucp.lab06.softprog.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAOImpl implements ProductoDAO {
    @Override
    public Producto save(Producto producto) {
        return null;
    }

    @Override
    public void remove(Producto producto) {

    }

    @Override
    public Producto load(Integer id) throws Exception {
        String sql = """
                select id_producto, nombre, unidad_medida, precio, activo, stock 
                from producto where id_producto = ?
                """;
        // NO TENEMOS QUE PONER LA CONNECTION DENTRO DEL TRY-WITH-RESOURCES
        // DE LO CONTRARIO LA CONEXION SE CERRARA
        Connection connection = TransactionContext.getConnection();
        try(
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id_producto"));
                    producto.setNombre(rs.getString(2));
                    producto.setUnidadMedida(rs.getString(3));
                    producto.setPrecio(rs.getDouble(4));
                    producto.setActivo(rs.getBoolean(5));
                    producto.setStock(rs.getLong(6));
                    return producto;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Producto update(Producto producto) throws Exception {
        String sql = """
                update producto set nombre = ?, 
                                    unidad_medida = ?, 
                                    precio = ?, 
                                    activo = ?, 
                                    stock = ? 
                where id_producto = ?
                """;
        // NO TENEMOS QUE PONER LA CONNECTION DENTRO DEL TRY-WITH-RESOURCES
        // DE LO CONTRARIO LA CONEXION SE CERRARA
        Connection connection = TransactionContext.getConnection();
        try(
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getUnidadMedida());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setBoolean(4, producto.getActivo());
            stmt.setLong(5, producto.getStock());
            stmt.setInt(6, producto.getId());
            stmt.executeUpdate();
            return producto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
