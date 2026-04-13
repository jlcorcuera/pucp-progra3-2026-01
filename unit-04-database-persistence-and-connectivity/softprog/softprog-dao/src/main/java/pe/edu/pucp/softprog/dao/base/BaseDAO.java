package pe.edu.pucp.softprog.dao.base;

public interface BaseDAO <T, ID> {
    T load(ID id);
    T save(T t);
    T update(T t);
    void remove(T t);
}
