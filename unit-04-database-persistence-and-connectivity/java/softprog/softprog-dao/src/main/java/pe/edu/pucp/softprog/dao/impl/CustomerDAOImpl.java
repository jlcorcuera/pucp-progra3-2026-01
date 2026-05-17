package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.CustomerDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public Customer load(Integer customerId) throws SQLException {
        String sql = """
            select
                c.id_cliente,
                p.dni,
                p.nombre,
                p.apellido_paterno,
                p.sexo,
                p.fecha_nacimiento,
                c.linea_credito,
                c.categoria
            from
                cliente c
                inner join persona p on c.id_cliente = p.id_persona
            where
                c.id_cliente = ?
        """;
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, customerId);
                try(ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        Customer customer = new Customer();
                        customer.setId(rs.getInt("id_cliente"));
                        customer.setDocumentNumber(rs.getString(2));
                        customer.setName(rs.getString(3));
                        customer.setLastName(rs.getString(4));
                        customer.setGender(rs.getString(5));
                        customer.setBirthDate(rs.getDate(6));
                        return customer;
                    }
                }
        }
        return null;
    }

    @Override
    public Customer save(Customer customer) throws SQLException {
        return null;
    }

    @Override
    public Customer update(Customer customer) throws SQLException {
        return null;
    }

    @Override
    public void remove(Customer customer) throws SQLException {

    }

}
