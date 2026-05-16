using BlazorAppSoftProg.Components;
using SoftProgBL.RRRHH;
using SoftProgDBManager;

IConfiguration configuration = new ConfigurationBuilder()
        .SetBasePath(Directory.GetCurrentDirectory())
        .AddJsonFile("appsettings.json")
        .Build();
string dbType = configuration.GetConnectionString("Type");
string connectionStringMySQL = configuration.GetConnectionString("MySqlConnection");
string connectionStringMSSQLServer = configuration.GetConnectionString("MSSQLServerConnection");
// Inicializamos el Singleton
DBManager.Initialize(dbType, connectionStringMySQL, connectionStringMSSQLServer);



var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// Add Business Logic Layer (BLL) services.
builder.Services.AddScoped<IAreaBL, AreaBL>();
builder.Services.AddScoped<IEmpleadoBL, EmpleadoBL>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseStatusCodePagesWithReExecute("/not-found", createScopeForStatusCodePages: true);
app.UseHttpsRedirection();

app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
