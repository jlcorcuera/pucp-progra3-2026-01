package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.AreaDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Area;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAOImpl implements AreaDAO {
    @Override
    public List<Area> listAll() throws SQLException {
        List<Area> list = new ArrayList<>();
        String sql = "select id_area, nombre, activa from area where activa = 1";
        try(Connection connection = DBManager.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql)) {
                while (rs.next()) {
                    Area area = new Area();
                    area.setId(rs.getInt(1));
                    area.setName(rs.getString(2));
                    area.setActive(rs.getBoolean(3));
                    list.add(area);
                }
                return list;
        }
    }

    @Override
    public Area load(Integer id) throws SQLException {
        String sql = "select id_area, nombre, activa from area where id_area = ?";
        try(Connection connection = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Area area = new Area();
                    area.setId(rs.getInt(1));
                    area.setName(rs.getString(2));
                    area.setActive(rs.getBoolean(3));
                    return area;
                }
            }
        }
        return null;
    }

    @Override
    public Area save(Area area) throws SQLException {
        area.setActive(true);
        String sql = "insert into area (nombre, activa) values (?, ?)";
        try(Connection connection = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, area.getName());
            pstmt.setBoolean(2, area.getActive());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        area.setId(newId);
                    }
                }
            }
            return area;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Area update(Area area) throws SQLException {
        String sql = "update area set nombre = ?, activa = ? where id_area = ?";
        try(Connection connection = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, area.getName());
            pstmt.setBoolean(2, area.getActive());
            pstmt.setInt(3, area.getId());
            pstmt.executeUpdate();
            return area;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Area area) throws SQLException {
        // TODO: please implement logical removal
        area.setActive(false);
        String sql = "update area set activa = ? where id_area = ?";
        try(Connection connection = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, area.getActive());
            pstmt.setInt(2, area.getId());
        }
    }
}
