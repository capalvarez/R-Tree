package structures;

import java.util.LinkedList;

public interface Node {

	/*Retorna el numero de entradas que contiene el nodo*/
	public int getEntryCount();
	
	/*Decide si el nodo es hoja o nodo interno*/
	public boolean isLeaf();
	
	/*Devuelve la lista enlazada que guarda el nodo*/
	public LinkedList getList();
	
	/*Devuelve el MBR (rectangulo que contiene a los elementos del nodo)*/
	public Rectangle getRectangle();

	/*Cambia el rectangulo covertor para ajustarse a los datos*/
	public void adjustRectangle();
	
}
