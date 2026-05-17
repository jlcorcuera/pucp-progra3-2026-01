using Microsoft.Extensions.Configuration;
using MySql.Data.MySqlClient;
using SoftProgDAO;
using SoftProgDBManager;
using SoftProgDomain.RRRHH;
using System;
using System.Data;
using System.IO;
class Program
{
    static void Main(string[] args)
    {
        IConfiguration configuration = new ConfigurationBuilder()
        .SetBasePath(Directory.GetCurrentDirectory())
        .AddJsonFile("appsettings.json")
        .Build();
        string dbType = configuration.GetConnectionString("Type");
        string connectionStringMySQL = configuration.GetConnectionString("MySqlConnection");
        string connectionStringMSSQLServer = configuration.GetConnectionString("MSSQLServerConnection");
        // Inicializamos el Singleton
        DBManager.Initialize(dbType, connectionStringMySQL, connectionStringMSSQLServer);

        AreaDAO areaDAO = new AreaDAO();

        Area nuevaArea = new Area();
        nuevaArea.Name = "Area V1";
        nuevaArea.Activa = true;

        areaDAO.registrarV1(nuevaArea);
    }
}