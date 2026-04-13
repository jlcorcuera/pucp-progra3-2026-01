package pe.edu.pucp.softprog.test;

import pe.edu.pucp.softprog.dao.AreaDAO;
import pe.edu.pucp.softprog.dao.impl.AreaDAOImpl;
import pe.edu.pucp.softprog.dao.model.Area;

import java.util.List;

public class TestAreaDAOMain {

    public static void main(String [] args) {
        AreaDAO areaDAO = new AreaDAOImpl();
        Area newArea = new Area("New Area 1");

        areaDAO.save(newArea);
        System.out.println("New area in List:");
        List<Area> areas = areaDAO.listAll();
        for(Area area: areas) {
            System.out.println(area.getName());
        }

        areaDAO.remove(newArea);
        System.out.println("After removing new area:");
        areas = areaDAO.listAll();
        for(Area area: areas) {
            System.out.println(area.getName());
        }
    }
}
