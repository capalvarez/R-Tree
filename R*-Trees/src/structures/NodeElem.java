package structures;

import java.util.LinkedList;

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
	
	public void adjustRectangle(){
		float top = Float.MIN_VALUE;
		float bottom = Float.MAX_VALUE;
		float left = Float.MAX_VALUE;
		float right = Float.MIN_VALUE;
		
		if(!child.isLeaf()){
			LinkedList<NodeElem> infoList = child.getList();
		
			for(int i = 0; i < infoList.size(); i++){
				Rectangle r = infoList.get(i).getRectangle();
				
				if(r.top()>top){
					top = r.top();			
				}
				
				if(r.bottom()<bottom){
					bottom = r.bottom();
				}
				
				if(left>r.left()){
					left = r.left();	
				}
				
				if(right<r.right()){
					right = r.right();
				}	
			}	
		}else{
			/*El nodo hijo es una hoja, por lo tanto guarda una lista de rectangulos*/
			
			LinkedList<Rectangle> infoList = child.getList();
			
			for(int i = 0; i < infoList.size(); i++){
				Rectangle r = infoList.get(i);
				
				if(r.top()>top){
					top = r.top();			
				}
				
				if(r.bottom()<bottom){
					bottom = r.bottom();
				}
				
				if(left>r.left()){
					left = r.left();	
				}
				
				if(right<r.right()){
					right = r.right();
				}	
			}		
		}
		boundingRectangle.setCoordinates(left,right,top,bottom);	
	}
	
}
