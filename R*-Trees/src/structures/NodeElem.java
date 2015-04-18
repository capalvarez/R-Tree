package structures;

public class NodeElem {
	private Rectangle boundingRectangle;
	private Node child;
	
	public NodeElem(){}
	
	public NodeElem(Node c, Rectangle r){
		boundingRectangle = r;
		child = c;
	}
	
	public void setChild(Node n){
		child = n;
	}
	
	public void setRectangle(Rectangle r){
		boundingRectangle = r;
	}
	
	public Rectangle getRectangle(){
		return boundingRectangle;
	}
	
	public Node getNode(){
		return child;
	}
	
}
