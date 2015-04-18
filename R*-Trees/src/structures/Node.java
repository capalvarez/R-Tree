package structures;

import java.util.LinkedList;

public interface Node {

	/*Retorna el numero de entradas que contiene el nodo*/
	public int getEntryCount();
	
	/*Decide si el nodo es hoja o nodo interno*/
	public boolean isLeaf();
	
	/*Devuelve la lista enlazada que guarda el nodo*/
	public LinkedList getList();
	
	/*Decide si el nodo es la raiz o no*/
	public boolean isRoot();
	
	/*Busca la entrada correspondiente a un nodo en la lista de NodeElem*/
	public NodeElem findEntry(Node n);
}
