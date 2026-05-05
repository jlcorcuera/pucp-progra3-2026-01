//using Lab07_dao.impl;
using Lab07_business_logic;
using Lab07_db_manager;
using Lab07_domain.dto;
using Microsoft.Extensions.Configuration;
using System;
using System.Configuration;
using System.IO;
class Program
{
    static void Main(string[] args)
    {
        IConfiguration configuration = new ConfigurationBuilder()
        .SetBasePath(Directory.GetCurrentDirectory())
        .AddJsonFile("appsettings.json")
        .Build();
        string connectionStringSource = configuration.GetConnectionString("MySqlConnectionSource");
        string connectionStringDestination = configuration.GetConnectionString("MySqlConnectionDestination");
        DBManager.Initialize(connectionStringSource, connectionStringDestination);
        //List<VentaDNDTO> ventas = new VentaDNDAO().listAll();
        //Console.WriteLine(ventas.Count);
        new MigratorBL().run();
    }
}