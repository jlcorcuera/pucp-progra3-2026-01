# Unit 6: Graphical User Interface (GUI) Development

This unit introduces the principles and practices of building modern, interactive web-based user interfaces using component-driven frameworks. Students will progress from foundational GUI concepts — such as event-driven programming, state management, and component lifecycles — toward advanced topics including authentication and authorization workflows, role-based access control, and the design of generic, reusable UI components. Practical work is grounded in **Blazor Server (.NET 10)**, a server-side rendering model in which UI interactions are processed on the server and propagated to the browser through a persistent WebSocket (SignalR) connection, enabling rich interactivity without writing JavaScript. Through hands-on projects, students will examine how architectural decisions at the framework level — such as separating HTTP cookie operations from the Blazor circuit, or applying reference-equality checks to synchronize paginated component state — directly influence correctness, security, and maintainability of real-world applications.

---

## Projects

### 1. 🖥️ SoftProg-MasterDetail — Blazor Web Application

**Folder:** [`SoftProg-MasterDetail/`](SoftProg-MasterDetail/)

An interactive web application built on **Blazor Server (.NET 10)**. It is designed as an educational reference for undergraduate software engineering students studying architectural design patterns, web security, and component reuse.

#### 🛡️ Security & Roles Subsystem

The application implements an HTTP cookie-based authentication scheme, integrating identity resolution directly with the Blazor Server circuit lifetime, while outsourcing cookie operations to independent Minimal API endpoints:

- **Cookie-Based Authentication**: Resolves identity at the HTTP pipeline level to ensure seamless compatibility with server-side pre-rendering and prevent concurrency issues.
- **Minimal API Integration**: Orchestrates session establishment (`POST /auth/login`) and termination (`GET /auth/logout`) before establishing the Blazor WebSocket (SignalR) connection.
- **Access Control Policies**: Restricts entire page components using the `@attribute [Authorize]` directive and conditionally updates UI structures via `<AuthorizeView>`.

> 📖 See [Security Configuration — Technical Reference](SoftProg-MasterDetail/README_Security_Configuration.md) for a detailed walkthrough of the authentication flow, middleware registration, and code examples.

#### 📊 Generic Table Pagination Subsystem

To handle large datasets efficiently, the application incorporates a generic presentation component:

- **Unidirectional Data Flow**: The generic `<PaginatedTable>` delegates collection slicing, filtering, and query logic (`Skip`/`Take`) to the parent container using event callbacks.
- **Generic Type Resolution (`@typeparam TItem`)**: Dynamically processes collections of arbitrary entity types (e.g., Clients, Products, Orders) through custom template fragments (`RenderFragment`).
- **State Synchronization (Reference-Equality)**: Evaluates model reference equality to automatically reset the active page index to 1 when external search criteria are cleared or updated.

> 📖 See [Table Pagination — Technical Reference](SoftProg-MasterDetail/README_Table_Pagination.md) for implementation patterns, performance details, and step-by-step generic integration instructions.

#### 🔑 Testing Credentials & Access Control

| Username | Password | Role | Display Name | Key Permissions |
| :--- | :--- | :--- | :--- | :--- |
| **admin** | `admin` | **Admin** | Principal Administrator | Full access to all sections and sales order management. |
| **employee** | `employee` | **Employee** | Sales Employee | Creation of sales orders (blocked from managing them). |

> [!IMPORTANT]
> These credentials are hardcoded strictly for pedagogical purposes. In production environments, credentials must be stored securely using cryptographic password hashing (e.g., ASP.NET Core Identity with BCrypt).

---

### 2. ☕ SoftProg — Java Web Services *(Coming Soon)*

**Folder:** [`softprog/`](softprog/)

A multi-tier modular **Java enterprise application** that will expose a set of **web services** to be consumed by the Blazor front-end and other clients. It is built as a multi-module **Apache Maven** project and demonstrates software architecture best practices by strictly separating concerns across layers.

#### Project Modules

```
softprog/
├── softprog-model           → Core domain entities
├── softprog-db-manager      → JDBC connection & transaction management
├── softprog-dao             → Data Access Object (CRUD) layer
├── softprog-business-logic  → Business rules & transaction orchestration
└── softprog-test            → Integration & entry-point test drivers
```

| Module | Responsibility |
| :--- | :--- |
| `softprog-model` | Domain entities: `Area`, `Customer`, `Employee`, `Product`, `UserAccount`, `SalesOrder`, `SalesOrderLine` |
| `softprog-db-manager` | JDBC lifecycle management (`DBManager`) and `TransactionContext` |
| `softprog-dao` | SQL CRUD operations: `AreaDAOImpl`, `CustomerDAOImpl`, `EmployeeDAOImpl`, `ProductDAOImpl`, `SalesOrderDAOImpl` |
| `softprog-business-logic` | Business logic orchestration: `SalesOrderBLImpl`, `BusinessLogicException` |
| `softprog-test` | Test drivers: `TestAreaDAOMain`, `TestProductDAOMain`, `TestSalesOrderBLMain` |

> [!NOTE]
> The web service layer (REST endpoints) is currently under development and will be added in a future iteration of this unit.

> 📖 See [SoftProg README](softprog/README.md) for prerequisites, database configuration, and build instructions.

---

## Educational Context

Both projects belong to the **Programming 3 (2026-01)** course at PUCP and provide hands-on exposure to:

- Component-based GUI development with Blazor Server.
- Authentication, authorization, and role-based access control.
- Generic and reusable UI components (pagination, master-detail views).
- Multi-tier Java application architecture (Model → DAO → Business Logic).
- JDBC-based data persistence and transaction management.
- RESTful web service design *(coming soon)*.
