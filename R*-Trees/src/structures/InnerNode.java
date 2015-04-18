package structures;

import java.util.LinkedList;

public class InnerNode implements Node {
	private boolean isRoot;
	private LinkedList<NodeElem> childList;
		
	public InnerNode(){
		childList = new LinkedList<NodeElem>();
	}
	
	public int getEntryCount(){
		return childList.size();
	}

	public boolean isLeaf(){
		return false;
	}
	
	/*Agrega un hijo al nodo, hay que actualizar el rectangulo que los contiene*/
	public void addChild(Node newChild){
		
	}
			
	/*Elimina hijo del nodo, hay que actualizar el rectangulo que los contiene*/
	public void deleteChild(Node oldChild){
		/*No estoy muy segura de que voy a recibir para buscar el que se elimina*/
	}

	public LinkedList getList() {
		return childList;
	}

	public NodeElem findEntry(Node n){
		for(int i = 0; i< childList.size(); i++){
			Node child = childList.get(i).getNode();
			
			if(child.equals(n)){
				return childList.get(i);
			}
		}
		
		return null;
	}

	public boolean isRoot() {
		return isRoot;
	}
	
	
}
