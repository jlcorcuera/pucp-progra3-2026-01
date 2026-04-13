package pe.edu.pucp.softprog.dao.impl;

import pe.edu.pucp.softprog.dao.AreaDAO;
import pe.edu.pucp.softprog.dao.manager.DBManager;
import pe.edu.pucp.softprog.dao.model.Area;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AreaDAOImpl implements AreaDAO {
    @Override
    public List<Area> listAll() {
        List<Area> list = new ArrayList<>();
        String sql = "select id, nombre, activo from AREA where activo = 1";
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Area load(Integer id) {
        String sql = "select id, nombre, activo from AREA where id = ?";
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Area save(Area area) {
        area.setActive(true);
        String sql = "insert into AREA (nombre, activo) values (?, ?)";
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
    public Area update(Area area) {
        String sql = "update AREA set nombre = ?, activo = ? where id = ?";
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
    public void remove(Area area) {
        // TODO: please implement logical removal
        area.setActive(false);
        String sql = "update AREA set activo = ? where id = ?";
        try(Connection connection = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, area.getActive());
            pstmt.setInt(2, area.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
