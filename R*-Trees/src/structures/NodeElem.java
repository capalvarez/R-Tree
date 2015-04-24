package structures;

import java.util.LinkedList;

public class NodeElem {
	private Rectangle boundingRectangle;
	private Node child;
	
	public NodeElem(){
		boundingRectangle = new Rectangle(0,0,0,0);
	}
	
	public NodeElem(Node c, Rectangle r){
		boundingRectangle = r;
		child = c;
	}
	
	public float getMinDim(int dim){
		return boundingRectangle.getMinDim(dim);
	}
	
	public float getMaxDim(int dim){
		return boundingRectangle.getMaxDim(dim);
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
		
		LinkedList<NodeElem> infoList = child.getNodeList();
		
		for (int i = 0; i < infoList.size(); i++) {
			Rectangle r = infoList.get(i).getRectangle();

			if (r.top() > top) {
				top = r.top();
			}

			if (r.bottom() < bottom) {
				bottom = r.bottom();
			}

			if (left > r.left()) {
				left = r.left();
			}

			if (right < r.right()) {
				right = r.right();
			}
		}
		
		boundingRectangle.setCoordinates(left,right,bottom,top);	
	}
	
	public boolean equals(NodeElem e){
		return e.getRectangle().equals(this.getRectangle());
	}	

	/*Calcula el overlap que aumentaria el rectangulo y al incluir el rectangulo entregado*/
	public float overlapEnlargement(Rectangle r, LinkedList<NodeElem> siblingList){	
		/*Calcular el valor del overlap sin incluir el nuevo rectangulo*/
		float oldOverlap = boundingRectangle.overlap(siblingList);
		
		/*Incluir el nuevo rectangulo para calcular el overlap*/		
		NodeElem nE = new NodeElem();
		nE.setRectangle(r);
		child.getNodeList().add(nE);
		
		/*Ajusto el MBR para representar que eh incluido un nuevo rectangulo en el hijo*/
		adjustRectangle();
				
		float newOverlap = boundingRectangle.overlap(siblingList);
				
		/*Sacar el elemento agregado y reajustar el rectangulo*/
		child.getNodeList().remove(nE);
		adjustRectangle();
		
		return Math.abs(newOverlap - oldOverlap);
	}

}
