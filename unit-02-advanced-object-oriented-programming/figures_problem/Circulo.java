class Circulo extends Figura {
   private Punto centro;
   private int radio;
   
   public Circulo(Punto centro, int radio) {
      this.centro = centro;
      this.radio = radio;
   }
   
   public String imprimir() {
      // CIRCULO: CENTRO=(11,12),RADIO=10
      return "CIRCULO: CENTRO=" + this.centro.imprimir() + ",RADIO=" + this.radio;
   }
}









