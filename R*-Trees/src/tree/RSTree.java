package tree;

import java.util.ArrayList;
import java.util.LinkedList;

import structures.*;

public class RSTree {
	private int t;
	private Node root;
	
	/*Inicializa un nuevo R*-Tree*/	
	public RSTree(int t){
		this.t = t;
		root = new LeafNode();
	}
	
	/*Insertar un rectangulo*/
	public void insertRectangle(Rectangle r){
		
	}
	
	/*Eliminar un rectangulo*/
	public void deleteRectangle(Rectangle r){
		
	}
	
	/*Busco todos los rectangulos que intersectan el entregado*/
	public Rectangle[] findRectangle(Rectangle r){
		return getRectangleArray(search(root,r));
	}
	
	public ArrayList<Rectangle> search(Node t, Rectangle r){
		ArrayList<Rectangle> returnList = new ArrayList<Rectangle>();
		
		if(t.isLeaf()){
			LinkedList<Rectangle> rectangleList = t.getList();
			
			for (int i = 0; i < rectangleList.size(); i++) {
				if(rectangleList.get(i).intersects(r)){
					returnList.add(rectangleList.get(i));
				}
			}

		}else{
			LinkedList<Node> childList = t.getList();
			
			for (int i = 0; i < childList.size(); i++) {
				/*Notar que aqui hay que hacer una modificacion para traer el nodo de memoria externa, por
				 * ahora esta considerado que esta en memoria principal*/
			
				Rectangle rect = childList.get(i).getRectangle();
				
				if(rect.intersects(r)){
					returnList.addAll(search(childList.get(i),r));
				}
			}
		}
		
		return returnList;	
	}
	
	public Rectangle[] getRectangleArray(ArrayList<Rectangle> rList){	
		Rectangle[] rArray = new Rectangle[rList.size()];
		rArray = rList.toArray(rArray);
		
		return rArray;
	}
	
	/*Busca el nodo hoja que contiene el rectangulo entregado*/
	
}
