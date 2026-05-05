# SoftProgSolution: Single Database Connection Example

This project serves as a foundational C# example demonstrating how to connect an application to a MySQL database using ADO.NET within a standard layered architecture. 

## Overview

Unlike more complex setups that might handle multiple databases concurrently, `SoftProgSolution` focuses on establishing and managing a **single, unified database connection**. It introduces core concepts such as the Singleton design pattern for connection management and parameterized queries to prevent SQL injection.

### Key Features
* **Single Connection Management**: The `SoftProgDBManager` class library implements a `DBManager` Singleton. This ensures that the application uses a centralized configuration to generate `MySqlConnection` objects.
* **ADO.NET Integration**: Demonstrates the direct use of the `MySql.Data` provider to execute SQL queries (e.g., `INSERT` operations).
* **Configuration via `appsettings.json`**: Shows how to securely externalize the database connection string rather than hardcoding it into the application logic, using the `Microsoft.Extensions.Configuration` packages.
* **Layered Architecture Structure**: The solution separates concerns into logical projects:
  * `SoftProgDomain`: For domain entities (`Area`, etc.).
  * `SoftProgDBManager`: For handling the DB connection logic.
  * `SoftProg`: The main Console Application entry point.

## Configuration

Before running the application, you must configure the MySQL database connection string in the `appsettings.json` file located in the `SoftProg` (Console Application) project:

```json
{
  "ConnectionStrings": {
    "MySqlConnection": "Server=localhost;Port=3306;Database=softprog;Uid=root;Pwd=password;"
  }
}
```

> **Note:** Ensure that the `appsettings.json` file's properties are set to **"Copy to Output Directory"** -> **"Copy if newer"** so that the compiled executable can read the configuration at runtime.

## Execution Flow

1. The `Program.cs` `Main` method starts by reading `appsettings.json` to fetch the `MySqlConnection` string.
2. It calls `DBManager.Initialize(connectionString)` to configure the Singleton instance.
3. It requests a connection using `DBManager.Instance.GetConnection()`.
4. It opens the connection and executes a parameterized `INSERT` query into the `area` table.
5. Finally, it outputs the number of rows affected to the console. The `using` block ensures the connection is properly closed and disposed of.
