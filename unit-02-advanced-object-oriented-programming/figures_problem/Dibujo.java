import java.util.List;
import java.util.ArrayList;

public class Dibujo {
  private List<Figura> figuras = new ArrayList<Figura>();
  
  public Dibujo() {
      this.generarFiguras();
  }
  
  public void agregarFigura(Figura figura) {
     if (this.figuras.size() == 30) {
     	return;
     }
     int lineas = 0;
     int cuadrilateros = 0;
     int circulos = 0;
     for(Figura fig: this.figuras) {
     	if (fig instanceof Linea) {
     	   lineas++;
     	} else if (fig instanceof Cuadrilatero) {
     	   cuadrilateros++;
     	} else {
     	   circulos++;
     	}
     }
     if (!(lineas <= 10 && cuadrilateros <= 10 && circulos <= 10)) {
     	return;
     }
     this.figuras.add(figura);
  }
  
  public void generarFiguras() {
  	/*
  	LINEA: (10,20),(20,30)
LINEA: (30,30),(15,15)
CUADRILÁTERO:
 LINEA: (0,0),(5,5)
 LINEA: (5,5),(10,5)
 LINEA: (10,5),(8,0)
 LINEA: (8,0),(0,0)
CIRCULO: CENTRO=(11,12),RADIO=10
  	*/
  	this.agregarFigura(new Linea(new Punto(10, 20), new Punto(20, 30)));
  	this.agregarFigura(new Linea(new Punto(30, 30), new Punto(15, 15)));
  	Linea linea1 = new Linea(new Punto(0, 0), new Punto(5, 5));
  	Linea linea2 = new Linea(new Punto(5, 5), new Punto(10, 5));
  	Linea linea3 = new Linea(new Punto(10, 5), new Punto(8, 0));
  	Linea linea4 = new Linea(new Punto(8, 0), new Punto(0, 0));
  	this.agregarFigura(new Cuadrilatero(linea1, linea2, linea3, linea4));
  	this.agregarFigura(new Circulo(new Punto(11, 12), 10));
  }
  
  public void imprimeInfoFiguras() {
     for(Figura fig: this.figuras) {
     	System.out.println(fig.imprimir());
     }
  }
  
  public static void main(String[] args) {
  	Dibujo dibujo = new Dibujo();
  	dibujo.imprimeInfoFiguras();
  }
}








