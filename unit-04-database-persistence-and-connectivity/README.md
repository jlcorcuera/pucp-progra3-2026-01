# Unit 4: Database Persistence & Connectivity

## Overview
This repository covers the core techniques for connecting an application to a Database Management System, alongside architectural patterns (like DAO) that provide a structured approach to permanently store and retrieve object data. This unit demonstrates how to integrate both Java and C# applications with relational databases hosted in the cloud. The module bridges theoretical database concepts with industry-standard tools, focusing on cloud infrastructure, package management, and database connectivity.

## Key Learning Objectives & Contents

*   **Cloud Infrastructure (AWS):** Students will explore cloud computing concepts such as elasticity and on-demand provisioning. The materials cover how to navigate AWS Academy and provision a highly available MySQL database instance using Amazon Relational Database Service (RDS). This includes configuring Virtual Private Cloud (VPC) security groups and setting up endpoints to allow public access on port 3306.
*   **Project Build & Dependency Management:** This module includes a deep dive into structuring applications and managing dependencies, exploring both Apache Maven for Java and NuGet/Project References for .NET C#.
*   **Database Connectivity:** The repository provides step-by-step implementations of database connections using both the standard JDBC API (Java) and ADO.NET (C#). Students will learn how to integrate MySQL drivers, establish connections, and manage credentials.
*   **Domain Modeling & Object Mapping:** This section applies architectural concepts through the "SoftProg" case study. It demonstrates how to map a relational database schema (Entity-Relationship Diagram) into an Object-Oriented UML Domain Model across multiple languages.
*   **Layered Architecture:** Structuring applications in logical layers (e.g., Domain/Model, Business Logic, DB Manager, and Presentation/Console) to promote modularity, maintainability, and code reuse.

---

## Part 1: Java Implementation

### Tech Stack & Tools
*   **Language:** Java
*   **Build Automation:** Apache Maven
*   **Database:** MySQL
*   **Cloud Provider:** Amazon Web Services (AWS RDS)
*   **Connectivity:** JDBC
*   **IDE:** IntelliJ IDEA

### Directory Structure
The Java projects and database setup resources are structured as follows:

*   [**`db_scripts`**](./db_scripts): Contains the SQL scripts and database backups (like `softprog_backup.sql`) required to provision the `softprog` database schema.
*   [**`java/SoftProgApp`**](./java/SoftProgApp): A foundational Java project demonstrating the basics of database connectivity, resource lifecycle management, and query execution.
*   [**`java/softprog`**](./java/softprog): A comprehensive multi-module Maven application demonstrating a layered architecture (Domain, DAO, DBManager, Business Logic, Presentation) with robust transaction management using `TransactionContext` to ensure ACID compliance.

### Configuring IntelliJ to work properly with Maven

> [!WARNING]
> It is important to delegate the build and run actions to Maven. To do this, follow these steps:

1. Open **Settings** (or **Preferences** on macOS) in IntelliJ IDEA.
2. Navigate to **Build, Execution, Deployment** > **Build Tools** > **Maven** > **Runner**.
3. Check the option **Delegate IDE build/run actions to Maven**.
4. Click **Apply** and then **OK** to save the changes.

### Getting Started
To run the examples in this repository, follow these steps:
1. Configure your AWS Academy account and start the lab environment to access the AWS Console.
2. Create an Amazon RDS instance running MySQL (Community Edition).
3. Configure its inbound security rules to allow traffic from anywhere on port `3306`.
4. Ensure Apache Maven is installed and properly configured in your system's `PATH` variables.
5. Update the connection parameters with your specific AWS RDS endpoint URL, database schema name, username, and password before compiling the project.

### First steps with JDBC and Maven
The `java/SoftProgApp` directory contains a sample multi-module Maven project demonstrating the fundamental concepts of connecting to a MySQL database using JDBC.

#### Adding the JDBC Driver Dependency
Before writing any Java code to connect to the database, the project must be configured with the necessary drivers. Because this project uses Maven, we simply configure the `pom.xml` file (specifically in the `AppTest` module) to seamlessly download and include the `mysql-connector-j` library:

```xml
<dependencies>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.6.0</version>
    </dependency>
</dependencies>
```

#### The JDBC URL Format
Before establishing a connection, the application must construct a Connection String (JDBC URL) that tells the driver exactly where the database is located. In the provided Java files, it follows the standard format: `jdbc:mysql://<host>:<port>/<databaseName>`.
*   `jdbc:mysql://` distinguishes the protocol and specific database engine being used.
*   `<host>` represents the AWS RDS endpoint or the IP address where the server is hosted.
*   `<port>` is the communication port, which defaults to `3306` for MySQL.
*   `<databaseName>` points to the specific database schema you want to connect to (e.g., `softprog`).

Inside the `AppTest` module, you will find three main executables that illustrate different approaches to handling database resources:

*   **`Main.java`**: Demonstrates the classic, manual resource management approach. Here is a breakdown of the specific logic used to connect and retrieve the date:
    *   `Connection connection = null; ...`: We declare the `Connection`, `Statement`, and `ResultSet` variables outside of the `try` block so that their scope survives into the `finally` block where they must be closed.
    *   `connection = DriverManager.getConnection(jdbcUrl, username, password);`: The standard JDBC `DriverManager` explicitly establishes a connection to the database server using the provided URL, user, and password.
    *   `stm = connection.createStatement();`: We ask the newly created connection to instantiate an empty `Statement` object, which acts as a vehicle to send SQL queries to the database.
    *   `rs = stm.executeQuery("select now() + INTERVAL 10 year");`: This line sends an SQL query requesting a date calculation to the MySQL server. It returns an iterable `ResultSet` object containing the data.
    *   `if (rs.next()) {`: The `ResultSet` cursor initially points *before* the first row of data. We must call `.next()` to advance the cursor to the first row so we can read it.
    *   `... rs.getDate(1));`: Since we retrieved one column of information, we extract the data out of the first column (JDBC is 1-indexed) and cast it to a Java Date object.
    *   `finally { ... connection.close(); }`: Because database connections hog network sockets and memory, we use a `try-catch-finally` block to guarantee that `rs.close()`, `stm.close()`, and `connection.close()` are called to prevent resource leaks, regardless of whether an exception crashed the program. Before closing, we explicitly verify if each referencing variable is not `null` (e.g., `if (rs != null)`). This crucial check ensures that if an exception crashes the program *before* a variable actually gets allocated, we don't accidentally run into a `NullPointerException` while attempting to `.close()` it.
*   **`MainTryWithResources.java`**: Implements the same logic but utilizes Java's *try-with-resources* statement (introduced in Java 7). By declaring our `Connection`, `Statement`, and `ResultSet` directly inside the parentheses of the `try(...)` block, Java guarantees that each of these objects will be closed automatically in reverse order of their creation when the block terminates. This modern approach drastically reduces boilerplate code by eliminating the need for a fragile `finally` block and manual `null` checks, provided the classes implement the `AutoCloseable` interface (which JDBC objects natively do).

    Notice in the code snippet below how multiple resources are separated by semicolons, but the **last** resource declaration inside the `try(...)` parentheses does not require a trailing semicolon. Additionally, it is perfectly valid to nest multiple `try-with-resources` blocks inside one another if different database operations need to be isolated or handled in different scopes:
    ```java
    try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
         Statement stm = connection.createStatement();
         ResultSet rs = stm.executeQuery("select now() + ...")) { // <-- No semicolon needed here
        
        // ... execute code
    }
    ```
*   **`MainExampleResultset.java`**: Shows a practical use case by querying a hypothetical table (`categories`) and navigating through the returned `ResultSet` using a `while(rs.next())` loop. It extracts and prints data row by row, demonstrating how to access specific column values using their numeric position (e.g., `rs.getInt(1)`). It is important to note that JDBC column indexes start at `1`, rather than the standard `0`-based indexing customary in most Java data structures.

#### Query Execution and Retrieving Values
To read data from the database in Java, you can use `PreparedStatement` to execute a `SELECT` query and read the results using a `ResultSet`.

```java
try (Connection connection = DBManager.getInstance().getConnection();
     PreparedStatement pstmt = connection.prepareStatement(
         "SELECT id_area, nombre, activa FROM area WHERE activa = ?")) {
    
    // Setting parameter (1-indexed)
    pstmt.setBoolean(1, true);
    
    // Execute the query and obtain a ResultSet
    try (ResultSet rs = pstmt.executeQuery()) {
        System.out.println("Áreas Activas:");
        while (rs.next()) {
            // Retrieve values using column names
            int id = rs.getInt("id_area");
            String nombre = rs.getString("nombre");
            boolean activa = rs.getBoolean("activa");
            
            System.out.println("- ID: " + id + ", Nombre: " + nombre + ", Activa: " + activa);
        }
    }
} catch (SQLException ex) {
    ex.printStackTrace();
}
```

#### Invoking a Stored Procedure with Output Parameters
When calling a stored procedure with JDBC, use the `CallableStatement` interface. If the procedure has `OUT` parameters, you must register them with their corresponding SQL type before executing.

```java
try (Connection connection = DBManager.getInstance().getConnection();
     CallableStatement cs = connection.prepareCall("{call InsertarArea(?, ?)}")) {
    
    // Input parameter (1-indexed)
    cs.setString(1, "Logística");
    
    // Register output parameter configuration (2nd parameter)
    cs.registerOutParameter(2, java.sql.Types.INTEGER);
    
    // Execute the stored procedure
    cs.executeUpdate();
    
    // Read the output parameter value
    int nuevoId = cs.getInt(2);
    System.out.println("Nueva área insertada con ID: " + nuevoId);
    
} catch (SQLException ex) {
    ex.printStackTrace();
}
```

### Transaction Management in Business Logic

In real-world applications, operations often span multiple database queries that must succeed or fail as a single unit of work. The `java/softprog` project demonstrates this through the `TransactionContext` utility and its integration within the Business Logic layer.

#### The `TransactionContext` Utility
Located in the `softprog-db-manager` module under `java/softprog` (`pe.edu.pucp.softprog.dao.transaction.TransactionContext`), this class is responsible for managing database connection lifecycles across different DAO calls. It uses a `ThreadLocal<Connection>` to ensure that all database operations executing within the same thread share a single, unified database connection. 

When a connection is first requested:
*   It retrieves a connection from the `DBManager`.
*   It disables auto-commit (`conn.setAutoCommit(false);`), allowing the application to control exactly when changes are finalized.

#### Usage in the Business Logic Layer
The Business Logic layer (`softprog-business-logic`) acts as the transaction boundary. A prime example is the `SalesOrderBLImpl.create` method, which performs multiple operations: validating stock, verifying employees/customers, saving the order, and updating product stock.

Here is the standard pattern used to ensure data integrity:

```java
public SalesOrder create(SalesOrder salesOrder) throws BusinessLogicException {
    try {
        // 1. Perform various validation and DAO operations 
        // (e.g., productDAO.load, salesOrderDAO.save, productDAO.update)
        
        // 2. If all operations succeed, commit the transaction
        TransactionContext.commit();
    } catch (Exception ex) {
        // 3. If any operation fails or an exception is thrown, rollback the transaction
        TransactionContext.rollback();
        // Handle or rethrow the exception
        throw new BusinessLogicException(ex);
    } finally {
        // 4. Always close the connection and clean up the ThreadLocal reference
        TransactionContext.close();
    }
    return salesOrder;
}
```

By delegating connection management to `TransactionContext`, the DAO layer remains unaware of the broader transaction scope. This allows the Business Logic layer to successfully orchestrate complex, multi-step workflows while maintaining ACID properties, ensuring that if any single step fails, the entire transaction is rolled back.

> [!WARNING]
> **Important DAO Rule:** When using the `TransactionContext`, individual DAO methods **MUST NOT** close the connection after their specific execution. Because the connection is managed by the ThreadLocal and shared across multiple DAO calls within the same transaction, closing it prematurely in a DAO will break the transaction and cause subsequent queries in the Business Logic layer to fail. The responsibility of closing the connection belongs strictly to the Business Logic layer, ideally inside a `finally` block.

---

## Part 2: C# Implementation

### Tech Stack & Tools
*   **Language:** C#
*   **Framework:** .NET
*   **Package Management:** NuGet
*   **Database:** MySQL
*   **Connectivity:** ADO.NET (`MySql.Data`)
*   **IDE:** Visual Studio 2026 Community Edition / Visual Studio Code

### Directory Structure & Projects
The `csharp` folder contains multiple C# implementations (along with educational content) illustrating different database connectivity approaches, provider abstraction, and data migrations using ADO.NET:

1.  [**`csharp/SoftProgSolutionMySQL`**](./csharp/SoftProgSolutionMySQL): A foundational C# solution demonstrating a **single database connection** approach with MySQL.
    *   **`SoftProgDomain`**: A Class Library representing the domain layer, keeping entity logic (`Area`, `Empleado`, `Cliente`, `Producto`) independent of databases and UI.
    *   **`SoftProgDBManager`**: A Class Library implementing a thread-safe Singleton `DBManager` using ADO.NET to establish and dispense `MySqlConnection` objects.
    *   **`SoftProg`**: The main Console Application serving as the system entry point, orchestrating user interactions and loading connection parameters from `appsettings.json`.

2.  [**`csharp/SoftProgSolutionMySQLMSSQL`**](./csharp/SoftProgSolutionMySQLMSSQL): An advanced C# solution showcasing database provider abstraction using the standard ADO.NET `IDbConnection` interface.
    *   Allows the application to run against **either MySQL or Microsoft SQL Server (MSSQL)** depending on the runtime type configured in `appsettings.json`.
    *   Demonstrates how to decouple query execution from specific DB engines, allowing great portability.

3.  [**`csharp/202501_Lab07`**](./csharp/202501_Lab07): A complete solution demonstrating data migration from a **denormalized source** to a **normalized destination** database using two simultaneous connection strings.
    *   Iterates through flattened sales records to normalize them across discrete relational tables (`Sucursal`, `Pelicula`, `Cliente`, `Venta`).
    *   Uses dual ADO.NET connections managed dynamically under a clean layered design.

4.  [**`csharp/Lab06SoftProg`**](./csharp/Lab06SoftProg): A Java-based multi-module Maven project representing Lab 06, included within this workspace for historical context or academic reference.

### Getting Started
To run the C# examples in this repository, follow these steps:
1. Install **Visual Studio 2026 Community Edition** with the following workloads: **.NET desktop development**, **ASP.NET and web development**, and **Data storage and processing**.
2. Configure your AWS RDS instance as described in the general section.
3. Open either `SoftProgSolutionMySQL` or `SoftProgSolutionMySQLMSSQL` in Visual Studio 2026 Community Edition using their respective `.slnx` solution definition files.
4. Verify the necessary inter-project dependencies. In the Solution Explorer, ensure the startup Console Application has references to its domain, data access, or DB manager projects.
5. Ensure the startup console application (`SoftProg`) is set as the **Startup Project** (Right-click the project -> **Set as Startup Project**).
6. Verify the required NuGet packages are installed. Key packages include:
   * `MySql.Data` (for MySQL connectivity)
   * `Microsoft.Data.SqlClient` (for MSSQL connectivity in the MSSQL solution)
   * `Microsoft.Extensions.Configuration`
   * `Microsoft.Extensions.Configuration.Json`
   * `Microsoft.Extensions.Configuration.FileExtensions`
7. Update the `appsettings.json` file in the main application project with your specific connection string(s). Make sure to set its **"Copy to Output Directory"** property to **"Copy if newer"** so the configuration file is included in the build output.

### Connecting to MySQL with ADO.NET

In the C# ecosystem, ADO.NET provides the core classes for database access. We use the `MySql.Data` provider to interact with our MySQL database.

#### The `appsettings.json` Configuration
Configuration is typically managed through an `appsettings.json` file. This format allows you to securely store connection strings and other runtime parameters:

```json
{
  "ConnectionStrings": {
    "MySqlConnection": "Server=localhost;Port=3306;Database=softprog;Uid=root;Pwd=1234;"
  }
}
```

#### Initializing the Database Connection
The console application reads this configuration using `Microsoft.Extensions.Configuration` and initializes the database manager Singleton instance:

```csharp
using System;
using System.IO;
using Microsoft.Extensions.Configuration;
using MySql.Data.MySqlClient;
using SoftProgDBManager;

class Program
{
    static void Main(string[] args)
    {
        // 1. Load configuration from appsettings.json
        IConfiguration configuration = new ConfigurationBuilder()
            .SetBasePath(Directory.GetCurrentDirectory())
            .AddJsonFile("appsettings.json")
            .Build();

        string connectionString = configuration.GetConnectionString("MySqlConnection");

        // 2. Initialize the Singleton DBManager
        DBManager.Initialize(connectionString);
        
        // ... application logic
    }
}
```

### Data Insertion Example

Once the `DBManager` is initialized, you can use the `MySqlConnection` and `MySqlCommand` classes to execute SQL queries. 

The following example demonstrates how to insert a new record into the `area` table using parameterized queries, which protect against SQL injection vulnerabilities:

```csharp
using (MySqlConnection connection = DBManager.Instance.GetConnection())
{
    connection.Open();
    string sql = "INSERT INTO area(nombre, activa) VALUES (@nombre, @activa)";
    
    using (MySqlCommand command = new MySqlCommand(sql, connection))
    {
        // Adding parameterized values
        command.Parameters.AddWithValue("@nombre", "Recursos Humanos");
        command.Parameters.AddWithValue("@activa", true);
        
        // Execute the query
        int filasAfectadas = command.ExecuteNonQuery();
        Console.WriteLine("Filas insertadas: " + filasAfectadas);
    }
}
```

Notice the use of the `using` statement. Similar to Java's *try-with-resources*, this guarantees that objects implementing the `IDisposable` interface (like `MySqlConnection` and `MySqlCommand`) are automatically and correctly disposed of when the execution block terminates, preventing resource leaks.

### Query Execution and Retrieving Values

To read data from the database, you can use `MySqlCommand` to execute a `SELECT` query and read the results using a `MySqlDataReader`. The reader provides methods to retrieve data row by row efficiently.

```csharp
using (MySqlConnection connection = DBManager.Instance.GetConnection())
{
    connection.Open();
    string sql = "SELECT id_area, nombre, activa FROM area WHERE activa = @activa";
    
    using (MySqlCommand command = new MySqlCommand(sql, connection))
    {
        command.Parameters.AddWithValue("@activa", true);
        
        // Execute the query and obtain a reader
        using (MySqlDataReader reader = command.ExecuteReader())
        {
            Console.WriteLine("Áreas Activas:");
            while (reader.Read())
            {
                // Retrieve values using column names (or numeric indices)
                int id = reader.GetInt32("id_area");
                string nombre = reader.GetString("nombre");
                bool activa = reader.GetBoolean("activa");
                
                Console.WriteLine($"- ID: {id}, Nombre: {nombre}, Activa: {activa}");
            }
        }
    }
}
```

### Invoking a Stored Procedure with Output Parameters

When calling a stored procedure, you must set the `CommandType` to `StoredProcedure`. If the procedure includes `OUT` parameters (such as returning an auto-generated identity ID), you must configure the parameter's `Direction` accordingly.

```csharp
using (MySqlConnection connection = DBManager.Instance.GetConnection())
{
    connection.Open();
    
    using (MySqlCommand command = new MySqlCommand("InsertarArea", connection))
    {
        command.CommandType = System.Data.CommandType.StoredProcedure;
        
        // Input parameter
        command.Parameters.AddWithValue("@_nombre", "Logística");
        
        // Output parameter configuration
        MySqlParameter outParam = new MySqlParameter("@_id_area", MySqlDbType.Int32);
        outParam.Direction = System.Data.ParameterDirection.Output;
        command.Parameters.Add(outParam);
        
        // Execute the stored procedure
        command.ExecuteNonQuery();
        
        // Read the output parameter value
        int nuevoId = Convert.ToInt32(outParam.Value);
        Console.WriteLine($"Nueva área insertada con ID: {nuevoId}");
    }
}
```

---

### Connecting to MySQL/MSSQL with ADO.NET

To build a truly portable and robust application, you should avoid hardcoding provider-specific classes like `MySqlConnection` or `SqlConnection` inside your Data Access Objects (DAOs). Instead, you can write database-agnostic code by leveraging standard ADO.NET interfaces: **`IDbConnection`**, **`IDbCommand`**, **`IDataReader`**, and **`IDbDataParameter`**. 

This approach ensures that you can switch the underlying database engine (e.g., from MySQL to Microsoft SQL Server) purely through configuration changes without rewriting any data access logic.

#### Unified `appsettings.json` Configuration
By externalizing the database type and defining separate connection strings, you can dynamically instantiate the appropriate connection at runtime:

```json
{
  "ConnectionStrings": {
    "Type": "MSSQLServer",
    "MySqlConnection": "Server=my-mysql-endpoint.rds.amazonaws.com;Port=3306;Database=softprog;Uid=admin;Pwd=password;",
    "MSSQLServerConnection": "Server=my-sqlserver-endpoint.rds.amazonaws.com;Database=softprog;Uid=admin;Pwd=password;TrustServerCertificate=True"
  }
}
```

The thread-safe `DBManager` Singleton reads this configuration and returns the generic `IDbConnection` interface:

```csharp
public IDbConnection GetConnection()
{
    if (_dbType == "MySQL")
    {
        return new MySqlConnection(_connectionString);
    }
    return new SqlConnection(_connectionStringMSSQLServer);
}
```

#### Database-Agnostic Data Insertion
To insert data without tying your code to a specific provider, use `IDbConnection` and `IDbCommand`. Since you cannot directly instantiate parameter objects without referencing a provider (like `new MySqlParameter()`), you must use the command's factory method `CreateParameter()` to instantiate them dynamically:

```csharp
using (IDbConnection connection = DBManager.Instance.GetConnection())
{
    connection.Open();
    string sql = "INSERT INTO area(nombre, activa) VALUES (@nombre, @activa)";
    
    using (IDbCommand command = connection.CreateCommand())
    {
        command.CommandText = sql;
        
        // 1. Create a generic parameter using the command factory
        IDbDataParameter paramNombre = command.CreateParameter();
        paramNombre.ParameterName = "@nombre";
        paramNombre.Value = "Recursos Humanos";
        command.Parameters.Add(paramNombre);
        
        // 2. Create another parameter
        IDbDataParameter paramActiva = command.CreateParameter();
        paramActiva.ParameterName = "@activa";
        paramActiva.Value = true;
        command.Parameters.Add(paramActiva);
        
        // Execute the query
        int filasAfectadas = command.ExecuteNonQuery();
        Console.WriteLine("Filas insertadas: " + filasAfectadas);
    }
}
```

#### Database-Agnostic Query Execution
Reading data in a provider-agnostic manner relies on `IDataReader`. You can access values using zero-based indices or type-safe getter methods:

```csharp
using (IDbConnection connection = DBManager.Instance.GetConnection())
{
    connection.Open();
    string sql = "SELECT id_area, nombre FROM area WHERE activa = @activa";
    
    using (IDbCommand command = connection.CreateCommand())
    {
        command.CommandText = sql;
        
        IDbDataParameter paramActiva = command.CreateParameter();
        paramActiva.ParameterName = "@activa";
        paramActiva.Value = true;
        command.Parameters.Add(paramActiva);
        
        // Execute the query and obtain the generic IDataReader
        using (IDataReader reader = command.ExecuteReader())
        {
            Console.WriteLine("Áreas Activas:");
            while (reader.Read())
            {
                // Access values using zero-based indices
                int id = reader.GetInt32(0);
                string nombre = reader.GetString(1);
                
                Console.WriteLine($"- ID: {id}, Nombre: {nombre}");
            }
        }
    }
}
```

#### Database-Agnostic Stored Procedure Execution
Invoking stored procedures that utilize output parameters (e.g., to retrieve auto-generated IDs) follows a similar database-independent structure. We configure the parameters with their appropriate type and direction:

```csharp
using (IDbConnection connection = DBManager.Instance.GetConnection())
{
    connection.Open();
    
    using (IDbCommand command = connection.CreateCommand())
    {
        // 1. Set command text and type
        command.CommandText = "INSERTAR_AREA";
        command.CommandType = CommandType.StoredProcedure;
        
        // 2. Configure OUTPUT Parameter using IRef/IDbDataParameter
        IDbDataParameter paramIdArea = command.CreateParameter();
        paramIdArea.ParameterName = "@_id_area";
        paramIdArea.DbType = DbType.Int32;
        paramIdArea.Direction = ParameterDirection.Output;
        command.Parameters.Add(paramIdArea);
        
        // 3. Configure Input Parameter
        IDbDataParameter paramNombre = command.CreateParameter();
        paramNombre.ParameterName = "@_nombre";
        paramNombre.DbType = DbType.String;
        paramNombre.Size = 30;
        paramNombre.Value = "Logística";
        command.Parameters.Add(paramNombre);
        
        // Execute the procedure
        command.ExecuteNonQuery();
        
        // 4. Retrieve the newly generated output ID safely
        int nuevoId = Convert.ToInt32(paramIdArea.Value);
        Console.WriteLine($"Nueva área insertada con ID: {nuevoId}");
    }
}
```

> [!TIP]
> **Parameter Prefixes across Dialects:** While MySQL commonly accepts parameters with `@` prefixes inside stored procedures or direct text commands, Microsoft SQL Server also uses `@`. In DB-agnostic applications, maintain unified names in your code and adjust stored procedures in each target database schema to share parameter names (such as `@_nombre` or `@nombre`) so that your code remains 100% database-agnostic.

