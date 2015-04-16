package structures;

import java.util.LinkedList;

public class InnerNode implements Node {
	private Rectangle boundingRectangle;
	private LinkedList<Node> childNode;
		
	public InnerNode(){
		childNode = new LinkedList<Node>();
	}
	
	public int getEntryCount(){
		return childNode.size();
	}

	public boolean isLeaf(){
		return false;
	}
	
	/*Agrega un hijo al nodo, hay que actualizar el rectangulo que los contiene*/
	public void addChild(Node newChild){
		
	}
	
	/*Actualiza el rectangulo contenedor*/
	public void setRectangle(Rectangle r){
		
	}
	
	/*Elimina hijo del nodo, hay que actualizar el rectangulo que los contiene*/
	public void deleteChild(Node oldChild){
		/*No estoy muy segura de que voy a recibir para buscar el que se elimina*/
	}
	
	public void adjustRectangle(){
		float top = Float.MIN_VALUE;
		float bottom = Float.MAX_VALUE;
		float left = Float.MAX_VALUE;
		float right = Float.MIN_VALUE;
			
		for(int i = 0; i < childNode.size(); i++){
			Rectangle r = childNode.get(i).getRectangle();
			
			if(r.top()>top){
				top = r.top();			
			}
			
			if(r.bottom()<bottom){
				bottom = r.bottom();
			}
			
			if(left>r.left()){
				left = r.left();	
			}
			
			if(right<r.right()){
				right = r.right();
			}	
		}
		
		boundingRectangle.setCoordinates(left,right,top,bottom);	
	}
	
}
