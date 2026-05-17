using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Text;

namespace Lab07_db_manager
{
    public class DBManager
    {
        private static DBManager _instance;
        private readonly string _connectionStringSource;
        private readonly string _connectionStringDestination;

        public static DBManager Instance
        {
            get
            {
                return _instance;
            }
        }
        private DBManager(string connectionStringSource, string connectionStringDestination)
        {
            _connectionStringSource = connectionStringSource;
            _connectionStringDestination = connectionStringDestination;
        }
        public static void Initialize(string connectionString1, string connectionString2)
        {
            if (_instance == null)
            {
                _instance = new DBManager(connectionString1, connectionString2);
            }
        }
        public MySqlConnection GetConnectionSource()
        {
            return new MySqlConnection(_connectionStringSource);
        }

        public MySqlConnection GetConnectionDestination()
        {
            return new MySqlConnection(_connectionStringDestination);
        }
    }

}
