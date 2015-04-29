package structures;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import display.RectWindow;

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
	
	public float getMinDim(int dim){
		if(dim==0){
			return left();
		}else{
			return bottom();
		}
	}
	
	public float getMaxDim(int dim){
		if(dim==0){
			return right();
		}else{
			return top();
		}
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
		return this.intersection(r)!=null;
	}
	
	/*Determina si el rectangulo entregado esta contenido en este rectangulo*/
	public boolean contains(Rectangle r){		
		return (r.left()>=this.left()) && (this.bottom()>=r.bottom()) &&
               (r.right()<=this.right()) && (r.top()<=this.top());
	}
	
	/*Entrega la distancia entre el rectangulo y el punto entregado*/
	public float distance(float x, float y){
		float[] midPoint = this.getCenter();	
		float distance = (float) Math.sqrt(Math.pow(x-midPoint[0], 2) + Math.pow(y-midPoint[1], 2));
		
		return distance;
	}
	
	public float distance(float[] c){
		return distance(c[0],c[1]);
	}
	
	/*Devuelve el area que aumentaria el rectangulo si se agrega al rectangulo entregado*/
	public float areaEnlargement(Rectangle r){
		/*Obtener el valor del area antes de incluir el nuevo rectangulo*/
		float oldArea = this.getArea();

		/*Incluir el nuevo rectangulo*/
		Rectangle newR = this.union(r);
		float newArea = newR.getArea();	

		return Math.abs(newArea - oldArea);
	}
			
	/*Determina si los dos rectangulos son equivalentes (tienen los mismos bordes)*/
	public boolean equals(Rectangle r){
		return (this.left()==r.left()) && (this.right()==r.right()) &&
			   (this.top()==r.top()) && (this.bottom()==r.bottom());
	}
	
	/*Calcula el overlap del rectangulo*/
	public float overlap(LinkedList<NodeElem> siblingList){
		float sum = 0;
	
		for(int i=0; i<siblingList.size(); i++){
			Rectangle cR = siblingList.get(i).getRectangle();
			
			if(!cR.equals(this)){
				Rectangle inter = cR.intersection(this);

				if(inter!=null){
					sum += inter.getArea();
				}
			}
	    }
		
	    return sum;	
	}
	
	/*Calcula el overlap que aumentaria el rectangulo y al incluir el rectangulo entregado*/
	public float overlapEnlargement(Rectangle r, LinkedList<NodeElem> siblingList){	
		/*Calcular el valor del overlap sin incluir el nuevo rectangulo*/
		float oldOverlap = this.overlap(siblingList);
		
		/*Incluir el nuevo rectangulo para calcular el overlap*/		
		Rectangle newRectangle = this.union(r);	
		float newOverlap = newRectangle.overlap(siblingList);

		return Math.abs(newOverlap - oldOverlap);
	}
	
			
	/*Devuelve el margen (perimetro) del rectangulo*/
	public float getMargin(){
		return 2*(this.height() + this.width());
	}
	
	/*Devuelve el rectangulo que corresponde a la inteseccion entre el rectangulo y el entregado*/
	public Rectangle intersection(Rectangle r){
		float xL = Math.max(this.left(), r.left());
		float xR = Math.min(this.right(),r.right());	
		
		if(xL>=xR)
			return null;
		
		float yT = Math.min(this.top(),r.top());
		float yB = Math.max(this.bottom(), r.bottom());
		
		if(yB>=yT)
			return null;
		
		return new Rectangle(xL,xR,yB,yT);	
	}
	
	/*Devuelve un nuevo rectangulo, que corresponde a la union de este y el entregado*/
	public Rectangle union(Rectangle r){
		float xL = Math.min(this.left(), r.left());
		float xR = Math.max(this.right(), r.right());
		
		float yT = Math.max(this.top(), r.top());
		float yB = Math.min(this.bottom(), r.bottom());
		
		return new Rectangle(xL,xR,yB,yT);
	}
	
	public float[] getCenter(){
		float[] midPoint = new float[2];
			
		midPoint[0] = (right() - left())/2 + left();
		midPoint[1] = (top() - bottom())/2 + bottom();
		
		return midPoint;
	}
	
	/*Dibuja un rectangulo*/
	public void draw(Graphics2D g2d){
        float height = Math.abs(minY- maxY);
        float width = Math.abs(maxX - minX);
       
        g2d.draw(new Rectangle2D.Double(minX, minY, width, height));
		
	}
	
	public String toString(){
		return "("+minX + ","+ minY +")(" + maxX + "," + maxY + ")";
	}
	
	public byte[] getByteForm(byte[] bytes, int pos){
		ByteBuffer.wrap(bytes, pos, 8).putFloat(this.left());	
		pos += 8;
		ByteBuffer.wrap(bytes, pos, 8).putFloat(this.right());	
		pos += 8;
		ByteBuffer.wrap(bytes, pos, 8).putFloat(this.bottom());	
		pos += 8;
		ByteBuffer.wrap(bytes, pos, 8).putFloat(this.top());	
		pos += 8;
		
		return bytes;
	}
}
