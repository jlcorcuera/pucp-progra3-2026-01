package pe.edu.pucp.softprog.dao.base;

import java.sql.SQLException;

public interface BaseDAO <T, ID> {
    T load(ID id) throws SQLException;
    T save(T t) throws SQLException;
    T update(T t) throws SQLException;
    void remove(T t) throws SQLException;
}
