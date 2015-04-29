package structures;

public class NodeElem {
	private Rectangle boundingRectangle;
	private long child;
	
	public NodeElem(){
		boundingRectangle = new Rectangle(0,0,0,0);
	}
	
	public NodeElem(long c, Rectangle r){
		boundingRectangle = r;
		child = c;
	}
	
	public float getMinDim(int dim){
		return boundingRectangle.getMinDim(dim);
	}
	
	public float getMaxDim(int dim){
		return boundingRectangle.getMaxDim(dim);
	}
	
	public void setChild(long n){
		child = n;
	}
	
	public void setRectangle(Rectangle r){
		boundingRectangle = r;
	}
	
	public Rectangle getRectangle(){
		return boundingRectangle;
	}
	
	public long getNode(){
		return child;
	}
		
	public boolean equals(NodeElem e){
		return e.getRectangle().equals(this.getRectangle());
	}	
}
