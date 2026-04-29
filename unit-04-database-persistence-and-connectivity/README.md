# Unit 4: Database Persistence & Connectivity

## Overview
This repository covers the core techniques for connecting an application to a Database Management System, alongside architectural patterns (like DAO) that provide a structured approach to permanently store and retrieve object data. This unit demonstrates how to integrate Java applications with relational databases hosted in the cloud. The module bridges theoretical database concepts with industry-standard tools, focusing on cloud infrastructure, build automation, and database connectivity.

## Key Learning Objectives & Contents

*   **Cloud Infrastructure (AWS):** Students will explore cloud computing concepts such as elasticity and on-demand provisioning. The materials cover how to navigate AWS Academy and provision a highly available MySQL database instance using Amazon Relational Database Service (RDS). This includes configuring Virtual Private Cloud (VPC) security groups and setting up endpoints to allow public access on port 3306.
*   **Project Build Management with Apache Maven:** This module includes a deep dive into structuring Java applications using Maven, which is based on the Project Object Model (POM). Topics include managing multi-module projects, downloading dependencies from the Maven Central Repository, and utilizing the standard build lifecycle phases like `clean`, `compile`, `package`, and `install`.
*   **Java Database Connectivity (JDBC):** The repository provides step-by-step implementation of database connections using the standard JDBC API. Students will learn how to integrate the `mysql-connector-j` driver, establish connections using the `DriverManager`, and manage credentials and parameters to connect to the database.
*   **Domain Modeling & Object Mapping:** This section applies architectural concepts through the "SoftProg" case study. It demonstrates how to map a relational database schema (Entity-Relationship Diagram) into an Object-Oriented UML Domain Model, dealing with entities such as Products, Clients, Employees, and Sales Orders.

## Tech Stack & Tools
*   **Language:** Java
*   **Build Automation:** Apache Maven
*   **Database:** MySQL
*   **Cloud Provider:** Amazon Web Services (AWS RDS)
*   **Connectivity:** JDBC
*   **IDE:** IntelliJ IDEA

## Directory Structure
*   **`db_scripts`**: Contains the SQL scripts and database backups required to provision the `softprog` database schema.
*   **`SoftProgApp`**: A foundational Java project demonstrating the basics of database connectivity and query execution.
*   [**`softprog`**](./softprog): A comprehensive project showcasing the implementation of business logic, including robust transaction handling and structured exception propagation across architectural layers.

## Configuring IntelliJ to work properly with Maven

> [!WARNING]
> It is important to delegate the build and run actions to Maven. To do this, follow these steps:

1. Open **Settings** (or **Preferences** on macOS) in IntelliJ IDEA.
2. Navigate to **Build, Execution, Deployment** > **Build Tools** > **Maven** > **Runner**.
3. Check the option **Delegate IDE build/run actions to Maven**.
4. Click **Apply** and then **OK** to save the changes.

## Getting Started
To run the examples in this repository, follow these steps:
1. Configure your AWS Academy account and start the lab environment to access the AWS Console.
2. Create an Amazon RDS instance running MySQL (Community Edition).
3. Configure its inbound security rules to allow traffic from anywhere on port `3306`.
4. Ensure Apache Maven is installed and properly configured in your system's `PATH` variables.
5. Update the connection parameters with your specific AWS RDS endpoint URL, database schema name, username, and password before compiling the project.

## First steps with JDBC and Maven
The `SoftProgApp` directory contains a sample multi-module Maven project demonstrating the fundamental concepts of connecting to a MySQL database using JDBC.

### Adding the JDBC Driver Dependency
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

### The JDBC URL Format
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

## Transaction Management in Business Logic

In real-world applications, operations often span multiple database queries that must succeed or fail as a single unit of work. The `softprog` project demonstrates this through the `TransactionContext` utility and its integration within the Business Logic layer.

### The `TransactionContext` Utility
Located in the `softprog-db-manager` module (`pe.edu.pucp.softprog.dao.transaction.TransactionContext`), this class is responsible for managing database connection lifecycles across different DAO calls. It uses a `ThreadLocal<Connection>` to ensure that all database operations executing within the same thread share a single, unified database connection. 

When a connection is first requested:
*   It retrieves a connection from the `DBManager`.
*   It disables auto-commit (`conn.setAutoCommit(false);`), allowing the application to control exactly when changes are finalized.

### Usage in the Business Logic Layer
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
