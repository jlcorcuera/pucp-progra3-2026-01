import java.util.List;
import java.util.ArrayList;
import java.util.Date;

interface Consultable {
   public String obtenerDatos();
}

enum TipoDedicacion {
  TOTAL,
  PARCIAL;
}

class Miembro {
   private int codigo;
   private String nombre;
   private Date fechaNacimiento;
   private String direccion;
   private String email;
   private String sexo;
   
   public Miembro() {
   
   }
   
   public Miembro(String nombre) {
      this.nombre = nombre;
   }
   
   public String getNombre() {
   	return this.nombre;
   }
}

abstract class MiembroInterno extends Miembro implements Consultable {
  protected String codigoPUCP;
  
  public MiembroInterno(String codigoPUCP, String nombre) {
     super(nombre);
     this.codigoPUCP = codigoPUCP;
  }
}

class Alumno extends MiembroInterno {
  private float craest;
  
  public Alumno(String codigoPucp, String nombre, float craest) {
     super(codigoPucp, nombre);
     this.craest = craest;
  }
  
  public String obtenerDatos() {
     // Alumno: 20090606 - Juan Perez - 68.3
     return "Alumno: " + this.codigoPUCP + " - " + getNombre() + " - " + this.craest;
  }
}

class Profesor extends MiembroInterno {
  private String estado;
  
    public Profesor(String codigoPucp, String nombre, String estado) {
     super(codigoPucp, nombre);
     this.estado = estado;
  }
  
  // Profesor: 46891 - Andrea Montenegro - TC
    public String obtenerDatos() {
     return "Profesor: " + this.codigoPUCP + " - " + getNombre() + " - " + estado;
  }
}

class MiembroExterno extends Miembro {
  private TipoDedicacion tipoDedicacion;
}

class Equipo {

   private String nombre;
   private String interes;
   private List<Miembro> miembros = new ArrayList<Miembro>();
   
   public void agregarMiembro(Miembro miembro) {
      this.miembros.add(miembro);
   }
   
   public List<Miembro> getMiembros() {
       return this.miembros;
   }
}

class EQuipu {
   private List<Equipo> equipos;
   private List<Miembro> ejecutivos;
   
   public EQuipu() {
   	this.equipos = new ArrayList<Equipo>();
   	this.ejecutivos = new ArrayList<Miembro>();
   }
   
   public void agregarEquipo(Equipo equipo) {
      this.equipos.add(equipo);
   }
   
   public String consultarMiembrosDeEquipo(int index) {
        Equipo equipo = this.equipos.get(index);
        String toReturn = "";
        for(Miembro miembro: equipo.getMiembros()) {
            if (miembro instanceof Consultable) {
            	toReturn += ((Consultable)miembro).obtenerDatos() + "\n";
            }
        }
   	return toReturn;
   }
}

public class Principal {

  public static void main(String[] args) {
  	EQuipu objEquipu = new EQuipu(); 
  	/*
  	Alumno: 20090606 - Juan Perez - 68.3
Profesor: 46891 - Andrea Montenegro - TC
Alumno: 20096969 - Viviana Rivasplata - 64.5 
  	*/
  	Alumno alumno1 = new Alumno("20090606", "Juan Perez", 68.3f);
  	Profesor profesor = new Profesor("46891", "Andrea Montenegro", "TC");
  	Alumno alumno2 = new Alumno("20096969", "Viviana Rivasplata", 64.5f);
  	Equipo equipo = new Equipo();
  	equipo.agregarMiembro(alumno1);
  	equipo.agregarMiembro(profesor);
  	equipo.agregarMiembro(alumno2);
  	
  	objEquipu.agregarEquipo(equipo);
  	
  	String reporte = objEquipu.consultarMiembrosDeEquipo(0);
  	System.out.println(reporte);
  }
}
