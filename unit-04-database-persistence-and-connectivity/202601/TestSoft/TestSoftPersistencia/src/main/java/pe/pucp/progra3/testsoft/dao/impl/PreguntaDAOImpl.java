package pe.pucp.progra3.testsoft.dao.impl;

import pe.pucp.progra3.testsoft.dao.AlumnoDAO;
import pe.pucp.progra3.testsoft.dao.PreguntaDAO;
import pe.pucp.progra3.testsoft.dbmanager.DBManager;
import pe.pucp.progra3.testsoft.model.Alumno;
import pe.pucp.progra3.testsoft.model.Pregunta;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAOImpl implements PreguntaDAO {
    @Override
    public List<Pregunta> listar() throws SQLException {
        List<Pregunta> preguntas = new ArrayList<>();
        try(Connection connection = DBManager.getInstance().getConnection();
            CallableStatement callableStatement = connection.prepareCall("{CALL listar_preguntas()}");
            ResultSet rs = callableStatement.executeQuery()) {
            //id, enunciado
            while (rs.next()) {
                preguntas.add(new Pregunta(rs.getInt("id"),
                                       rs.getString("enunciado")));
            }
        }
        return preguntas;
    }
}
