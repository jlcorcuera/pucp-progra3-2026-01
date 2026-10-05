using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace PokemonMigrator_dbmanager
{
    public class DBManager
    {
        private static DBManager _instance;
        private readonly string _connectionString;
        public static DBManager Instance
        {
            get
            {
                return
            _instance;
            }
        }
        private DBManager(string connectionString)
        {
            _connectionString = connectionString;
        }
        public static void Initialize(string connectionString)
        {
            if (_instance == null
            )
            {
                _instance = new DBManager(connectionString);
            }
        }
        public IDbConnection GetConnection()
        {
             return new MySqlConnection(_connectionString);
        }
    }
}
