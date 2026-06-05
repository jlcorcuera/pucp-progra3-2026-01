# Security Configuration — Technical Reference

This document provides a comprehensive technical description of the security architecture implemented in **SoftProg WebApp**, a Blazor Interactive Server application built on .NET 8. It is intended for undergraduate students enrolled in software engineering or web development courses, and assumes familiarity with C#, the ASP.NET Core request pipeline, and basic concepts of HTTP authentication.

---

## 🗂️ Security File Structure

All security-related artifacts are organized into two dedicated locations within the project, following the principle of **separation of concerns**:

```
SoftProg-WebApp/
│
├── Security/                               ← Server-side authentication logic
│   ├── AuthExtensions.cs                   ← Service registration (extension method)
│   └── AuthEndpoints.cs                    ← HTTP login / logout endpoints
│
└── Components/
    ├── Security/                           ← Razor security components
    │   ├── AccessDenied.razor              ← Reusable "Access Denied" UI component
    │   └── RedirectToLogin.razor           ← Redirect handler for unauthenticated users
    ├── Pages/
    │   └── AccessDenied.razor              ← Routable page at /access-denied (used by middleware)
    └── Routes.razor                        ← Central router with authorization logic
```

> [!NOTE]
> Centralizing security artifacts in a dedicated `Security/` folder improves discoverability and maintainability. Any developer working on access control can locate all relevant logic in a single, predictable location.

---

## 🔑 Predefined User Accounts

For demonstration and testing purposes, the application includes two hardcoded user accounts with distinct roles:

| Username | Password | Role | Display Name |
|:---|:---|:---|:---|
| `admin` | `admin` | **Admin** | Principal Administrator |
| `employee` | `employee` | **Employee** | Sales Employee |

> [!IMPORTANT]
> In a production environment, credentials must **never** be hardcoded. User accounts should be stored in a database with hashed passwords (e.g., using BCrypt or ASP.NET Core Identity). The approach used here is strictly for educational purposes.

---

## 🛠️ System Architecture — End-to-End Request Flow

The following diagram illustrates the complete flow of a user request through the security layers of the application:

```text
[ User submits credentials ]
        │
        ▼  POST /auth/login  (standard HTML form submission)
[ AuthEndpoints.cs ]  ──► Validates credentials → builds ClaimsPrincipal → SignInAsync
        │
        ▼  Encrypted HTTP cookie issued to the browser
[ ASP.NET Core Middleware ]  ──► UseAuthentication / UseAuthorization
        │
        ▼  User navigates to a protected page
[ Routes.razor ]  ──► AuthorizeRouteView evaluates the page's [Authorize] attribute
        │
        ├── ✅ Authenticated + correct role  ──► Page is rendered
        │
        ├── ❌ Authenticated, insufficient role
        │       ├── Blazor (internal navigation)  ──► <AccessDenied />
        │       └── HTTP Middleware (direct URL access)  ──► /access-denied
        │
        └── ❌ Not authenticated
                ├── Blazor (internal navigation)  ──► <RedirectToLogin />
                └── HTTP Middleware  ──► / (login screen)
```

This layered design ensures that access control is enforced at **two independent levels**: the HTTP middleware pipeline (server-side) and the Blazor component router (client-side). Both layers must be aligned to prevent security gaps.

---

## 📋 Implementation Walkthrough

### Step 1 — Registering Authentication Services

**File:** [`Security/AuthExtensions.cs`](SoftProg-WebApp/Security/AuthExtensions.cs)

An **extension method** named `AddCookieAuth()` is defined on `IServiceCollection` to encapsulate all authentication-related service registrations. This pattern keeps `Program.cs` concise and makes the authentication configuration cohesive and easy to locate.

```csharp
public static IServiceCollection AddCookieAuth(this IServiceCollection services)
{
    services
        .AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
        .AddCookie(options =>
        {
            options.LoginPath = "/";                       // Redirect here if not authenticated
            options.AccessDeniedPath = "/access-denied";   // Redirect here if authenticated but unauthorized
            options.SlidingExpiration = true;
            options.ExpireTimeSpan = TimeSpan.FromHours(8);
        });

    services.AddAuthorization();
    services.AddCascadingAuthenticationState();  // Propagates auth state as a cascading parameter
    services.AddHttpContextAccessor();

    return services;
}
```

**Key design decisions explained:**

> **Why `AddCascadingAuthenticationState()`?**  
> In Blazor Server, authentication state is exposed to components through a `CascadingParameter`. Without this registration, built-in components such as `<AuthorizeView>` and `<AuthorizeRouteView>` would not receive the current authenticated user and would be unable to evaluate access control rules.

> **Why `LoginPath = "/"`?**  
> The login form is rendered at the root path `/`. When the middleware detects an unauthenticated request to a protected resource, it automatically redirects the user to this path.

> **Why `AccessDeniedPath = "/access-denied"`?**  
> This path is distinct from `LoginPath`. It is invoked when a user **is authenticated** but **lacks the required role** to access a resource. Directing these two scenarios to different destinations allows the application to display contextually appropriate feedback to the user.

---

### Step 2 — Configuring the Middleware Pipeline

**File:** [`Program.cs`](SoftProg-WebApp/Program.cs)

```csharp
using SoftProg_WebApp.Security;

builder.Services.AddCookieAuth();       // Register all authentication services

app.UseAuthentication();                // Read the cookie and populate HttpContext.User
app.UseAuthorization();                 // Evaluate access policies against HttpContext.User

app.MapAuthEndpoints();                 // Register POST /auth/login and GET /auth/logout
```

> [!IMPORTANT]
> **Middleware ordering is critical.** `UseAuthentication()` must be called before `UseAuthorization()`. Both must be placed before `UseAntiforgery()` and before mapping Razor components. Incorrect ordering will cause the authentication state to be unavailable when authorization decisions are made.

---

### Step 3 — Login and Logout HTTP Endpoints

**File:** [`Security/AuthEndpoints.cs`](SoftProg-WebApp/Security/AuthEndpoints.cs)

A fundamental constraint of Blazor Interactive Server must be understood here: once the **SignalR circuit** is established, the HTTP response headers have already been sent to the browser. Consequently, it is **not possible to set or clear cookies from within a Razor component** during an active circuit. To work around this architectural limitation, authentication operations are handled through standard HTTP endpoints using ASP.NET Core's **Minimal API** model.

The login endpoint follows a four-step process:

```csharp
endpoints.MapPost("/auth/login", async (HttpContext context) =>
{
    // Step 1: Read credentials and the "remember me" flag from the form body
    var username = form["usuario"].ToString();
    var password = form["contrasena"].ToString();
    var rememberMe = form["rememberMe"].ToString() == "on"; // checkbox posts "on" when checked

    // Step 2: Validate credentials and assign a role
    // (admin/admin → Admin role | employee/employee → Employee role)

    // Step 3: Build a ClaimsPrincipal representing the authenticated user
    var claims = new List<Claim>
    {
        new(ClaimTypes.Name, username),
        new(ClaimTypes.Role, role),
        new("DisplayName", displayName),
        new("EmployeeId", "56478")
    };

    // Step 4: Issue the authentication cookie
    // IsPersistent controls whether the browser writes the cookie to disk (true)
    // or keeps it in memory only (false). ExpiresUtc controls server-side ticket validity.
    // When rememberMe=false, ExpiresUtc is set to 30 minutes — see note below.
    await context.SignInAsync(
        CookieAuthenticationDefaults.AuthenticationScheme,
        principal,
        new AuthenticationProperties
        {
            IsPersistent = rememberMe,
            ExpiresUtc = rememberMe
                ? DateTimeOffset.UtcNow.AddHours(8)
                : DateTimeOffset.UtcNow.AddMinutes(30)
        });

    return Results.LocalRedirect("/home");
});

endpoints.MapGet("/auth/logout", async (HttpContext context) =>
{
    await context.SignOutAsync(CookieAuthenticationDefaults.AuthenticationScheme);
    return Results.LocalRedirect("/");
});
```

> **`IsPersistent` vs `ExpiresUtc` — an important distinction**
>
> These two properties control different layers of cookie lifetime:
>
> | Property | What it controls | Where it takes effect |
> |:---|:---|:---|
> | `IsPersistent` | Whether the `Expires` attribute appears in the `Set-Cookie` header | **Browser** — determines if the cookie is written to disk or kept in memory |
> | `ExpiresUtc` | The expiry timestamp encoded inside the encrypted ticket | **Server** — ASP.NET Core rejects tickets past this time, regardless of the browser |
>
> **Importantly**, when `IsPersistent = false`, ASP.NET Core **never** emits an `Expires` attribute in the `Set-Cookie` header, even if `ExpiresUtc` is set. The cookie is always a session cookie from the browser's perspective. `ExpiresUtc` only limits server-side ticket acceptance.
>
> **Browser session restore caveat:** Modern browsers (Chrome, Edge, Firefox) include a session-restore feature that saves session cookies to disk and reloads them on the next launch. This means `IsPersistent = false` alone does **not** guarantee the user is logged out after closing the browser. Setting `ExpiresUtc` to a short duration (e.g. 30 minutes) ensures the server rejects the restored cookie ticket, effectively enforcing the timeout even when the browser restores the session.

The login form in `Login.razor` uses standard HTML markup — not a Blazor `EditForm` — to ensure the request is sent as a full HTTP POST:

```html
<form method="post" action="/auth/login">
    <AntiforgeryToken />
    <input name="usuario" ... />
    <input name="contrasena" type="password" ... />
    <input type="checkbox" name="rememberMe" />  <!-- posts "on" when checked, omitted when not -->
    <button type="submit">Sign In</button>
</form>
```

> **Why is `<AntiforgeryToken />` required?**  
> Blazor's `UseAntiforgery()` middleware validates a hidden anti-forgery token on every POST request. This mechanism protects against **Cross-Site Request Forgery (CSRF)** attacks, where a malicious site could otherwise submit a form on behalf of an authenticated user. Omitting this token results in a `400 Bad Request` response.

---

### Step 4 — Declaring Page-Level Access Requirements

The `[Authorize]` attribute is applied to Razor page components to declare their access requirements. The central router evaluates this attribute before rendering any page.

| Page | Directive | Access Level |
|:---|:---|:---|
| `Login.razor` | `@attribute [AllowAnonymous]` | Publicly accessible — no authentication required |
| `Home.razor` | `@attribute [Authorize]` | Requires an active authenticated session |
| `CreateOrder.razor` | `@attribute [Authorize]` | Requires an active authenticated session |
| `ManageOrders.razor` | `@attribute [Authorize(Roles = "Admin")]` | Restricted to users with the Admin role |

> **To add a new protected page**, place the appropriate attribute at the top of the `.razor` file. The router and middleware handle enforcement automatically — no additional configuration is required.

```razor
@page "/new-page"
@attribute [Authorize(Roles = "Admin")]   <!-- or simply [Authorize] -->
```

---

### Step 5 — The Central Authorization Router

**File:** [`Components/Routes.razor`](SoftProg-WebApp/Components/Routes.razor)

The router distinguishes between two distinct unauthorized scenarios and responds to each appropriately:

```razor
<AuthorizeRouteView RouteData="routeData" DefaultLayout="typeof(Layout.MainLayout)">
    <NotAuthorized>
        @if (context.User.Identity?.IsAuthenticated == true)
        {
            <AccessDenied />       <!-- User is authenticated but lacks the required role -->
        }
        else
        {
            <RedirectToLogin />    <!-- User is not authenticated → redirect to login -->
        }
    </NotAuthorized>
</AuthorizeRouteView>
```

**Conceptual notes:**

- `context` is an `AuthenticationState` object provided by the `CascadingAuthenticationState` service registered in Step 1.
- `context.User.Identity?.IsAuthenticated` evaluates to `true` when a valid authentication cookie is present, regardless of whether the user holds the correct role.
- This logic is defined **once** and applies **globally** to all protected pages, eliminating the need for repetitive authorization checks at the component level.

---

### Step 6 — Security Razor Components

#### `RedirectToLogin.razor`
**Location:** `Components/Security/RedirectToLogin.razor`  
**Invoked by:** `Routes.razor` → `<NotAuthorized>` when `context.User.Identity?.IsAuthenticated` is `false`.

```razor
@inject NavigationManager NavigationManager

@code {
    protected override void OnInitialized()
    {
        // forceLoad: true triggers a full HTTP request rather than a Blazor
        // client-side navigation. This allows the cookie authentication middleware
        // to take control and apply server-side redirect logic correctly.
        NavigationManager.NavigateTo("/", forceLoad: true);
    }
}
```

#### `AccessDenied.razor` (UI Component)
**Location:** `Components/Security/AccessDenied.razor`  
**Invoked by:** `Routes.razor` → `<NotAuthorized>` when the user is authenticated but lacks the required role.  
This component contains exclusively the markup for the "Access Denied" card. It serves as the **single source of truth** for this UI, ensuring visual consistency regardless of how it is invoked.

#### `AccessDenied.razor` (Routable Page)
**Location:** `Components/Pages/AccessDenied.razor`  
**Invoked by:** The ASP.NET Core middleware when redirecting directly to `/access-denied` via `AccessDeniedPath`.

```razor
@page "/access-denied"

<PageTitle>Access Denied</PageTitle>

<AccessDenied />   <!-- Consumes the UI component from Components/Security/ -->
```

> **Why are both files necessary?**  
> Two independent flows must display the same UI:
> 1. **Blazor internal navigation** — `Routes.razor` renders `<AccessDenied />` directly within the existing circuit.
> 2. **Direct URL access** — The HTTP middleware redirects to `/access-denied`, which is a routable page that wraps the same component.  
>
> By having both files consume the same underlying component, the application guarantees **a consistent user experience** while avoiding code duplication.

---

### Step 7 — Conditionally Rendering UI Elements by Role

In addition to protecting entire pages, individual UI elements — such as navigation links — can be conditionally rendered based on the user's role using the `<AuthorizeView>` component in `MainLayout.razor`:

```razor
<AuthorizeView Roles="Admin">
    <li><a href="/manage-orders">Sales Orders</a></li>
</AuthorizeView>
```

**Important distinctions:**

- The element is **entirely excluded from the rendered DOM** for users who do not hold the specified role. This is not a CSS visibility toggle; the HTML is never sent to the browser.
- `<AuthorizeView>` can be used in any Razor component, not exclusively in layout files.
- This mechanism provides **UI-level access control** and is complementary to, but not a replacement for, route-level protection via `[Authorize]`.

---

## ➕ How to Add a New Protected Page

The following procedure should be followed whenever a new restricted page is introduced into the application:

1. Create a new `.razor` file under `Components/Pages/`.
2. Declare the required access level at the top of the file:
   ```razor
   @page "/my-new-page"
   @attribute [Authorize(Roles = "Admin")]   <!-- or @attribute [Authorize] for any authenticated user -->
   ```
3. No further configuration is required. The router (`Routes.razor`) and the middleware (`AuthExtensions.cs`) enforce access control automatically.

> To introduce a **new role**, define it in `AuthEndpoints.cs` when constructing the user's claims, then reference it in `[Authorize(Roles = "NewRole")]` and `<AuthorizeView Roles="NewRole">` wherever appropriate.

---

## ⚙️ File Responsibilities Summary

| File | Responsibility |
|:---|:---|
| `Security/AuthExtensions.cs` | Cookie authentication registration, login/denied paths, session expiration |
| `Security/AuthEndpoints.cs` | Credential validation logic, cookie issuance and destruction |
| `Program.cs` | Pipeline orchestration — delegates entirely to the two files above |
| `Components/Routes.razor` | Distinguishes unauthenticated vs. unauthorized, enforces globally |
| `Components/Security/AccessDenied.razor` | Reusable "Access Denied" UI component (single source of truth) |
| `Components/Security/RedirectToLogin.razor` | Full-page redirect for unauthenticated users |
| `Components/Pages/AccessDenied.razor` | Routable page at `/access-denied` consumed by the HTTP middleware |
| `Components/_Imports.razor` | Exposes `SoftProg_WebApp.Components.Security` namespace project-wide |
