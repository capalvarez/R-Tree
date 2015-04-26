package structures;

import java.util.Stack;

public class NodeFamily {
	private Node node = null;
	private Stack<Node> family = new Stack<Node>(); 
	
	public NodeFamily(){}
	
	private NodeFamily(Stack<Node> fam){
		family = fam;
	}
	
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
	
	public Node viewParent(){
		return family.peek();
	}
	
	public int getTreeHeight(){
		return family.size();
	}

	public NodeFamily getClone(){
		NodeFamily copy = new NodeFamily((Stack<Node>) family.clone());
		copy.setNode(node);
		
		return copy;
	}

}
