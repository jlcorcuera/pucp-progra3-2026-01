using Microsoft.Extensions.Configuration;
using PokemonMigrator_dbmanager;
using PokemonMigrator_service.Impl;
using PokemonMigrator_service.Interfaces;

IConfiguration configuration = new ConfigurationBuilder()
.SetBasePath(Directory.GetCurrentDirectory())
.AddJsonFile("appsettings.json")
.Build();
string connectionStringMySQL = configuration.GetConnectionString("MySqlConnection");// Inicializamos el Singleton
DBManager.Initialize(connectionStringMySQL);

Console.WriteLine("Iniciando Migrador");
IPokemonMigrator migrador = new PokemonMigrator(); //PokemonMigratorDictionary
migrador.migrate();