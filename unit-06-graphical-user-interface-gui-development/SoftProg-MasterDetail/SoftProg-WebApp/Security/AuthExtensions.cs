using Microsoft.AspNetCore.Authentication.Cookies;

namespace SoftProg_WebApp.Security;

public static class AuthExtensions
{
    public static IServiceCollection AddCookieAuth(this IServiceCollection services)
    {
        services
            .AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme)
            .AddCookie(options =>
            {
                options.LoginPath = "/";
                options.AccessDeniedPath = "/acceso-denegado";
                options.SlidingExpiration = true;
                options.ExpireTimeSpan = TimeSpan.FromHours(8);
            });

        services.AddAuthorization();
        services.AddCascadingAuthenticationState();
        services.AddHttpContextAccessor();

        return services;
    }
}
