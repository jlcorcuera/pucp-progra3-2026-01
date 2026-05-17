package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.ProductDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Product;
import pe.edu.pucp.softprog.dao.transaction.TransactionContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public Product load(Integer productId) throws SQLException {
        String sql = "select id_producto, nombre, unidad_medida, precio, activo, stock from producto where id_producto = ?";
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
                        product.setStock(rs.getInt(6));
                        return product;
                    }
                    return null;
                }
        }
    }

    @Override
    public Product save(Product product) throws SQLException {
        String sql = "insert producto (nombre, unidad_medida, precio, activo) values (?, ?, ?, ?)";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, product.getName());
            pstm.setString(2, product.getUnitMeasurement());
            pstm.setDouble(3, product.getPrice());
            pstm.setBoolean(4, product.getActive());
            pstm.executeUpdate();
        }
        return product;
    }

    @Override
    public Product update(Product product) throws SQLException {
        String sql = "update producto set nombre = ?, unidad_medida = ?, precio = ?, stock = ?, activo = ? where id_producto = ?";
        Connection connection = TransactionContext.getConnection();
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, product.getName());
            pstm.setString(2, product.getUnitMeasurement());
            pstm.setDouble(3, product.getPrice());
            pstm.setInt(4, product.getStock());
            pstm.setBoolean(5, product.getActive());
            pstm.setInt(6, product.getId());
            pstm.executeUpdate();
        }
        return product;
    }

    @Override
    public void remove(Product product) throws SQLException {

    }
}
