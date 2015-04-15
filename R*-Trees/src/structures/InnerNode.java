package structures;

import java.util.LinkedList;

public class InnerNode implements Node {
	private Rectangle boundingRectangle;
	private LinkedList childNode;
	
	public InnerNode(){
		childNode = new LinkedList();
	}
	
	public int getEntryCount(){
		return childNode.size();
	}

	/*Agrega un hijo al nodo, hay que actualizar el rectangulo que los contiene*/
	
}
