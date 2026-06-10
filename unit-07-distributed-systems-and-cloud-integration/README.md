# Unit 7: Distributed Systems & Cloud Integration

This unit explores client-server communication using distributed architectures. You will learn to implement, test, and consume both SOAP (Simple Object Access Protocol) and RESTful (Representational State Transfer) Web Services in the Java ecosystem using Jakarta EE, and integrate them with clients developed in Java and C# (.NET).

---

## 📖 Core Concepts

Before building web services, it is essential to understand the architectural foundation and underlying components:

### 1. Jakarta EE
**Jakarta EE** (formerly Java EE, maintained by the Eclipse Foundation) is a set of specifications and APIs designed for developing robust, scalable, and secure enterprise applications in Java. It includes standard specifications such as Servlets, JSP, JAX-WS, JAX-RS, CDI, and EJB.

### 2. Application Server
An **Application Server** is a software platform that provides a runtime environment for enterprise applications. It manages requests, sessions, security, transactions, database connectivity, and scaling, abstracting infrastructure concerns from the developer.

### 3. GlassFish
**GlassFish** is the open-source reference implementation for the Jakarta EE specification. It allows developers to deploy, run, and manage enterprise Java web applications utilizing Servlets, JAX-WS, and JAX-RS.

### 4. Web Service
A **Web Service** is a standardized technology that enables interoperable machine-to-machine communication over a network (such as the Internet), allowing heterogeneous applications (written in different languages like Java, C#, Python) to exchange data.

### 5. SOAP vs. REST Architectures

| Feature | SOAP (Simple Object Access Protocol) | REST (Representational State Transfer) |
|---|---|---|
| **Protocol / Style** | Strict, XML-based protocol. | Architectural style utilizing standard HTTP. |
| **Data Format** | XML only (strict schema constraint). | Multiple formats (JSON, XML, HTML, Text). JSON is standard. |
| **Contract** | Strict contract using **WSDL** (Web Services Description Language). | Loose coupling, documented with OpenAPI/Swagger or self-descriptive. |
| **Transport** | Protocol-independent (HTTP, SMTP, JMS, etc.). | Designed specifically to work over HTTP/HTTPS. |
| **State** | Stateless by default, but can be stateful. | Strictly stateless (each request contains all context). |

---

## 🔌 Part 1: SOAP Web Services in Java & C#

### 🛠️ 1. Environment and Application Server Setup

To configure **GlassFish** and development plugins in **IntelliJ IDEA**:

1. **Install Plugins:**
   - Go to `File > Settings > Plugins`.
   - Search and install **GlassFish** (by JetBrains).
   - Search and install **Jakarta EE: Web Services (JAX-WS)**.
2. **Download GlassFish:**
   - Visit [glassfish.org/download](https://glassfish.org/download) and download the zip bundle (e.g., **Eclipse GlassFish 8.0.2, Jakarta EE Platform 11**).
   - Extract the `.zip` file to a stable directory on your computer (e.g., `C:\glassfish8`).
3. **Configure Java Home for GlassFish:**
   - Locate the configuration directory: `<glassfish_root>\glassfish\config\`.
   - Open `asenv.bat` (on Windows) and append the JDK path:
     ```batch
     set AS_JAVA=C:\Program Files\Java\jdk-25.0.2
     ```
4. **Register GlassFish Server in IntelliJ:**
   - Go to `File > Settings > Build, Execution, Deployment > Application Servers`.
   - Click `+` and choose **Glassfish Server**.
   - Set the **GlassFish Home** path to your extracted directory (e.g., `C:\glassfish8\glassfish`) and click **OK**.

---

### 💻 2. Creating a SOAP Web Service in Java

#### Step 2.1: Initialize the Project
1. Create a new project in IntelliJ IDEA of type **Jakarta EE** with the template set to **Web Application**.
2. Select your Application Server (**GlassFish 8.0.2**).
3. In the dependencies wizard, check **XML Web Services (JAX-WS)** and optionally **Servlet**. Click **Create**.

#### Step 2.2: Implement the Web Service Class
Create a package `pe.edu.pucp.softprog.services` and write the service class `PruebaWS.java`:

```java
package pe.edu.pucp.softprog.services; 
 
import jakarta.jws.WebMethod; 
import jakarta.jws.WebService; 
 
@WebService( 
    serviceName = "PruebaWS", 
    targetNamespace = "http://services.softprog.pucp.edu.pe/" 
) 
public class PruebaWS { 
 
    @WebMethod(operationName = "saludar") 
    public String saludar(String nombre) { 
        return "Hola " + nombre; 
    } 
}
```

#### Step 2.3: Run and Test the Web Service
1. Run the project configuration. IntelliJ will launch GlassFish and deploy your war bundle.
2. Once deployed, GlassFish generates a testing page and a WSDL description automatically:
   - **Test Web Page:** Append `?Tester` to the service URL:
     `http://localhost:8080/SoftProgWS-1.0-SNAPSHOT/PruebaWS?Tester`
   - **WSDL Definition:** Append `?wsdl` to the service URL:
     `http://localhost:8080/SoftProgWS-1.0-SNAPSHOT/PruebaWS?WSDL`

---

### 📝 3. JAX-WS Reference Table

Below are the annotations used to construct SOAP web services under the Jakarta XML Web Services specification:

| Annotation | Description |
|---|---|
| `@WebService` | Marks the Java class as a SOAP Web Service endpoint. |
| `serviceName` | Property of `@WebService` defining the service port name exposed in the WSDL. |
| `targetNamespace` | Property of `@WebService` mapping XML structures to a unique XML URI namespace. |
| `@WebMethod` | Exposes a public class method as a SOAP operation. |
| `operationName` | Property of `@WebMethod` renaming the operation inside the XML SOAP payload. |
| `@WebParam` | Specifies the XML element name and mapping rules for an input method parameter. |

---

### 🧮 4. Comprehensive SOAP Example: `CalculadoraWS`

Below is an arithmetic SOAP web service implementing calculator operations:

```java
package pe.edu.pucp.softprog.services; 
import jakarta.jws.WebMethod; 
import jakarta.jws.WebParam; 
import jakarta.jws.WebService; 
 
@WebService( 
    serviceName = "CalculadoraWS", 
    targetNamespace = "http://services.softprog.pucp.edu.pe/" 
) 
public class CalculadoraWS { 
 
    @WebMethod(operationName = "sumar") 
    public double sumar( 
        @WebParam(name = "numero1") double numero1, 
        @WebParam(name = "numero2") double numero2) { 
        return numero1 + numero2; 
    } 
 
    @WebMethod(operationName = "restar") 
    public double restar( 
        @WebParam(name = "numero1") double numero1, 
        @WebParam(name = "numero2") double numero2) { 
        return numero1 - numero2; 
    } 
 
    @WebMethod(operationName = "multiplicar") 
    public double multiplicar( 
        @WebParam(name = "numero1") double numero1, 
        @WebParam(name = "numero2") double numero2) { 
        return numero1 * numero2; 
    } 
 
    @WebMethod(operationName = "dividir") 
    public double dividir( 
        @WebParam(name = "numero1") double numero1, 
        @WebParam(name = "numero2") double numero2) { 
 
        if (numero2 == 0) { 
            throw new ArithmeticException("No es posible dividir entre cero."); 
        } 
        return numero1 / numero2; 
    } 
}
```

---

### 🖥️ 5. Consuming the SOAP Web Service in C# (.NET)

To invoke the exposed Java SOAP operations from a C# client:

1. **Add Service Reference:**
   - In a C# Console Application project in Visual Studio, right-click the project file and select **Add > Service Reference...** (Agregar > Referencia de servicio...).
   - Choose **WCF Web Service** and click Next.
   - In the **URI** field, enter the WSDL address:
     `http://localhost:8080/SoftProgWS-1.0-SNAPSHOT/CalculadoraWS?WSDL`
   - Click **Go / Ir**. Once discovered, set the namespace to `ServiciosWeb` and click Next.
2. **Configure Data Types:**
   - Set the collection type representation to **`System.Collections.Generic.List`** to handle list returns correctly.
   - Maintain the access level of the generated classes as **Public** and finalize configuration.
3. **Execute Client Invocations:**

```csharp
using System;
using ServiciosWeb; 
 
public class Program 
{ 
    public static void Main(string[] args) 
    { 
        // Instantiate the auto-generated client proxy
        CalculadoraWSClient ws = new CalculadoraWSClient(); 
 
        double suma = ws.sumar(10, 5); 
        double resta = ws.restar(10, 5); 
        double multiplicacion = ws.multiplicar(10, 5); 
        double division = ws.dividir(10, 5); 
 
        Console.WriteLine("Suma: " + suma); 
        Console.WriteLine("Resta: " + resta); 
        Console.WriteLine("Multiplicación: " + multiplicacion); 
        Console.WriteLine("División: " + division); 
    } 
}
```

---

## 🌐 Part 2: RESTful Web Services (Jakarta REST)

REST is centered around **resources** exposed via uniform resource identifiers (URIs) and manipulated using standard HTTP request verbs.

### 📝 1. HTTP Methods and CRUD Mapping

REST maps standard actions on entities directly onto HTTP verbs:

| CRUD Operation | HTTP Verb | Purpose / Path Context |
|---|---|---|
| **CREATE** | `POST` | Create a new resource on the server. |
| **READ (ALL)** | `GET` | Retrieve list representation of all resources. |
| **READ (ONE)** | `GET` | Retrieve resource attributes by identifier (e.g. `/AreaRS/{id}`). |
| **UPDATE** | `PUT` | Replace an existing resource or update properties. |
| **DELETE** | `DELETE` | Remove a resource from the server storage by ID. |

---

### 📝 2. Jakarta REST (JAX-RS) Annotations

| Annotation | Target Level | Description |
|---|---|---|
| `@Path` | Class / Method | Defines the relative URI template path mapping. Supports variable templates: `{id}`. |
| `@GET` | Method | Configures the endpoint to handle HTTP GET requests. |
| `@POST` | Method | Configures the endpoint to handle HTTP POST requests. |
| `@PUT` | Method | Configures the endpoint to handle HTTP PUT requests. |
| `@DELETE` | Method | Configures the endpoint to handle HTTP DELETE requests. |
| `@PathParam` | Parameter | Extracts values bound to the URI template path variables (e.g., `@Path("{id}")`). |
| `@QueryParam` | Parameter | Extracts request values supplied via URL query parameters (e.g., `?nombre=Juan`). |
| `@Consumes` | Class / Method | Restricts requests to specified MIME media types (e.g., `application/json`). |
| `@Produces` | Class / Method | Declares the returned response media payload type (e.g., `application/json`, `text/html`). |
| `@ApplicationPath` | Class | Registers the base path prefix for all resources. Must be placed on a class extending `Application`. |

---

### 💻 3. Setting Up a REST Web Service in Java

#### Step 3.1: Install Plugin & Project Setup
1. In IntelliJ IDEA, ensure the plugin **Jakarta EE: RESTful Web Services (JAX-RS)** is enabled.
2. Create a new **Jakarta EE Web Application** project targeting **GlassFish 8.0.2** using Maven.
3. Check **RESTful Web Services (JAX-RS)** and **Servlet** under dependencies.
4. Ensure the JDK is set to version **25** inside the `pom.xml` build configuration:
   ```xml
   <maven.compiler.target>25</maven.compiler.target>
   <maven.compiler.source>25</maven.compiler.source>
   ```

#### Step 3.2: Configure the REST Application Path
Create a class extending the standard resource configuration. This class registers the application path root:

```java
package pe.edu.pucp.softprog.services;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("webresources")
public class RestApplication extends Application {
    // This class activates the automatic scanning of REST resources
}
```

#### Step 3.3: Implement the Resource Endpoint (`ServicioRS`)
Write a resource endpoint returning HTML structure:

```java
package pe.edu.pucp.softprog.services;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("ServicioRS")
public class ServicioRS {
 
    @GET
    @Produces("text/html")
    public String getHtml(@QueryParam("nombre") String nombre) {
        if (nombre == null || nombre.isBlank()) {
            nombre = "Invitado";
        }
        return "<html lang=\"es\">" +
               "<head><title>Página de Saludo</title></head>" +
               "<body><h1>Hola " + nombre + "!</h1></body>" +
               "</html>";
    }
}
```

Access the endpoint at:
`http://localhost:8080/WebApplication-1.0-SNAPSHOT/webresources/ServicioRS?nombre=Juan`

#### Step 3.4: Configuring a Clean Context Path
To omit the default `-1.0-SNAPSHOT` suffix:
1. In `pom.xml`, inside the `<build>` node, define a static build name:
   ```xml
   <finalName>WebApplication</finalName>
   ```
2. Modify the target deployment configuration URL in your IntelliJ GlassFish configuration:
   `http://localhost:8080/WebApplication/`
3. Now, your clean access path is:
   `http://localhost:8080/WebApplication/webresources/ServicioRS?nombre=Juan`

---

### 📦 4. RESTful CRUD API Implementation (JSON Representation)

We will build a CRUD web service handling database-like entities with JSON serialization.

#### Step 4.1: Create Domain Entity (`Area`)
```java
package pe.edu.pucp.softprog.model;

public class Area {
    private int idArea;
    private String nombre;
    private boolean activa;

    public Area(int idArea, String nombre, boolean activa){
        this.idArea = idArea;
        this.nombre = nombre;
        this.activa = activa;
    }
    
    public Area(){}

    public int getIdArea() { return idArea; }
    public void setIdArea(int idArea) { this.idArea = idArea; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
```

#### Step 4.2: Implement Resource Controller (`AreaRS`)
This controller simulates database transactions using an in-memory `List` collections buffer.

```java
package pe.edu.pucp.softprog.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.softprog.model.Area;

@Path("AreaRS")
@Produces(MediaType.APPLICATION_JSON) // All responses return JSON payloads
@Consumes(MediaType.APPLICATION_JSON) // All request methods process JSON data
public class AreaRS { 

    // Simulation of database records in-memory
    private static List<Area> listaAreas = new ArrayList<>();
    private static int contadorId = 1;

    static {
        listaAreas.add(new Area(contadorId++, "SISTEMAS", true));
        listaAreas.add(new Area(contadorId++, "CONTABILIDAD", true));
    } 

    // GET: Retrieve all active/inactive Areas
    @GET
    public List<Area> listarAreas() {
        return listaAreas;
    } 

    // GET: Retrieve specific Area entity properties by ID
    @GET
    @Path("{id}")
    public Response obtenerAreaPorId(@PathParam("id") int id) {
        for (Area a : listaAreas) {
            if (a.getIdArea() == id) {
                return Response.ok(a).build(); // HTTP 200 OK + JSON
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build(); // HTTP 404
    }

    // POST: Insert a new Area record
    @POST
    public Response crearArea(Area nuevaArea) {
        nuevaArea.setIdArea(contadorId++);
        listaAreas.add(nuevaArea);
        return Response.status(Response.Status.CREATED).entity(nuevaArea).build(); // HTTP 201 Created + JSON
    }

    // PUT: Update an existing Area entity properties by ID
    @PUT
    @Path("{id}")
    public Response actualizarArea(@PathParam("id") int id, Area areaActualizada) {
        for (int i = 0; i < listaAreas.size(); i++) {
            if (listaAreas.get(i).getIdArea() == id) {
                areaActualizada.setIdArea(id); // Retain original primary ID
                listaAreas.set(i, areaActualizada);
                return Response.ok(areaActualizada).build(); // HTTP 200 OK + JSON
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build(); // HTTP 404
    }

    // DELETE: Delete an Area entity record from storage by ID
    @DELETE
    @Path("{id}")
    public Response eliminarArea(@PathParam("id") int id) {
        for (Area a : listaAreas) {
            if (a.getIdArea() == id) {
                listaAreas.remove(a);
                return Response.noContent().build(); // HTTP 204 No Content
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build(); // HTTP 404
    }
}
```

---

### 🧪 5. Testing RESTful Endpoints with Postman

To verify correct behavior, test the endpoints using **Postman** (or an equivalent REST client):

1. **CREATE AN AREA (POST):**
   - **URL:** `http://localhost:8080/WebApplication/webresources/AreaRS`
   - **Headers:** `Content-Type: application/json`
   - **Body (raw JSON):**
     ```json
     {
         "nombre": "LOGÍSTICA",
         "activa": true
     }
     ```
   - **Expected Status:** `201 Created`
2. **OBTAIN SPECIFIC AREA BY ID (GET):**
   - **URL:** `http://localhost:8080/WebApplication/webresources/AreaRS/1`
   - **Expected Status:** `200 OK`
3. **MODIFY AN AREA (PUT):**
   - **URL:** `http://localhost:8080/WebApplication/webresources/AreaRS/2`
   - **Headers:** `Content-Type: application/json`
   - **Body (raw JSON):**
     ```json
     {
         "nombre": "CONTABILIDAD MODIFICADA",
         "activa": false
     }
     ```
   - **Expected Status:** `200 OK`
4. **DELETE AN AREA (DELETE):**
   - **URL:** `http://localhost:8080/WebApplication/webresources/AreaRS/2`
   - **Expected Status:** `204 No Content`
5. **RETRIEVE ALL RECORDS (GET):**
   - **URL:** `http://localhost:8080/WebApplication/webresources/AreaRS`
   - **Expected Status:** `200 OK`

---

## 💻 Part 3: Consuming RESTful Web Services

This section presents implementation guides for consuming REST APIs programmatically from client applications using Java and C#.

### ☕ 1. Consuming RESTful Services in Java

We will use Java's native HTTP Client (`java.net.http`) and the **Jackson** serialization library.

#### Step 1.1: Configure Maven Dependency
Add the Jackson databind library to your client project `pom.xml`:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.19.0</version>
</dependency>
```

#### Step 1.2: Code Implementations for CRUD Operations

##### A. Read All Areas (GET Request)
```java
package pe.edu.pucp.softprog.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import pe.edu.pucp.softprog.model.Area;

public class Principal {
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/WebApplication/webresources/AreaRS";
        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
                
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Area> listaAreas = mapper.readValue(json, new TypeReference<ArrayList<Area>>() {});
        
        for (Area area : listaAreas) {
            System.out.println(area.getIdArea() + " - " + area.getNombre() + " - Activa: " + area.isActiva());
        }
    }
}
```

##### B. Insert a New Area (POST Request)
```java
package pe.edu.pucp.softprog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import pe.edu.pucp.softprog.model.Area;

public class PrincipalInsertar {
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:8080/WebApplication/webresources/AreaRS";
        
        Area nuevaArea = new Area();
        nuevaArea.setNombre("RECURSOS HUMANOS");
        nuevaArea.setActiva(true);
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(nuevaArea);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
                
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Código de respuesta: " + response.statusCode());
        System.out.println("Respuesta del servidor: " + response.body());
        
        // Deserialize returned entity
        Area areaRespuesta = mapper.readValue(response.body(), Area.class);
        System.out.println("Área creada: " + areaRespuesta.getIdArea() + " - " + areaRespuesta.getNombre());
    }
}
```

##### C. Update an Area (PUT Request)
```java
package pe.edu.pucp.softprog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import pe.edu.pucp.softprog.model.Area;

public class PrincipalModificar {
    public static void main(String[] args) throws Exception {
        int idAreaAModificar = 1;
        String url = "http://localhost:8080/WebApplication/webresources/AreaRS/" + idAreaAModificar;
        
        Area areaModificada = new Area();
        areaModificada.setNombre("LOGÍSTICA");
        areaModificada.setActiva(false);
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(areaModificada);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
                
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Código de respuesta: " + response.statusCode());
        System.out.println("Respuesta del servidor: " + response.body());
        
        Area areaRespuesta = mapper.readValue(response.body(), Area.class);
        System.out.println("Área actualizada: " + areaRespuesta.getIdArea() + " - " + areaRespuesta.getNombre());
    }
}
```

##### D. Delete an Area (DELETE Request)
```java
package pe.edu.pucp.softprog.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PrincipalEliminar {
    public static void main(String[] args) throws Exception {
        int idAreaAEliminar = 1;
        String url = "http://localhost:8080/WebApplication/webresources/AreaRS/" + idAreaAEliminar;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
                
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Código de respuesta: " + response.statusCode());
        System.out.println("Respuesta del servidor: " + response.body());
    }
}
```

##### E. Retrieve Area by ID (GET Request)
```java
package pe.edu.pucp.softprog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import pe.edu.pucp.softprog.model.Area;

public class PrincipalObtenerPorID {
    public static void main(String[] args) throws Exception {
        int idArea = 2;
        String url = "http://localhost:8080/WebApplication/webresources/AreaRS/" + idArea;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
                
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Código de respuesta: " + response.statusCode());
        System.out.println("JSON recibido: " + response.body());
        
        ObjectMapper mapper = new ObjectMapper();
        Area area = mapper.readValue(response.body(), Area.class);
        System.out.println("Área recibida:");
        System.out.println("ID: " + area.getIdArea());
        System.out.println("Nombre: " + area.getNombre());
        System.out.println("Activa: " + area.isActiva());
    }
}
```

---

### 🖥️ 2. Consuming RESTful Services in C# (.NET)

We will use C# Console App, standard `HttpWebRequest` and **Newtonsoft.Json** library for payload translation.

#### Step 2.1: Implement C# Class Entity Model
Use JSON Property mapping to match serialization names sent by Jakarta EE:

```csharp
using Newtonsoft.Json;

public class Area
{ 
    private int idArea;
    private string nombre;
    private bool activa;

    [JsonProperty("idArea")]
    public int IdArea { get => idArea; set => idArea = value; }

    [JsonProperty("nombre")]
    public string Nombre { get => nombre; set => nombre = value; }
 
    [JsonProperty("activa")]
    public bool Activa { get => activa; set => activa = value; }
}
```

#### Step 2.2: Implement Client Listing (GET Request)
```csharp
using System;
using System.IO;
using System.Net;
using System.ComponentModel;
using Newtonsoft.Json;

class Program
{
    private const string BASE_URL = "http://localhost:8080/SoftProgRS/webresources/AreaRS";

    static void Main(string[] args)
    { 
        HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{BASE_URL}");
        req.Method = "GET";
        req.Accept = "application/json";
        req.Timeout = 30000;

        using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
        using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
        {
            string json = sr.ReadToEnd();
            BindingList<Area> areas = JsonConvert.DeserializeObject<BindingList<Area>>(json) ?? new BindingList<Area>();
            
            foreach (Area a in areas)
            {
                Console.WriteLine(a.IdArea + ". " + a.Nombre);
            }
        }
    } 
}
```

#### Step 2.3: Implement Client Insertion (POST Request)
```csharp
using System;
using System.IO;
using System.Net;
using Newtonsoft.Json;

internal class Program
{ 
    private const string BASE_URL = "http://localhost:8080/SoftProgRS/webresources/AreaRS";

    static void Main(string[] args)
    { 
        HttpWebRequest req = (HttpWebRequest)WebRequest.Create($"{BASE_URL}");
        req.Method = "POST"; 
        req.Accept = "application/json"; 
        req.ContentType = "application/json"; 
        req.Timeout = 30000;

        Area area = new Area();
        area.Nombre = "RECURSOS HUMANOS"; 

        string jsonBody = JsonConvert.SerializeObject(area);

        using (StreamWriter sw = new StreamWriter(req.GetRequestStream()))
        {
            sw.Write(jsonBody);
            sw.Flush();
        }

        using (HttpWebResponse resp = (HttpWebResponse)req.GetResponse())
        using (StreamReader sr = new StreamReader(resp.GetResponseStream()))
        {
            string json = sr.ReadToEnd();
            int resultado;
            try
            { 
                resultado = JsonConvert.DeserializeObject<int>(json);
            } 
            catch
            { 
                resultado = int.Parse(json);
            } 
            
            Console.WriteLine("Resultado de inserción: " + resultado);
            if (resultado != 0)
            {
                Console.WriteLine("Inserción finalizada correctamente.");
            }
        }
    } 
}
```

---

## 📂 Reference Project: `softprog-main-rs`

This project is a multi-module Maven project structured as follows:

```
softprog-main-rs/
├── pom.xml                   ← Parent project configuration (pom packaging)
└── softprog-rs/              ← Web service module (war packaging)
    ├── pom.xml               ← Module-specific dependencies and build configs
    └── src/main/java/pe/pucp/progra3/rs/
        ├── RestApplication.java
        ├── HelloWorldRest.java
        ├── AlumnoRest.java
        ├── HelloServlet.java
        └── dto/
            ├── SaludoDTO.java
            └── AlumnoDTO.java
```

### 1. Key Components & Class Reference

#### A. Application Configuration (`RestApplication.java`)
Configures the base URI prefix mapping for all REST resources in the application:
```java
package pe.pucp.progra3.rs;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("services")
public class RestApplication extends Application {
}
```
With this configuration, all endpoints exposed inside JAX-RS resources will be mapped relative to the `/services` path (e.g., `http://localhost:8080/softprog-rs-1.0-SNAPSHOT/services/...`).

#### B. Greeting Web Service (`HelloWorldRest.java`)
Exposes simple test routes demonstrating string and object serialization:
```java
package pe.pucp.progra3.rs;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import pe.pucp.progra3.rs.dto.SaludoDTO;

@Path("hello")
public class HelloWorldRest {

    @GET
    public String saludar() {
        return "Hello from my first rest service";
    }

    @GET
    @Path("hello")
    @Produces("application/json")
    public SaludoDTO saludar2() {
        SaludoDTO saludoDTO = new SaludoDTO();
        saludoDTO.setMessage("Hello from JSON object");
        return saludoDTO;
    }
}
```
* **GET `/services/hello`** returns plaintext: `"Hello from my first rest service"`.
* **GET `/services/hello/hello`** returns JSON: `{"message":"Hello from JSON object"}`.

#### C. Student Entity CRUD Service (`AlumnoRest.java`)
A fully-featured REST controller implementing annotations for POST, GET, DELETE, path variables, and query parameters:
```java
package pe.pucp.progra3.rs;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import pe.pucp.progra3.rs.dto.AlumnoDTO;

import java.util.ArrayList;
import java.util.List;

@Path("/alumno")
@Consumes("application/json")
@Produces("application/json")
public class AlumnoRest {

    @POST
    public AlumnoDTO registrar(AlumnoDTO alumno) {
        alumno.setCodigo("NUEVO CODIGO");
        return alumno;
    }

    @Path("{id}")
    @GET
    public AlumnoDTO obtenerPorId(@PathParam("id") Integer id) {
        AlumnoDTO alumno = new AlumnoDTO();
        alumno.setCodigo(id + "");
        alumno.setNombre("Jose");
        return alumno;
    }

    @Path("{id_alumno}/apoderado/{id_apoderado}")
    @GET
    public AlumnoDTO obtenerApoderado(@PathParam("id_alumno") Integer idAlumno,
                                      @PathParam("id_apoderado") Integer idApoderado) {
        AlumnoDTO alumno = new AlumnoDTO();
        alumno.setCodigo(idAlumno + "");
        alumno.setNombre("Jose");
        return alumno;
    }

    @Path("delete/{id}")
    @DELETE
    public Response eliminarAlumno(@PathParam("id") Integer id) {
        // Business logic to delete student record goes here
        return Response.ok().build();
    }

    @Path("search")
    @GET
    public List<AlumnoDTO> listar(@QueryParam("first_name") String firstName,
                                  @QueryParam("last_name") String lastName) {
        List<AlumnoDTO> myList = new ArrayList<>();
        int n = 10;
        for(int i = 1; i <= n; i++) {
            AlumnoDTO alumnoDTO = new AlumnoDTO();
            alumnoDTO.setCodigo(i + "");
            alumnoDTO.setNombre("Nombre " + i);
            myList.add(alumnoDTO);
        }
        return myList;
    }
}
```

* **POST `/services/alumno`**: Registers a student using JSON body content.
* **GET `/services/alumno/{id}`**: Extracts ID path parameter.
* **GET `/services/alumno/{id_alumno}/apoderado/{id_apoderado}`**: Demonstrates using multiple path parameters.
* **DELETE `/services/alumno/delete/{id}`**: Deletes a student using DELETE HTTP verb, returning empty 200 OK.
* **GET `/services/alumno/search?first_name=X&last_name=Y`**: Illustrates search filtering using query parameters.

#### D. Base Servlet Integration (`HelloServlet.java`)
Baseline Jakarta Servlet mapping demonstrating traditional Java web servlet configuration:
```java
package pe.pucp.progra3.rs;

import java.io.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
```
Access URL: `http://localhost:8080/softprog-rs-1.0-SNAPSHOT/hello-servlet`

### 2. Data Transfer Objects (DTOs)

* **`SaludoDTO.java`**: Models a simple JSON structure with a `message` string field.
* **`AlumnoDTO.java`**: Models student domain fields containing `codigo`, `nombre`, and `apellido`.

---

## 🔄 Case Study: Java Web Services (`softprog`) & Blazor Web Client (`SoftProg-WebApp`)

This unit includes a complete implementation of Java-based enterprise web services (`softprog`) and their integration with a C# front-end application (`SoftProg-WebApp`) built on Blazor Server.

### 1. The Java Backend: `softprog`
The `softprog` directory is structured as a multi-tier modular Maven application containing:
* **`softprog-ws` (SOAP):** Exposes SOAP services such as `CustomerWS` for client retrieval.
* **`softprog-rs` (REST):** Exposes lightweight JSON resource endpoints such as `FilmsRS`.

For example, `CustomerWS.java` exposes a paginated customer query operation:
```java
@WebService(
    serviceName = "CustomerWS",
    targetNamespace = "http://services.softprog.pucp.edu.pe/"
)
public class CustomerWS {
    private CustomerBL customerBL = new CustomerBLImpl();

    @WebMethod(operationName = "search")
    public CustomerSearchResultDTO search(String name, String firstName, String docNumber, int page, int recordsPerPage) {
        CustomerSearchResultDTO customerSearchResultDTO = new CustomerSearchResultDTO();
        try {
            customerSearchResultDTO.setTotalCount(customerBL.totalNumberOfRecords(name, firstName, docNumber));
            customerSearchResultDTO.setPageData(customerBL.fetchPage(name, firstName, docNumber, page, recordsPerPage));
        } catch (BusinessLogicException e) {
            throw new RuntimeException(e);
        }
        return customerSearchResultDTO;
    }
}
```

### 2. The Blazor Frontend: `SoftProg-WebApp`
The C# client application (`SoftProg-WebApp`) consumes the backend services using auto-generated client proxies.

#### SOAP Service Consumption in Blazor
1. **Service Reference:** Registered inside the project's **Connected Services** section, referencing the WSDL:
   `http://localhost:8080/softprog-ws-1.0-SNAPSHOT/CustomerWS?wsdl`
2. **Page Consumption (`AdministrarClientes.razor`):** Instantiates the C# proxy `CustomerWSClient` and invokes the SOAP query directly within the table pagination code:
```csharp
@code {
    private CustomerWSClient customerWSClient = new CustomerWSClient();
    private List<Cliente> todosLosClientes = new();
    private TablePage<Cliente> paginaClientes = new();
    private PageQuery queryActual = new(1, 3);

    private void ActualizarPagina()
    {
        // Call the auto-generated SOAP proxy client
        customerSearchResultDTO results = customerWSClient.search(
            filtroNombre, filtroApellido, filtroDNI, 
            queryActual.Page, queryActual.PageSize
        );
        
        // Map raw SOAP payload arrays to Blazor local data models
        customer[] pageData = results.pageData;
        todosLosClientes.Clear();
        foreach (customer customer in pageData)
        {
            todosLosClientes.Add(new Cliente {
                DNI = customer.documentNumber,
                Nombre = customer.name,
                ApellidoPaterno = customer.lastName
            });
        }
        
        paginaClientes = new TablePage<Cliente>
        {
            Items = todosLosClientes,
            TotalRecords = results.totalCount
        };
    }
}
```

