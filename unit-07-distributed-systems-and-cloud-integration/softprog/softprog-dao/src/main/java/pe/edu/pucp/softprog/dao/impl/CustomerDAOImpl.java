package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.CustomerDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public int totalNumberOfRecords(String firstName, String lastName, String docNumber) throws SQLException {
        String sql = """
            select
                count(c.id_cliente)
            from
               cliente c
               inner join persona p on p.id_persona = c.id_cliente
            where
                1 = 1
        """;
        if (firstName != null && !firstName.isEmpty()) {
            sql += " and p.nombre like concat('%', ?, '%') ";
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql += " and p.apellido_paterno like concat('%', ?, '%') ";
        }
        if (docNumber != null && !docNumber.isEmpty()) {
            sql += " and p.DNI like concat('%', ?, '%') ";
        }
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            int index = 0;
            if (firstName != null && !firstName.isEmpty()) {
                pstm.setString(++index, firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                pstm.setString(++index, firstName);
            }
            if (docNumber != null && !docNumber.isEmpty()) {
                pstm.setString(++index, firstName);
            }
            try(ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public List<Customer> fetchPage(String firstName, String lastName, String docNumber, int page, int recordsPerPage) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = """
            select
                c.id_cliente,
                p.nombre,
                p.apellido_paterno,
                p.DNI
            from
               cliente c
               inner join persona p on p.id_persona = c.id_cliente
            where
                1 = 1
        """;
        if (firstName != null && !firstName.isEmpty()) {
            sql += " and p.nombre like concat('%', ?, '%') ";
        }
        if (lastName != null && !lastName.isEmpty()) {
            sql += " and p.apellido_paterno like concat('%', ?, '%') ";
        }
        if (docNumber != null && !docNumber.isEmpty()) {
            sql += " and p.DNI like concat('%', ?, '%') ";
        }
        sql += " order by c.id_cliente ";
        sql += " limit ?, ?";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            int index = 0;
            if (firstName != null && !firstName.isEmpty()) {
                pstm.setString(++index, firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                pstm.setString(++index, lastName);
            }
            if (docNumber != null && !docNumber.isEmpty()) {
                pstm.setString(++index, docNumber);
            }
            int offset = (page - 1) * recordsPerPage;
            pstm.setInt(++index, offset);
            pstm.setInt(++index, recordsPerPage);
            try(ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id_cliente"));
                    customer.setDocumentNumber(rs.getString("DNI"));
                    customer.setName(rs.getString("nombre"));
                    customer.setLastName(rs.getString("apellido_paterno"));
                    customers.add(customer);
                }
            }
        }
        return customers;
    }
}
