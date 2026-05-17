package pe.edu.pucp.softprog.test;

import pe.edu.pucp.softprog.dao.ProductDAO;
import pe.edu.pucp.softprog.dao.impl.ProductDAOImpl;
import pe.edu.pucp.softprog.dao.model.Product;

import java.sql.SQLException;

public class TestProductDAOMain {

    public static void main(String[] args) throws SQLException {
        ProductDAO productDAO = new ProductDAOImpl();
        Product product = productDAO.load(3);
        System.out.println(product.getName());

        Product newProduct = new Product();
        newProduct.setName("progra3 product");
        newProduct.setUnitMeasurement("nota");
        newProduct.setPrice(20.0);
        newProduct.setActive(true);
        productDAO.save(newProduct);
    }
}
