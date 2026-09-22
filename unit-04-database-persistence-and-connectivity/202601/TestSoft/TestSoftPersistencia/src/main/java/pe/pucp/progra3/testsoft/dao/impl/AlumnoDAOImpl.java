package pe.pucp.progra3.testsoft.dao.impl;

import pe.pucp.progra3.testsoft.dao.AlumnoDAO;
import pe.pucp.progra3.testsoft.dbmanager.DBManager;
import pe.pucp.progra3.testsoft.model.Alumno;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAOImpl implements AlumnoDAO {
    @Override
    public List<Alumno> leerTodos() throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement callableStatement = connection.prepareCall("{CALL listar_alumnos()}");
            ResultSet rs = callableStatement.executeQuery()) {
            //id, codigo, nombre, correo
            while (rs.next()) {
                alumnos.add(new Alumno(rs.getInt("id"),
                                       rs.getString("codigo"),
                                       rs.getString("nombre"),
                                       rs.getString("correo")));
            }
        }
        return alumnos;
    }
}
