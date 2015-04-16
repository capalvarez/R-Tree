package structures;

public class Rectangle {
	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	
	public Rectangle(){}
	
	public Rectangle(float x1, float x2, float y1, float y2){
		minX = x1;
		maxX = x2;
		
		minY = y1;
		maxY = y2;
	}
	
	public float left(){
		return minX;
	}
	
	public float right(){
		return maxX;
	}
	
	public float top(){
		return maxY;
	}
	
	public float bottom(){
		return minY;
	} 
	
	/*Devuelve ancho del rectangulo*/
	public float width(){
		return maxX - minX;
	}
	
	/*Devuelve altura del rectangulo*/
	public float height(){
		return maxY - minY;
	}
	
	/*Asignar valores a las coordenadas del rectangulo*/
	public void setCoordinates(float x1, float x2, float y1, float y2){
		minX = x1;
		maxX = x2;
		
		minY = y1;
		maxY = y2;
	}
		
	/*Devuelve el area del rectangulo*/
	public float getArea(){
		return width()*height();
	}
	
	/*Determina si los dos rectangulos se intersectan*/
	public boolean intersects(Rectangle r){
		return !(r.left() > this.right() 
			     ||  r.right() < this.left()
				 ||  r.top() > this.bottom()
				 ||  r.bottom() < this.top());
	}
	
	/*Determina si el rectangulo entregado esta contenido en este rectangulo*/
	public boolean contains(Rectangle r){
		return false;
	}
	
	/*Determina si este rectangulo esta contenido en el entregado*/
	public boolean containedBy(Rectangle r){
		return false;
	}
	
	/*Entrega la distancia entre el rectangulo y el punto entregado*/
	public float distance(float x, float y){
		return 0;
	}
	
	/*Entrega la distancia entre el rectangulo y el rectangulo entregado*/
	public float distance(Rectangle r){
		return 0;
	}
	
	/*Devuelve el area que aumentaria el rectangulo si se agrega al rectangulo entregado*/
	public float areaEnlargement(Rectangle r){
		return 0;
	}
	
	/*Guarda en este rectangulo el resultado de agregar el rectangulo entregado al original*/
	public void addRectangle(Rectangle r){
		
	}
		
	/*Computa el resultado de unir este rectangulo con el entregado, sin modificar ninguno*/
	public Rectangle rectangleUnion(Rectangle r){
		return null;
	}
	
	/*Determina si los dos rectangulos son equivalentes (tienen los mismos bordes)*/
	public boolean equals(Rectangle r){
		return false;
	}
	
	/*Calcula el overlap que aumentaria el rectangulo y al incluir el rectangulo entregado*/
	public float overlapEnlargement(Rectangle r){
		return 0;
	}
	
	/*Devuelve el margen (perimetro) del rectangulo*/
	public float getMargin(){
		return 0;
	}
	
}
