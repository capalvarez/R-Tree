package structures;

import java.util.LinkedList;

public interface Node {

	/*Retorna el numero de entradas que contiene el nodo*/
	public int getEntryCount();
	
	/*Decide si el nodo es hoja o nodo interno*/
	public boolean isLeaf();
	
	/*Devuelve la lista de hijos que guarda el nodo*/
	public LinkedList<NodeElem> getNodeList();
	
	/*Devuelve la posicion en el archivo donde esta el nodo*/
	public long getPos();
	
	/*Setea la posicion del nodo en el archivo*/
	public void setPos(long pos);
	
	/*Setea la lista de hijos que guarda el nodo*/
	public void setNodeList(LinkedList<NodeElem> l);
	
	/*Agrega un hijo al arreglo*/
	public void addChild(NodeElem e);
	
	/*Elimina un hijo del arreglo*/
	public void removeChild(NodeElem e);
	
	/*Devuelve la posicion en archivo asociada al hijo i*/
	public long getChildPos(int i);
	
	/*Decide si el nodo es la raiz o no*/
	public boolean isRoot();
	
	/*Setea el nodo como raiz del arbol*/
	public void setAsRoot();
	
	/*Setea el nodo como uno no raiz*/
	public void setAsNotRoot();

	/*Devuelve la representación en bytes del nodo*/
	public byte[] getByteForm();

}
