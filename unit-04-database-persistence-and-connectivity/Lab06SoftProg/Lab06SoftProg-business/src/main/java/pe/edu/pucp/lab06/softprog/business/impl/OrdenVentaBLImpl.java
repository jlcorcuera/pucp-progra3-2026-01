package pe.edu.pucp.lab06.softprog.business.impl;

import pe.edu.pucp.lab06.softprog.business.OrdenVentaBL;
import pe.edu.pucp.lab06.softprog.dao.OrdenVentaDAO;
import pe.edu.pucp.lab06.softprog.dao.ProductoDAO;
import pe.edu.pucp.lab06.softprog.dao.impl.OrdenVentaDAOImpl;
import pe.edu.pucp.lab06.softprog.dao.impl.ProductoDAOImpl;
import pe.edu.pucp.lab06.softprog.dbmanager.TransactionContext;
import pe.edu.pucp.lab06.softprog.model.LineaOrdenVenta;
import pe.edu.pucp.lab06.softprog.model.OrdenVenta;
import pe.edu.pucp.lab06.softprog.model.Producto;

public class OrdenVentaBLImpl implements OrdenVentaBL {

    private ProductoDAO productoDAO = new ProductoDAOImpl();
    private OrdenVentaDAO ordenVentaDAO = new OrdenVentaDAOImpl();

    @Override
    public OrdenVenta crearOrden(OrdenVenta ordenVenta) throws Exception {
        try {
            for(LineaOrdenVenta linea: ordenVenta.getDetalles()) {
                Producto freshProduct = productoDAO.load(linea.getProducto().getId());
                if (freshProduct.getStock() < linea.getCantidad()) {
                    throw new Exception("Error no hay stock");
                }
            }
            ordenVentaDAO.save(ordenVenta);
            for(LineaOrdenVenta linea: ordenVenta.getDetalles()) {
                Producto freshProduct = productoDAO.load(linea.getProducto().getId());
                freshProduct.setStock(freshProduct.getStock() - linea.getCantidad());
                productoDAO.update(freshProduct);
            }
            TransactionContext.commit();
        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Ocurrio un error", ex);
        } finally {
            TransactionContext.close();
        }
        return ordenVenta;
    }
}
