
class Linea extends Figura {
  private Punto punto1;
  private Punto punto2;
  
  public Linea(Punto punto1, Punto punto2) {
     this.punto1 = punto1;
     this.punto2 = punto2;
  }
  
  public String imprimir() {
      // LINEA: (10,20),(20,30)
     return "LINEA: " + punto1.imprimir() + ", " + punto2.imprimir();
  }
}





