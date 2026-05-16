# Unit 5: Core Web Technologies

An introduction to modern web development — from the **Front-End Triad** (HTML, CSS, and JavaScript) that powers every web page, to building full-stack interactive applications using **Blazor** and the **.NET ecosystem**.

---

## 🌐 Introduction to Web Programming

### The Web at a Glance

The Web was invented by **Tim Berners-Lee** in 1989 at CERN to allow scientists to share documents via hyperlinks. The three foundational technologies introduced at that time — **HTML**, **HTTP**, and **URLs** — remain the backbone of the Web today.

> **Internet ≠ Web.** The Internet is the underlying network infrastructure; the Web is a set of services and resources that travel *over* it.

The Web has evolved through three generations: **Web 1.0** (static pages), **Web 2.0** (social and interactive), and **Web 3.0** (semantic, decentralized, AI-driven).

### HTTP & URLs

**HTTP** is the client-server protocol that powers all web communication. A browser sends a *request* using a verb (`GET`, `POST`, `PUT`, `DELETE`), and the server replies with a *response*. HTTP is **stateless** — it does not remember previous requests on its own.

A **URL** uniquely identifies any resource on the Web:
```
https://www.example.com:443/products?id=5#section
  │         │              │   │       │    └─ Fragment
  │         │              │   │       └─ Query parameters
  │         │              │   └─ Path
  │         │              └─ Port
  │         └─ Domain
  └─ Protocol
```

### HTML — Structure

**HTML (HyperText Markup Language)** defines the *structure and content* of a web page. It is a markup language, not a programming language.

```html
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>My Page</title>
  </head>
  <body>
    <h1>Hello World</h1>
    <p>This is a paragraph.</p>
    <a href="https://google.com">Go to Google</a>
    <img src="photo.jpg" alt="A photo">
  </body>
</html>
```

Key elements: headings `<h1>`–`<h6>`, paragraphs `<p>`, links `<a>`, images `<img>`, lists `<ul>`/`<ol>`, tables `<table>`, and text formatting (`<strong>`, `<em>`).

### CSS — Styling

**CSS (Cascading Style Sheets)** controls the visual presentation of HTML, separating *content* from *design*.

```css
h1       { color: blue; }
p        { font-family: Arial; }
#menu    { background: black; }   /* ID selector */
.button  { padding: 10px; }       /* Class selector */
```

CSS can be applied **inline** (on the element), **internally** (in a `<style>` block), or **externally** (in a linked `.css` file — recommended).

**Bootstrap** is the most popular CSS + JS framework. It provides a responsive 12-column grid, pre-built UI components, and utility classes. 📖 [getbootstrap.com/docs/5.3](https://getbootstrap.com/docs/5.3/getting-started/introduction/)

### JavaScript — Behavior

**JavaScript** adds interactivity and logic to web pages. Together with HTML and CSS it forms the *Front-End Triad*.

```javascript
// Variables
let name = "Ana";
const age = 25;

// Functions
function greet(name) {
  return "Hello " + name;
}

// DOM manipulation
document.getElementById("demo").innerHTML = "Text from JS";

// Events
document.getElementById("btn")
  .addEventListener("click", () => alert("Hello!"));
```

### HTML Forms

Forms collect user input and send it to a server via `GET` (data in the URL) or `POST` (data in the request body).

```html
<form action="/process" method="post">
  <input type="text" name="username">
  <input type="password" name="password">
  <input type="checkbox" name="agree" value="yes"> I agree
  <select name="country">
    <option value="pe">Peru</option>
    <option value="mx">Mexico</option>
  </select>
  <button type="submit">Send</button>
</form>
```

---

## ⚡ Web Application Development with Blazor

### What is Blazor?

**Blazor** is a Microsoft framework that lets you build interactive web UIs using **C# instead of JavaScript**. It is part of the **ASP.NET Core** ecosystem and enables full-stack development in a single language.

| Advantage | Description |
|-----------|-------------|
| Unified language | C# for both frontend and backend |
| Code reuse | Share .NET libraries between client and server |
| Strong typing | Compile-time error checking reduces runtime bugs |
| .NET ecosystem | Full access to NuGet and .NET tooling |

### Rendering Models

| Model | How it runs | Best for |
|-------|-------------|----------|
| **Blazor Server** | App runs on the server; UI synced via WebSocket (SignalR) | Fast startup, server-heavy apps |
| **Blazor WebAssembly** | .NET runtime downloaded and runs in the browser | Offline support, reduced server load |
| **.NET 8+ Unified** | Mix both modes per component using `@rendermode` | Maximum flexibility |

### Project Structure

```
MyBlazorApp/
├── wwwroot/                  ← Static assets (CSS, images, JS)
├── Components/
│   ├── Pages/                ← Routable pages (.razor files)
│   └── Layout/               ← MainLayout.razor, NavMenu.razor
├── appsettings.json          ← App configuration
└── Program.cs                ← Entry point; registers services
```

Key files:
- **`App.razor`** — Root component containing the `Router`
- **`_Imports.razor`** — Global `@using` directives applied to all components
- **`Program.cs`** — Registers services (`AddRazorComponents`, `AddHttpClient`, etc.)

### Razor Syntax

Razor embeds C# into HTML using the `@` character:

```razor
<h1>Welcome, @username!</h1>

@if (isLoggedIn)
{
    <p>Hello again!</p>
}

<ul>
@foreach (var item in items)
{
    <li>@item.Name</li>
}
</ul>

@code {
    private string username = "Ana";
    private bool isLoggedIn = true;
    private List<string> items = ["Apple", "Banana"];
}
```

### Routing

Add `@page` to make any component a navigable page:

```razor
@page "/products/{id:int}"

<h1>Product: @Id</h1>

@code {
    [Parameter] public int Id { get; set; }
}
```

Route parameters can be **typed** (`:int`, `:guid`) and **optional** (`{id:int?}`).

### Forms and Validation

Blazor's `EditForm` integrates with C# **DataAnnotations** for automatic validation:

```razor
<EditForm Model="user" OnValidSubmit="Save">
    <DataAnnotationsValidator />
    <ValidationSummary />
    <InputText @bind-Value="user.Name" />
    <button type="submit">Save</button>
</EditForm>

@code {
    private User user = new();
    private void Save() { /* runs only if valid */ }

    public class User {
        [Required(ErrorMessage = "Name is required")]
        [StringLength(20)]
        public string Name { get; set; } = string.Empty;
    }
}
```

Built-in input components (`InputText`, `InputNumber`, `InputDate`, `InputCheckbox`, `InputSelect`) all support two-way binding via `@bind-Value`.

### Code Behind Pattern

For complex components, separate the HTML markup from C# logic using `partial` classes:

- **`Products.razor`** — contains only the HTML/Razor markup
- **`Products.razor.cs`** — contains the C# logic as a `partial class`

This improves readability and collaboration between designers and developers.

### REST API Integration

Use `HttpClient` to communicate with backend APIs:

```razor
@inject HttpClient Http

@code {
    private List<Product>? products;

    protected override async Task OnInitializedAsync()
    {
        // GET — automatically deserializes JSON
        products = await Http.GetFromJsonAsync<List<Product>>("api/products");
    }

    private async Task Create(Product p)
    {
        // POST — automatically serializes to JSON
        await Http.PostAsJsonAsync("api/products", p);
    }
}
```

Register the client in `Program.cs`: `builder.Services.AddHttpClient(...)`.

### Dependency Injection

Register services in `Program.cs` and inject them into components with `@inject`:

```csharp
// Program.cs
builder.Services.AddScoped<IProductService, ProductService>();
```
```razor
@inject IProductService ProductService
```

| Lifetime | Behavior |
|----------|----------|
| `Transient` | New instance on every request |
| `Scoped` | One instance per user circuit/connection |
| `Singleton` | One instance for the entire app lifetime |

### Authentication & Authorization

Blazor uses ASP.NET Core cookie-based authentication. Key pieces:

- **`Program.cs`** — configure `AddAuthentication`, `AddAuthorization`, `UseAuthentication`, `UseAuthorization`
- **`Routes.razor`** — wrap the `Router` with `<CascadingAuthenticationState>` and use `<AuthorizeRouteView>`
- **`<AuthorizeView>`** — conditionally render UI based on login state or roles:

```razor
<AuthorizeView Roles="Admin">
    <Authorized>
        <p>Admin panel content here.</p>
    </Authorized>
    <NotAuthorized>
        <p>Access denied. Please log in.</p>
    </NotAuthorized>
</AuthorizeView>
```

---