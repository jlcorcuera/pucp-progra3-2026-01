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
