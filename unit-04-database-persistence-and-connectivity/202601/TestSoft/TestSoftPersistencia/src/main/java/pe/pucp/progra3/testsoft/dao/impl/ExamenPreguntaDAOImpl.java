package pe.pucp.progra3.testsoft.dao.impl;

import pe.pucp.progra3.testsoft.dao.ExamenDAO;
import pe.pucp.progra3.testsoft.dao.ExamenPreguntaDAO;
import pe.pucp.progra3.testsoft.dbmanager.DBManager;
import pe.pucp.progra3.testsoft.dbmanager.TransactionContext;
import pe.pucp.progra3.testsoft.model.Examen;
import pe.pucp.progra3.testsoft.model.ExamenPregunta;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class ExamenPreguntaDAOImpl implements ExamenPreguntaDAO {
    @Override
    public void registrar(List<ExamenPregunta> examenesPreguntas) throws SQLException {
        /*
CREATE DEFINER=`admin`@`%` PROCEDURE `insertar_examen_pregunta`(
    in p_id_examen int,
    in p_id_pregunta int,
    in p_orden int,
    out p_id int
)
        */
        Connection connection = TransactionContext.getConnection();
        try(
            CallableStatement callableStatement = connection.prepareCall("{CALL insertar_examen_pregunta(?, ?, ?, ?)}");) {
            for(ExamenPregunta examenPregunta:examenesPreguntas) {
                callableStatement.registerOutParameter(4, Types.INTEGER);
                callableStatement.setInt(1, examenPregunta.getExamen().getId());
                callableStatement.setInt(2, examenPregunta.getPregunta().getId());
                callableStatement.setInt(3, examenPregunta.getOrden());
                callableStatement.execute();
            }
           // callableStatement.executeBatch();
        }
    }
}
