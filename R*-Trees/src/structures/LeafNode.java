package structures;

import java.util.LinkedList;

public class LeafNode implements Node {
	private boolean isRoot;
	
	private LinkedList<NodeElem> infoList = new LinkedList<NodeElem>();
	
	public LeafNode(){}
	
	public LeafNode(LinkedList<NodeElem> info){
		infoList = info;
	}
	
	public int getEntryCount(){
		return infoList.size();
	}

	public boolean isLeaf(){
		return true;
	}	
		
	public LinkedList<NodeElem> getNodeList() {
		return infoList;
	}
	
	public void setNodeList(LinkedList<NodeElem> l) {
		infoList = l;	
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

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHeight() {
		// TODO Auto-generated method stub
		
	}

	
	
}
