package pe.pucp.progra3.testsoft.dao.impl;

import pe.pucp.progra3.testsoft.dao.ExamenDAO;
import pe.pucp.progra3.testsoft.dbmanager.DBManager;
import pe.pucp.progra3.testsoft.dbmanager.TransactionContext;
import pe.pucp.progra3.testsoft.model.Alumno;
import pe.pucp.progra3.testsoft.model.Examen;

import java.sql.*;

public class ExamenDAOImpl implements ExamenDAO {
    @Override
    public void registrar(Examen examen) throws SQLException {
        /*
        CREATE DEFINER=`admin`@`%` PROCEDURE `insertar_examen`(
            in p_id_alumno int,
            in p_titulo varchar(120),
            out p_id int
      )
        */
        Connection connection = TransactionContext.getConnection();
        try(
            CallableStatement callableStatement = connection.prepareCall("{CALL insertar_examen(?, ?, ?)}");) {
            callableStatement.registerOutParameter(3, Types.INTEGER);
            callableStatement.setInt(1, examen.getAlumno().getId());
            callableStatement.setString(2, examen.getTitulo());
            callableStatement.execute();
            Integer idGenerado = callableStatement.getInt(3);
            examen.setId(idGenerado);
        }
    }
}
