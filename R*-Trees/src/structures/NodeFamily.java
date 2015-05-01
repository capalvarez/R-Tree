package structures;

import java.util.Stack;

public class NodeFamily {
	private Node node = null;
	private Stack<Node> family = new Stack<Node>();
	private Stack<Integer> index = new Stack<Integer>();
	
	public NodeFamily(){}
	
	private NodeFamily(Stack<Node> fam, Stack<Integer> in){
		family = fam;
		index = in;
	}
	
	public void setNode(Node n){
		node = n;
	}
	
	public Node getNode(){
		return node;
	}
	
	public void addDesc(Node child, int i){
		family.push(child);
		index.push(i);
	}
	
	public Node getParent(){
		return family.pop();
	}
	
	public int getNextIndex(){
		return index.pop(); 
	}
	
	public Node viewParent(){
		return family.peek();
	}
	
	public Integer viewIndex(){
		return index.peek();
	}
	
	public int getTreeHeight(){
		return family.size();
	}

	public NodeFamily getClone(){
		NodeFamily copy = new NodeFamily((Stack<Node>) family.clone(),(Stack<Integer>) index.clone());
		copy.setNode(node);
		
		return copy;
	}

}
