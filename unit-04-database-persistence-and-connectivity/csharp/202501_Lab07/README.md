# Lab 07: Denormalized to Normalized Database Migration

This project demonstrates a C# application designed to migrate data from a denormalized source database into a fully normalized destination database. It serves as a practical example of handling multiple simultaneous database connections using ADO.NET and the Singleton pattern within a layered architecture.

## Overview

The core objective of this application is to read flattened, denormalized data (representing sales) and properly distribute it across normalized relational tables (`Sucursal`, `Pelicula`, `Cliente`, and `Venta`).

### Key Features
* **Dual Database Connections**: The application connects to two distinct MySQL databases concurrently:
  * **Source**: Contains the denormalized data (`VentaDNDAO`).
  * **Destination**: Contains the normalized schema (`SucursalDAO`, `PeliculaDAO`, `ClienteDAO`, `VentaDAO`).
* **Data Normalization Logic**: The business logic (`MigratorBL`) iterates through the denormalized records and performs "Upsert" style operations:
  * It checks if a `Sucursal`, `Pelicula`, or `Cliente` already exists in the destination database.
  * If it does not exist, it inserts the new entity and retrieves its newly generated ID.
  * Finally, it creates the `Venta` (Sale) record linking all the normalized foreign keys together.
* **Layered Architecture**: The solution is cleanly separated into Domain (`Lab07-domain`), Data Access (`Lab07-dao`), Business Logic (`Lab07-business-logic`), Infrastructure/DB Management (`Lab07-db-manager`), and the Console Application (`Lab07-app`).

## Configuration

Before running the application, you must configure the database connection strings in the `appsettings.json` file located in the `Lab07-app` project:

```json
{
  "ConnectionStrings": {
    "MySqlConnectionSource": "Server=localhost;Port=3306;Database=denormalized_db;Uid=root;Pwd=password;",
    "MySqlConnectionDestination": "Server=localhost;Port=3306;Database=normalized_db;Uid=root;Pwd=password;"
  }
}
```

Make sure that `appsettings.json` is configured to "Copy to Output Directory" (e.g., "Copy if newer") so the executable can read the settings at runtime.

## Execution Flow

1. The `Program.cs` reads the connection strings and initializes the `DBManager` with both the source and destination credentials.
2. The `MigratorBL.run()` method is invoked.
3. The `VentaDNDAO` reads all records from the source database as Data Transfer Objects (`VentaDNDTO`).
4. The migration loop begins, distributing data across the destination tables.
