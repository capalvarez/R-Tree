package structures;

import java.util.LinkedList;

public class LeafNode implements Node {
	private Rectangle boundingRectangle;
	private LinkedList<Rectangle> infoList;
		
	public int getEntryCount(){
		return infoList.size();
	}

	public boolean isLeaf(){
		return true;
	}
	
	public void adjustRectangle(){
		float top = Float.MIN_VALUE;
		float bottom = Float.MAX_VALUE;
		float left = Float.MAX_VALUE;
		float right = Float.MIN_VALUE;
			
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
		
		boundingRectangle.setCoordinates(left,right,top,bottom);	
	}
	
}
