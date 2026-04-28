package pe.edu.pucp.lab06.softprog.dao;

public interface BaseDAO <E, ID> {

    E save(E e) throws Exception;
    void remove(E e) throws Exception;
    E load(ID id) throws Exception;
    E update(E e) throws Exception;
}
