package pe.pucp.progra3.jasper.rs.jasperreportrest.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import pe.pucp.progra3.jasper.rs.jasperreportrest.dto.ProductoDTO;

import java.io.InputStream;
import java.util.*;
import java.io.InputStream;
import java.util.*;

@Path("download")
public class ReporteRest {

    @GET
    public Response generarReportProducto() throws JRException {
        List<ProductoDTO> productos = new ArrayList<>();
        int N = 1000;
        for(int i = 1; i <= N; i++) {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(i);
            producto.setNombre("Producto " + i);
            producto.setStock(i * 100);
            productos.add(producto);
        }

        InputStream jasperStream = ReporteRest.class.getResourceAsStream("/reports/listado_productos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaReporte", new Date());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        // populado del reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        byte[] reporteBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // 3. Stream the bytes back to the JAX-RS response
        StreamingOutput stream = output -> {
            output.write(reporteBytes);
            output.flush();
        };

        return Response.ok(stream)
                // Change "inline" to "attachment" if you want to force download immediately
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"listado_productos.pdf\"")
                .build();

    }
}
