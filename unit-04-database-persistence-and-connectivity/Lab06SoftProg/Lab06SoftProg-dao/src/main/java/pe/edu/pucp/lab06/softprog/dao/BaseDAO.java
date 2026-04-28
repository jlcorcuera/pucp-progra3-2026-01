package pe.edu.pucp.lab06.softprog.dao;

public interface BaseDAO <E, ID> {

    E save(E e);
    void remove(E e);
    E load(ID id);
    E update(E e);
}
