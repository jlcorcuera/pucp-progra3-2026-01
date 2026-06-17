# Unit 8: Data Reporting & Document Generation

This unit covers the systematic transformation of raw application data into professional, formatted business documents. You will learn to design report templates using JasperReports, compile and fill them with Java Bean collections, and expose them through both standalone console applications and RESTful web services deployed on GlassFish.

---

## 📖 Core Concepts

### 1. Report Generation

**Report generation** is the process of collecting, organizing, and presenting business data in structured, human-readable documents. It serves as a critical bridge between the persistence layer and the decision-making layer of an enterprise application.

Common document types produced by report generation engines include:

| Category | Examples |
|---|---|
| **Transactional Records** | Invoices, purchase orders |
| **Financial Syntheses** | Balance sheets, financial statements |
| **Operational Logs** | Inventory levels, stock reports |
| **Formal Documentation** | Certificates, official corporate or government documents |
| **Analytical Reports** | Data-intensive summaries, typically distributed in PDF format |

### 2. JasperReports

**JasperReports** is the most widely used open-source reporting library in the Java ecosystem. It generates *pixel-perfect* documents in PDF, HTML, Excel, and CSV from any data source. Its key characteristics are:

- **Pure Java Engine:** Runs on any JVM; no native dependencies.
- **Multi-Format Export:** PDF, HTML, Excel (XLS/XLSX), CSV, and more.
- **Extensible Data Adapters:** Decouples the report template from the data provider.
- **Visual Designer:** **Jaspersoft Studio** (Eclipse-based) is the official IDE for designing `.jrxml` templates with drag-and-drop support.

### 3. The Standard Reporting Lifecycle

The report generation workflow follows a four-stage pipeline that separates design from data:

| Stage | Artifact | Description |
|---|---|---|
| **1. Design** | `.jrxml` | Developer defines layout, bands, fields, and parameters in XML. |
| **2. Compilation** | `.jasper` | `JasperCompileManager` converts the XML template to a binary format. Perform at build time to avoid runtime overhead. |
| **3. Filling** | `JasperPrint` | `JasperFillManager` injects data from a `JRDataSource` and runtime parameters into the compiled template. |
| **4. Exporting** | PDF / HTML / etc. | `JasperExportManager` serializes the in-memory `JasperPrint` into the final document. |

> **Key principle:** The `.jasper` binary should be compiled during the Maven build phase and bundled into the classpath — never compiled on every HTTP request.

---

## 📝 JRXML Reference

### 1. Report Bands

The `.jrxml` file is organized into functional **Bands** that govern rendering frequency and position:

| Band | Behavior |
|---|---|
| `<background>` | Reserved for watermarks and global branding elements. |
| `<title>` | Rendered once at the report's beginning. |
| `<pageHeader>` | Rendered at the top of every page. |
| `<columnHeader>` | Rendered once above the detail rows; typically holds column labels. |
| `<detail>` | Repeats once per record provided by the data source. |
| `<columnFooter>` | Rendered at the base of data columns. |
| `<pageFooter>` | Rendered at the bottom of every page (e.g., for page numbers). |
| `<summary>` | Rendered once at the end; ideal for totals and aggregates. |

### 2. Expression Types

Three expression syntaxes handle data flow within the template:

| Expression | Syntax | Description |
|---|---|---|
| **Parameter** | `$P{name}` | External values injected at runtime (e.g., report title, date range filters). |
| **Field** | `$F{name}` | Mappings to the data source — database columns or DTO getter properties. |
| **Variable** | `$V{name}` | Calculated aggregates such as `Sum`, `Avg`, `Count`, `Min`, and `Max`. |

### 3. Core API Classes

| Class | Responsibility |
|---|---|
| `JasperCompileManager` | Converts a `.jrxml` template into a compiled `.jasper` binary. |
| `JRLoader` | De-serializes a `.jasper` file from the classpath or filesystem into a `JasperReport` object. |
| `JasperFillManager` | Fills the compiled report with parameters and data, producing a `JasperPrint` in memory. |
| `JRBeanCollectionDataSource` | Wraps a `Collection<T>` of Java Beans, using reflection to bind DTO properties to report fields. |
| `JasperExportManager` | Provides convenience methods to export `JasperPrint` to PDF files or byte arrays. |

### 4. Data Source Types

| Type | Class | When to Use |
|---|---|---|
| **JavaBeans (recommended)** | `JRBeanCollectionDataSource` | Pass an in-memory `List<DTO>` — keeps business logic in Java, not in the template. |
| **JDBC** | `java.sql.Connection` | The engine executes SQL directly; useful for simple reports. |
| **Empty** | `JREmptyDataSource` | Static reports that rely entirely on parameters, with no tabular data. |

---

## 📦 Maven Dependencies

Both reference projects use **JasperReports 7.0.6** with three mandatory dependencies:

```xml
<!-- Core JasperReports engine -->
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>7.0.6</version>
</dependency>

<!-- Custom font extension (required for proper PDF font embedding) -->
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports-fonts</artifactId>
    <version>7.0.6</version>
</dependency>

<!-- PDF generation support -->
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports-pdf</artifactId>
    <version>7.0.6</version>
</dependency>
```

---

## 📂 Reference Project: `jasper-report-example`

A standalone **Java console application** (`pe.pucp.progra3.jasper.console:jasper-report-example:1.0-SNAPSHOT`, Java 25) demonstrating the complete report generation pipeline from data population to PDF file output.

### Project Structure

```
jasper-report-example/
├── pom.xml
└── src/
    └── main/
        ├── java/pe/pucp/progra3/jasper/dto/
        │   ├── ProductoDTO.java       ← Data Transfer Object for report records
        │   └── Main.java             ← Entry point: builds data, fills, and exports PDF
        └── resources/
            └── reports/
                └── listado_productos.jasper  ← Pre-compiled report binary
```

### 1. Data Transfer Object (`ProductoDTO.java`)

The DTO must expose public getters matching the `<field>` names defined in the `.jrxml` template. JasperReports uses reflection to bind `$F{id}`, `$F{nombre}`, and `$F{stock}` to the corresponding getter methods.

```java
package pe.pucp.progra3.jasper.dto;

public class ProductoDTO {
    private Integer id;
    private String nombre;
    private Integer stock;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
```

### 2. Entry Point (`Main.java`)

Demonstrates the full lifecycle: data preparation → classpath loading → filling → PDF export.

```java
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
        // 1. Prepare the data collection
        List<ProductoDTO> productos = new ArrayList<>();
        int N = 1000;
        for (int i = 1; i <= N; i++) {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(i);
            producto.setNombre("Producto " + i);
            producto.setStock(i * 100);
            productos.add(producto);
        }

        // 2. Load the pre-compiled .jasper binary from the classpath
        InputStream jasperStream = Main.class.getResourceAsStream("/reports/listado_productos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // 3. Define runtime parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaReporte", new Date());

        // 4. Wrap the collection in a JRBeanCollectionDataSource
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        // 5. Fill the report (inject data + parameters → JasperPrint)
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 6. Export to PDF
        String outputPath = "C:/tmp/reporte.pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
    }
}
```

---

## 📂 Reference Project: `jasper-report-rest`

A **Jakarta EE WAR application** (`pe.pucp.progra3.jasper.rs:jasper-report-rest:1.0-SNAPSHOT`, Java 8 source, deployed to GlassFish) that exposes a JAX-RS endpoint for downloading a dynamically generated PDF report over HTTP.

### Project Structure

```
jasper-report-rest/
├── pom.xml
└── src/
    └── main/
        ├── java/pe/pucp/progra3/jasper/rs/jasperreportrest/
        │   ├── ApplicationRS.java              ← JAX-RS application path configuration
        │   ├── HelloServlet.java               ← Baseline Jakarta Servlet
        │   ├── dto/
        │   │   └── ProductoDTO.java            ← DTO matching the report fields
        │   └── rest/
        │       └── ReporteRest.java            ← REST endpoint: GET /services/download
        └── resources/
            └── reports/
                └── listado_productos.jasper    ← Pre-compiled report binary
        └── webapp/
            ├── index.jsp
            └── WEB-INF/
                └── web.xml
```

### 1. Application Configuration (`ApplicationRS.java`)

Registers the base URI path `/services` for all JAX-RS resources in the application:

```java
package pe.pucp.progra3.jasper.rs.jasperreportrest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("services")
public class ApplicationRS extends Application {
}
```

### 2. Data Transfer Object (`ProductoDTO.java`)

Identical structure to the console example — the same `.jasper` report template is reused across both projects:

```java
package pe.pucp.progra3.jasper.rs.jasperreportrest.dto;

public class ProductoDTO {
    private Integer id;
    private String nombre;
    private Integer stock;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
```

### 3. Report REST Endpoint (`ReporteRest.java`)

The core JAX-RS resource that generates the report in memory and streams it directly to the HTTP response as a `application/pdf` payload:

```java
package pe.pucp.progra3.jasper.rs.jasperreportrest.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import pe.pucp.progra3.jasper.rs.jasperreportrest.dto.ProductoDTO;

import java.io.InputStream;
import java.util.*;

@Path("download")
public class ReporteRest {

    @GET
    public Response generarReportProducto() throws JRException {
        // 1. Build the data collection
        List<ProductoDTO> productos = new ArrayList<>();
        int N = 1000;
        for (int i = 1; i <= N; i++) {
            ProductoDTO producto = new ProductoDTO();
            producto.setId(i);
            producto.setNombre("Producto " + i);
            producto.setStock(i * 100);
            productos.add(producto);
        }

        // 2. Load the pre-compiled report from the classpath
        InputStream jasperStream = ReporteRest.class.getResourceAsStream("/reports/listado_productos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // 3. Inject parameters and fill
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fechaReporte", new Date());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 4. Export to a byte array (in-memory; no filesystem path needed)
        byte[] reporteBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // 5. Stream bytes back as the HTTP response body
        StreamingOutput stream = output -> {
            output.write(reporteBytes);
            output.flush();
        };

        return Response.ok(stream)
                // Change "inline" to "attachment" to force an immediate download dialog
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"listado_productos.pdf\"")
                .build();
    }
}
```

**Endpoint summary:**

| Method | Path | Description |
|---|---|---|
| `GET` | `/services/download` | Generates a 1,000-row product listing report and returns it as an inline PDF response. |

Access URL: `http://localhost:8080/jasper-report-rest-1.0-SNAPSHOT/services/download`

---

## 📄 Report Template: `listado_productos.jrxml`

The shared JRXML template used by both projects, created with **Jaspersoft Studio 7.0.6**. It generates an A4-portrait PDF listing product records.

```xml
<!-- Created with Jaspersoft Studio version 7.0.6 -->
<jasperReport name="listado_productos" language="java"
              pageWidth="595" pageHeight="842"
              columnWidth="555" leftMargin="20" rightMargin="20"
              topMargin="20" bottomMargin="20"
              uuid="41e8fece-36e5-4d66-9f9c-f26b7068d93a">

    <!-- Parameters: injected at runtime via Map<String, Object> -->
    <parameter name="fechaReporte" class="java.util.Date"/>

    <!-- Fields: bound to ProductoDTO properties via JRBeanCollectionDataSource -->
    <field name="id"     class="java.lang.Integer"/>
    <field name="nombre" class="java.lang.String"/>
    <field name="stock"  class="java.lang.Integer"/>

    <!-- Page Header: shows formatted print date using $P{fechaReporte} -->
    <pageHeader height="41">
        <element kind="staticText" x="227" y="2" width="142" height="30">
            <text><![CDATA[Fecha impresion:]]></text>
        </element>
        <element kind="textField" x="370" y="2" width="190" height="30">
            <expression><![CDATA[new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format($P{fechaReporte})]]></expression>
        </element>
    </pageHeader>

    <!-- Column Header: static labels for each column -->
    <columnHeader height="61">
        <element kind="staticText" x="10"  y="31" width="240" height="30"
                 mode="Opaque" backcolor="#BAA7A6" hTextAlign="Center" vTextAlign="Middle">
            <text><![CDATA[nombre]]></text>
        </element>
        <element kind="staticText" x="250" y="31" width="280" height="30"
                 hTextAlign="Center" vTextAlign="Middle">
            <text><![CDATA[stock]]></text>
        </element>
    </columnHeader>

    <!-- Detail: repeated once per ProductoDTO record in the datasource -->
    <detail>
        <band height="32">
            <element kind="textField" x="10"  y="0" width="240" height="30">
                <expression><![CDATA[$F{nombre}]]></expression>
            </element>
            <element kind="textField" x="250" y="0" width="280" height="30">
                <expression><![CDATA[$F{stock}]]></expression>
            </element>
        </band>
    </detail>

    <!-- Page Footer: page number variable -->
    <pageFooter height="70">
        <element kind="textField" x="360" y="40" width="100" height="30">
            <expression><![CDATA[$V{PAGE_NUMBER}]]></expression>
        </element>
    </pageFooter>

</jasperReport>
```

**Template anatomy summary:**

| Element | Type | Value/Expression |
|---|---|---|
| `fechaReporte` | Parameter | `java.util.Date` — injected at runtime |
| `id` | Field | `java.lang.Integer` — maps to `ProductoDTO.getId()` |
| `nombre` | Field | `java.lang.String` — maps to `ProductoDTO.getNombre()` |
| `stock` | Field | `java.lang.Integer` — maps to `ProductoDTO.getStock()` |
| `$V{PAGE_NUMBER}` | Variable | Built-in page number counter |
| Print date expression | Text Field | `new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format($P{fechaReporte})` |

---

## ✅ Best Practices & Common Pitfalls

### ✔️ Best Practices

| Practice | Rationale |
|---|---|
| **Pre-compile `.jrxml` → `.jasper` at build time** | Eliminates the Groovy/Java compiler overhead on every execution; the `.jasper` binary is placed in `src/main/resources/reports/`. |
| **Load via classpath** | Use `MyClass.class.getResourceAsStream("/reports/file.jasper")` instead of absolute filesystem paths for portability. |
| **Use `JRBeanCollectionDataSource`** | Keeps SQL and business logic in Java; the report template remains presentation-only. |
| **Set `Content-Disposition` header** | Essential in web responses to instruct the browser whether to display inline or force a download dialog. |
| **Cache the `JasperReport` object** | The compiled object is thread-safe and can be cached in a Singleton to eliminate redundant I/O and deserialization. |

### ❌ Anti-patterns to Avoid

| Anti-pattern | Risk |
|---|---|
| **Compiling `.jrxml` on every request** | Significant CPU/memory spikes; blocks request threads. |
| **Using absolute file paths** | Breaks portability across environments (development, staging, production). |
| **Embedding business logic in JRXML expressions** | Reduces testability and maintainability; complex calculations belong in the Java layer. |
| **Ignoring `JRException`** | This is a checked exception; all JasperReports API calls must be wrapped in `try-catch` or declared with `throws`. |
| **Generating large reports synchronously over HTTP** | Risk of HTTP timeout and `OutOfMemoryError`; consider asynchronous generation and pagination for large datasets. |
| **Omitting `Content-Type: application/pdf`** | The browser may not render the PDF inline, displaying raw binary instead. |
