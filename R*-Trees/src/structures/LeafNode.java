package structures;

import java.util.LinkedList;

public class LeafNode implements Node {
	private boolean isRoot;
	private LinkedList<Rectangle> infoList;
			
	public int getEntryCount(){
		return infoList.size();
	}

	public boolean isLeaf(){
		return true;
	}	
	
	/*Insertar un rectangulo en una hoja*/
	public void insertInLeaf(Rectangle r,int t){
		if(this.getEntryCount()<2*t){
			infoList.add(r);		
		}else{
		
			
		}
	}
	
	public LinkedList getList() {
		return infoList;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public NodeElem findEntry(Node n) {
		return null;
	}

	public void setAsRoot() {
		isRoot = true;	
	}

	public void setAsNotRoot() {
		isRoot = false;
	}
	
}
