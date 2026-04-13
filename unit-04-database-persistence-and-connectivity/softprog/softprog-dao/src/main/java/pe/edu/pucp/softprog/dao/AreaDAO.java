package pe.edu.pucp.softprog.dao;

import pe.edu.pucp.softprog.dao.base.BaseDAO;
import pe.edu.pucp.softprog.dao.model.Area;

import java.util.List;

public interface AreaDAO extends BaseDAO<Area, Integer> {
    List<Area> listAll();
}
