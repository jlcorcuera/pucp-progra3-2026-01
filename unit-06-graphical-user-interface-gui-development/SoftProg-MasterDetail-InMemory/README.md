# SoftProg WebApp — Architectural Overview

This repository contains **SoftProg WebApp**, an interactive web application built on Blazor Server (.NET 10). It is designed as an educational reference for undergraduate software engineering students studying architectural design patterns, web security, and component reuse. 

Below is an overview of the two primary subsystems implemented in the project. For in-depth code explanations and step-by-step guides, consult the linked technical references.

---

## 🔑 Testing Credentials & Access Control

For authorization testing and role verification, the application includes two preconfigured user accounts with distinct domain roles:

| Username | Password | Role | Display Name | Key Permissions |
| :--- | :--- | :--- | :--- | :--- |
| **admin** | `admin` | **Admin** | Principal Administrator | Full access to all sections and sales order management. |
| **employee** | `employee` | **Employee** | Sales Employee | Creation of sales orders (blocked from managing them). |

> [!IMPORTANT]
> These credentials are hardcoded strictly for pedagogical purposes. In production environments, credentials must be stored securely using cryptographic password hashing (e.g., ASP.NET Core Identity with BCrypt).

---

## 🛡️ Security & Roles Subsystem

The application utilizes an HTTP cookie-based authentication scheme, integrating identity resolution directly with the Blazor Server circuit lifetime, while outsourcing cookie operations to independent Minimal API endpoints:

- **Cookie-Based Authentication**: Resolves identity at the HTTP pipeline level to ensure seamless compatibility with server-side pre-rendering and prevent concurrency issues.
- **Minimal API Integration**: Orchestrates session establishment (`POST /auth/login`) and termination (`GET /auth/logout`) before establishing the Blazor WebSocket (SignalR) connection.
- **Access Control Policies**: Restricts entire page components using the `@attribute [Authorize]` directive and conditionally updates UI structures via `<AuthorizeView>`.

📖 **For a detailed description of authentication flow, middleware registration, and code walkthroughs, see:**  
👉 **[Security Configuration — Technical Reference](README_Security_Configuration.md)**

---

## 📊 Generic Table Pagination Subsystem

To handle large datasets efficiently, the application incorporates a generic presentation component:

- **Unidirectional Data Flow**: The generic `<PaginatedTable>` delegates collection slicing, filtering, and query logic (`Skip`/`Take`) to the parent container using event callbacks.
- **Generic Type Resolution (`@typeparam TItem`)**: Dynamically processes collections of arbitrary entity types (e.g., Clients, Products, Orders) through custom template fragments (`RenderFragment`).
- **State Synchronization (Reference-Equality)**: Evaluates model reference equality to automatically reset the active page index to 1 when external search criteria are cleared or updated.

📖 **For implementation patterns, performance details, and step-by-step generic integration instructions, see:**  
👉 **[Table Pagination — Technical Reference](README_Table_Pagination.md)**
