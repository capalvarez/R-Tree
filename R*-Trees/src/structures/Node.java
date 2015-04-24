package structures;

import java.util.LinkedList;

public interface Node {

	/*Retorna el numero de entradas que contiene el nodo*/
	public int getEntryCount();
	
	/*Decide si el nodo es hoja o nodo interno*/
	public boolean isLeaf();
	
	/*Devuelve la lista de hijos que guarda el nodo*/
	public LinkedList<NodeElem> getNodeList();
	
	/*Setea la lista de hijos que guarda el nodo*/
	public void setNodeList(LinkedList<NodeElem> l);
	
	/*Decide si el nodo es la raiz o no*/
	public boolean isRoot();
	
	/*Busca la entrada correspondiente a un nodo en la lista de NodeElem*/
	public NodeElem findEntry(Node n);
	
	/*Setea el nodo como raiz del arbol*/
	public void setAsRoot();
	
	/*Setea el nodo como uno no raiz*/
	public void setAsNotRoot();
	
	/*Retorna la altura en el arbol en el que esta el nodo*/
	public int getHeight();
	
	/*Setea la altura en la que esta el nodo*/
	public void setHeight();
}
