package structures;

import java.util.LinkedList;

public class InnerNode implements Node {
	private boolean isRoot;
	private LinkedList<NodeElem> childList;
		
	public InnerNode(){
		childList = new LinkedList<NodeElem>();
	}
	
	public InnerNode(LinkedList<NodeElem> list){
		childList = list;
	}
	
	public int getEntryCount(){
		return childList.size();
	}

	public boolean isLeaf(){
		return false;
	}

	public LinkedList<NodeElem> getNodeList() {
		return childList;
	}
	
	public void setNodeList(LinkedList<NodeElem> l) {
		childList = l;	
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
	
	public void setAsRoot(){
		isRoot = true;
	}
	
	public void setAsNotRoot(){
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
