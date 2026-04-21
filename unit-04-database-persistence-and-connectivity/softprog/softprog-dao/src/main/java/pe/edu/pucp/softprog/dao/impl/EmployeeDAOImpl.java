package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.AreaDAO;
import pe.edu.pucp.softprog.dao.EmployeeDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImpl implements EmployeeDAO {

    private AreaDAO areaDAO = new AreaDAOImpl();
    @Override
    public Employee load(Integer employeeId) throws SQLException {
        String sql = """
                select
                    e.id_empleado,
                    e.fid_area,
                    p.dni,
                    p.nombre,
                    p.apellido_paterno,
                    p.sexo,
                    p.fecha_nacimiento,
                    e.cargo,
                    e.sueldo,
                    e.activo
                from
                    empleado e
                    inner join persona p on e.id_empleado = p.id_persona
                where
                    e.id_empleado = ?
        """;
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
                pstm.setInt(1, employeeId);
                try(ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        Employee employee = new Employee();
                        employee.setId(rs.getInt("id_empleado"));
                        employee.setArea(areaDAO.load(rs.getInt("fid_area")));
                        employee.setDocumentNumber(rs.getString(3));
                        employee.setName(rs.getString(4));
                        employee.setLastName(rs.getString(5));
                        employee.setGender(rs.getString(6));
                        employee.setBirthDate(rs.getDate(7));
                        employee.setPosition(rs.getString(8));
                        employee.setSalary(rs.getDouble(9));
                        employee.setActive(rs.getBoolean(10));
                        return employee;
                    }
                    return null;
                }
        }
    }

    @Override
    public Employee save(Employee employee) {
        return null;
    }

    @Override
    public Employee update(Employee employee) {
        return null;
    }

    @Override
    public void remove(Employee employee) {

    }

}
