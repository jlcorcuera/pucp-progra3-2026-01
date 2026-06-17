package pe.pucp.progra3.jasper.dto;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import java.io.InputStream;
import java.util.*;



public class Main {

    public static void main(String args[]) throws JRException {
        List<ProductoDTO> productos = new ArrayList<>();
        int N = 1000;
        for(int i = 1; i <= N; i++) {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(i);
            producto.setNombre("Producto " + i);
            producto.setStock(i * 100);
            productos.add(producto);
        }

        InputStream jasperStream = Main.class.getResourceAsStream("/reports/listado_productos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaReporte", new Date());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        // populado del reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        // generacion PDF
        String outputPath = "C:/tmp/reporte.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);



    }
}
