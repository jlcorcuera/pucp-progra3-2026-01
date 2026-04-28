package pe.edu.pucp.lab06.softprog.dao;

<<<<<<< HEAD
import java.sql.SQLException;

public interface BaseDAO <E, ID> {

    E save(E e) throws Exception;
    void remove(E e) throws Exception;
    E load(ID id) throws Exception;
    E update(E e) throws Exception;
=======
public interface BaseDAO <E, ID> {

    E save(E e);
    void remove(E e);
    E load(ID id);
    E update(E e);
>>>>>>> 4015b73e8b8686efecc5246e9bc696a45b4c1a97
}
