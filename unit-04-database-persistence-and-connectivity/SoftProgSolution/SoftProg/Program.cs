using System;
using System.IO;
using Microsoft.Extensions.Configuration;
using MySql.Data.MySqlClient;
using SoftProgDBManager;
class Program
{
    static void Main(string[] args)
    {
        IConfiguration configuration = new ConfigurationBuilder()
        .SetBasePath(Directory.GetCurrentDirectory())
        .AddJsonFile("appsettings.json")
        .Build();
        string connectionString = configuration.GetConnectionString("MySqlConnection");
          // Inicializamos el Singleton
        DBManager.Initialize(connectionString);
        using (MySqlConnection connection = DBManager.Instance.GetConnection())
        {
            connection.Open();
            string sql = "INSERT INTO area(nombre, activa) VALUES (@nombre, @activa)";
            MySqlCommand command = new MySqlCommand(sql, connection);
            command.Parameters.AddWithValue("@nombre", "Recursos Humanos");
            command.Parameters.AddWithValue("@activa", true);
            int filasAfectadas = command.ExecuteNonQuery();
            Console.WriteLine("Filas insertadas: " + filasAfectadas);
        }
    }
}