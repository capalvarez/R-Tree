package structures;

import java.util.Stack;

public class NodeFamily {
	private Node node = null;
	private Stack<Node> family = new Stack<Node>(); 
	
	public NodeFamily(){}
	
	public void setNode(Node n){
		node = n;
	}
	
	public Node getNode(){
		return node;
	}
	
	public void addDesc(Node child){
		family.push(child);
	}
	
	public Node getParent(){
		return family.pop();
	}
	
	public int getTreeHeight(){
		return family.size();
	}

}
