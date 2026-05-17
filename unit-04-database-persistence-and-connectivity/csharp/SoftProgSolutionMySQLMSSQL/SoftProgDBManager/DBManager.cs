using Microsoft.Data.SqlClient;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Data;
using System.Text;

namespace SoftProgDBManager
{
    public class DBManager
    {
        private static DBManager _instance;
        private readonly string _connectionString;
        private readonly string _connectionStringMSSQLServer;
        private readonly string _dbType;
        public static DBManager Instance
        {
            get
            {
                return
            _instance;
            }
        }
        private DBManager
        (string dbType, string connectionString, string connectionStringMSSQLServer
        )
        {
            _dbType = dbType;
            _connectionString = connectionString;
            _connectionStringMSSQLServer = connectionStringMSSQLServer;
        }
        public static void Initialize(string dbType, string connectionString, string connectionStringMSSQLServer)
        {
            if (_instance == null
            )
            {
                _instance = new DBManager(dbType, connectionString, connectionStringMSSQLServer);
            }
        }
        public IDbConnection GetConnection()
        {
            if (_dbType == "MySQL")
            {
                return new MySqlConnection(_connectionString);
            }
            return new SqlConnection(_connectionStringMSSQLServer); //Deberia devolver la conexion hacia MSSQLServer
        }
    }
}
