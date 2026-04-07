package principal;
import matematicas.Operacion;

public class Principal {

  public static void main(String[] args) {
       Operacion operacionMate = new Operacion();
       System.out.println(operacionMate.suma(2, 4));

       fisica.Operacion operacionFisica = new fisica.Operacion();
       System.out.println(operacionFisica.suma2(3, 5));
  }

}
