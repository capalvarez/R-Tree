package structures;

public class OverflowRes {
	private Node original;
	private Node newNode;
	
	public OverflowRes(Node original){
		original = this.original;
	}
	
	public boolean split(){
		return newNode!=null;
	}
	
	public void setNewNode(Node n){
		newNode = n;
	}
	
	public Node getNode(){
		return original;
	}
	
	public Node getNewNode(){
		return newNode;
	}
	
}
