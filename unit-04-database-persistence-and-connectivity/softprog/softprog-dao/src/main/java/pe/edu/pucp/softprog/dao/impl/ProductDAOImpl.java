package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.ProductDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public Product load(Integer productId) {
        String sql = "select id_producto, nombre, unidad_medida, precio, activo from producto where id_producto = ?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, productId);
                try(ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        Product product = new Product();
                        product.setId(rs.getInt(1));
                        product.setName(rs.getString(2));
                        product.setUnitMeasurement(rs.getString(3));
                        product.setPrice(rs.getDouble(4));
                        product.setActive(rs.getBoolean(5));
                        return product;
                    }
                    return null;
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product save(Product product) {
        String sql = "insert producto (nombre, unidad_medida, precio, activo) values (?, ?, ?, ?)";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, product.getName());
            pstm.setString(2, product.getUnitMeasurement());
            pstm.setDouble(3, product.getPrice());
            pstm.setBoolean(4, product.getActive());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public void remove(Product product) {

    }
}
