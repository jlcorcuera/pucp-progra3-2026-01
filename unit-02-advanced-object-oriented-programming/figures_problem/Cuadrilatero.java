class Cuadrilatero extends Figura {
   /*
   CUADRILÁTERO:
 	LINEA: (0,0),(5,5)
 	LINEA: (5,5),(10,5)
 	LINEA: (10,5),(8,0)
 	LINEA: (8,0),(0,0)
   */
   private Linea linea1;
   private Linea linea2;
   private Linea linea3;
   private Linea linea4;
   
   public Cuadrilatero(Linea linea1, Linea linea2, Linea linea3, Linea linea4) {
   	this.linea1 = linea1;
   	this.linea2 = linea2;
   	this.linea3 = linea3;
   	this.linea4 = linea4;
   }
   
   public String imprimir() {
      String texto = "CUADRILÁTERO: \n";
      texto += "\t " + this.linea1.imprimir() + "\n";
      texto += "\t " + this.linea2.imprimir() + "\n";
      texto += "\t " + this.linea3.imprimir() + "\n";
      texto += "\t " + this.linea4.imprimir();
      return texto;
   }
}



