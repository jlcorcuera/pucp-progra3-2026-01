using System.Security.Claims;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.Cookies;

namespace SoftProg_WebApp.Security;

public static class AuthEndpoints
{
    public static IEndpointRouteBuilder MapAuthEndpoints(this IEndpointRouteBuilder endpoints)
    {
        endpoints.MapPost("/auth/login", async (HttpContext context) =>
        {
            var form = await context.Request.ReadFormAsync();
            var usuario = form["usuario"].ToString();
            var contrasena = form["contrasena"].ToString();
            var rememberMe = form["rememberMe"].ToString() == "on";

            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(contrasena))
                return Results.LocalRedirect("/?error=1");

            string role;
            string displayName;

            if (usuario.Equals("admin", StringComparison.OrdinalIgnoreCase) && contrasena == "admin")
            {
                role = "Admin";
                displayName = "Administrador Principal";
            }
            else if (usuario.Equals("employee", StringComparison.OrdinalIgnoreCase) && contrasena == "employee")
            {
                role = "Employee";
                displayName = "Empleado de Ventas";
            }
            else
            {
                return Results.LocalRedirect("/?error=1");
            }

            var claims = new List<Claim>
            {
                new(ClaimTypes.Name, usuario),
                new(ClaimTypes.Role, role),
                new("DisplayName", displayName),
                new("EmployeeId", "56478")
            };

            var principal = new ClaimsPrincipal(
                new ClaimsIdentity(claims, CookieAuthenticationDefaults.AuthenticationScheme));

            await context.SignInAsync(
                CookieAuthenticationDefaults.AuthenticationScheme,
                principal,
                new AuthenticationProperties
                {
                    // rememberMe=true  → persistent cookie, survives browser restarts, expires in 8 hours
                    // rememberMe=false → short-lived cookie (30 min); avoids browser session-restore keeping
                    //                    the user logged in indefinitely despite not ticking "remember me"
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

        return endpoints;
    }
}
