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
*   **`db_scripts`**: Contains the SQL scripts and database backups required to provision the `softprog` database schema.
*   **`SoftProgApp`**: A foundational Java project demonstrating the basics of database connectivity and query execution.
*   [**`softprog`**](./softprog): A comprehensive project showcasing the implementation of business logic, including robust transaction handling and structured exception propagation across architectural layers.

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
The `SoftProgApp` directory contains a sample multi-module Maven project demonstrating the fundamental concepts of connecting to a MySQL database using JDBC.

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

In real-world applications, operations often span multiple database queries that must succeed or fail as a single unit of work. The `softprog` project demonstrates this through the `TransactionContext` utility and its integration within the Business Logic layer.

#### The `TransactionContext` Utility
Located in the `softprog-db-manager` module (`pe.edu.pucp.softprog.dao.transaction.TransactionContext`), this class is responsible for managing database connection lifecycles across different DAO calls. It uses a `ThreadLocal<Connection>` to ensure that all database operations executing within the same thread share a single, unified database connection. 

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
This repository includes two distinct C# implementations demonstrating different connectivity approaches. Both use a standard layered architecture organized within a `.sln` (Solution) file containing multiple Class Library projects:

1.  [**`SoftProgSolution`**](./SoftProgSolution): A foundational C# project demonstrating a **single database connection** approach.
    *   **`SoftProgDomain`**: A Class Library representing the domain layer. Contains entities such as `Area`, `Empleado`, `Cliente`, `Producto` organized logically into namespaces (e.g., `SoftProgDomain.RRHH`). This layer must be independent of infrastructure and UI concerns.
    *   **`SoftProgDBManager`**: A Class Library managing the database connection. Utilizes the Singleton pattern (`DBManager`) and ADO.NET to establish connections to the MySQL database.
    *   **`SoftProg`**: The Console Application project serving as the entry point (`Main()`) of the application. It orchestrates the system and reads configurations from `appsettings.json`.

2.  [**`202501_Lab07`**](./202501_Lab07): An advanced C# project demonstrating how to handle **two connections to different databases** simultaneously.
    *   **`Lab07-domain`**: The domain layer containing entities such as `Cliente`, `Pelicula`, `Sucursal`, and `Venta`.
    *   **`Lab07-db-manager`**: The infrastructure layer responsible for managing multiple connection strings and configuring ADO.NET connections to interact with two distinct databases.
    *   **`Lab07-dao`** and **`Lab07-business-logic`**: Layers that encapsulate data access and business rules, managing queries and operations across the two database environments.
    *   **`Lab07-app`**: The Console Application project that coordinates the logic and reads multiple database configurations from `appsettings.json`.

### Getting Started
To run the C# examples in this repository, follow these steps:
1. Install **Visual Studio 2026 Community Edition** with the following workloads: **.NET desktop development**, **ASP.NET and web development**, and **Data storage and processing**.
2. Configure your AWS RDS instance as described in the general section.
3. Open `SoftProgSolution` in Visual Studio 2026 Community Edition.
4. Set the necessary inter-project dependencies. Right-click on the `SoftProg` project -> **Add** -> **Project Reference...** and ensure references to `SoftProgDomain` and `SoftProgDBManager` are added.
5. Ensure the `SoftProg` console application is set as the **Startup Project** (Right-click -> "Set as Startup Project").
6. Verify the required NuGet packages are installed. You will need:
   * `MySql.Data` (for MySQL connectivity)
   * `Microsoft.Extensions.Configuration`
   * `Microsoft.Extensions.Configuration.Json`
   * `Microsoft.Extensions.Configuration.FileExtensions`
7. Update the `appsettings.json` file in the `SoftProg` project with your MySQL connection string. Make sure to set its **"Copy to Output Directory"** property to **"Copy if newer"** so the configuration file is included in the build output.

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
