package tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import structures.*;

public class RSTree {
	private int t;
	private int m;
	private Node root;
	
	/*Inicializa un nuevo R*-Tree*/	
	public RSTree(int t){
		this.t = t;
		root = new LeafNode();
	}
	
	/*Insertar un rectangulo usando el algoritmo de split en caso de overflow*/
	public void insertSplitRectangle(Rectangle r){
		/*Busco el nodo hoja en el que debo insertar el nuevo rectangulo*/
		//NodeFamily c = chooseSubtree(root,r);
		//c.getNode().getList().add(r);
		
		
		
		
	}
	
	
	
	
	/*Insertar un rectangulo usando el algoritmo de reinsert en caso de overflow*/
	/*public void insertReinsertRectangle(Rectangle r){*/
		/*Busco el nodo hoja en el que debo insertar el nuevo rectangulo*/
		/*NodeFamily c = chooseSubtree(root,r);
		c.getNode().getList().add(r);
		
		
		
		
	}*/
	
	/*Realiza split de un nodo, devolviendo los dos nodos en los que se divide*/
	public Node[] split(Node n){
		int dim = chooseSplitAxis(n);
		int k = chooseSplitIndex(n,dim);
		
		
		
		
	}
	
	public int chooseSplitAxis(Node n){
		/*Consideramos solamente dos dimensiones*/
		float[] sum = new float[2];
		
		for(int d = 0; d<2; d++){
			LinkedList list = n.getList();
			list = sortByDimMin(list,d);
		
			for(int k=1; k<(2*t-2*m+2); k++){
				List l1 = list.subList(0,k-1);
				List l2 = list.subList(k,list.size());
			
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
				
				sum[d] = sum[d] + MBR1.getMargin() + MBR2.getMargin();	
			}
			
			list = sortByDimMax(list,d);
			
			for(int k=1; k<(2*t-2*m+2); k++){
				List l1 = list.subList(0,k-1);
				List l2 = list.subList(k,list.size());
			
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
				
				sum[d] = sum[d] + MBR1.getMargin() + MBR2.getMargin();	
			}
		}
		
		float sumMin = Float.MAX_VALUE;
		int dim = 0;
		
		for(int d = 0; d<2; d++){
			if(sum[d]<sumMin){
				sumMin = sum[d];
				dim = d;
			}
		}		
		return dim;
	}
	
	public int chooseSplitIndex(Node n, int dim){
		return 0;
	}
		
	/*Ordena la lista segun los valores de la dimension dim de menor a mayor*/ 
	public LinkedList sortByDimMin(LinkedList l, int dim){
		return null;
	}
	
	/*Ordena la lista segun los valores de la dimension dim de mayor a menor*/ 
	public LinkedList sortByDimMax(LinkedList l, int dim){
		return null;
	}
	
	/*Entrega el rectangulo que cubre a todos los elementos de la lista*/
	public Rectangle getBoundingRectangle(List list){
		return null;	
	}
	
	/*Eliminar un rectangulo*/
	public void deleteRectangle(Rectangle r){
		NodeFamily newFamily = new NodeFamily();
		NodeFamily cont = searchNode(root,r,newFamily);
		
		/*Si el rectangulo pedido no esta en el arbol, no hacemos nada*/
		if(cont==null){
			return;
		}
		
		/*Si encuentro un nodo que contenga el rectangulo, lo elimino de la lista*/
		cont.getNode().getList().remove(r);
		
		/*Propago los cambios*/
		condenseTree(cont);
		
		/*Si luego de condensar el arbol, la raiz me queda con un solo elemento*/
		if(root.getEntryCount()==1)
			root = (Node)root.getList().get(0);
			root.setAsRoot();

	}
	
	/*Busca en el arbol el nodo que contiene al rectangulo entregado*/
	public NodeFamily searchNode(Node t, Rectangle r, NodeFamily fam){
				
		if(t.isLeaf()){
			LinkedList<Rectangle> rectangleList = t.getList();
			
			for (int i = 0; i < rectangleList.size(); i++) {
				if(rectangleList.get(i).equals(r)){
					fam.setNode(t);
					return fam;
				}
			}
			
			/*En el caso en que recorro toda la lista y no encuentro el rectangulo que estoy buscando*/
			return fam;
			
		}else{
			LinkedList<NodeElem> childList = t.getList();
			
			for (int i = 0; i < childList.size(); i++) {
				/*Notar que aqui hay que hacer una modificacion para traer el nodo de memoria externa, por
				 * ahora esta considerado que esta en memoria principal*/
			
				Rectangle rect = childList.get(i).getRectangle();
				
				if(rect.contains(r)){
					NodeFamily result = searchNode(childList.get(i).getNode(),r, fam);
				
					if(result.getNode()!=null){
						fam.addDesc(childList.get(i).getNode());
						return result;
					}
				}
			}
			
			/*Para el caso en el que sali del for (revise todas las ramas y no encontre el rectangulo)*/
			return fam;		
		}		
	}
	
	/*Propaga los cambios por haber eliminado un nodo de abajo hacia arriba*/
	public void condenseTree(NodeFamily init){
		LinkedList<Node> deleteList = new LinkedList<Node>();
		LinkedList<Integer> heightList = new LinkedList<Integer>();
		Node n = init.getNode();
		
		int height = 0;
		
		while(!n.isRoot()){
			Node parent = init.getParent();
			NodeElem entryN = parent.findEntry(init.getNode());
			
			if(n.getEntryCount()<t){
				parent.getList().remove(entryN);
				deleteList.add(n);
				heightList.add(height);
			}else{
				entryN.adjustRectangle();
			}
			
			height++;
			n = parent;		
		}
		
		/*Etapa de reinsercion*/
		for(int i = 0; i<deleteList.size(); i++){
			insertByHeight(deleteList.get(i),heightList.get(i));
		}	
	}
	
	/*Insertar un nodo a una cierta altura de forma obligatoria*/
	public void insertByHeight(Node n, int height){
		
	}
	
	/*Busco todos los rectangulos que intersectan el entregado*/
	public Rectangle[] findRectangle(Rectangle r){
		return getRectangleArray(search(root,r));
	}
	
	/*Busca todos los rectangulos en el arbol que se intersectan con el entregado*/
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
			LinkedList<NodeElem> childList = t.getList();
			
			for (int i = 0; i < childList.size(); i++) {
				/*Notar que aqui hay que hacer una modificacion para traer el nodo de memoria externa, por
				 * ahora esta considerado que esta en memoria principal*/
			
				Rectangle rect = childList.get(i).getRectangle();
				
				if(rect.intersects(r)){
					returnList.addAll(search(childList.get(i).getNode(),r));
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
	public Node chooseSubtree(Node n, Rectangle r){
		if(n.isLeaf()){
			return n;
		}
		
		LinkedList<NodeElem> childList = n.getList();
		
		/*Los hijos son hojas, basta verificar que el primero lo sea*/
		if(childList.get(0).getNode().isLeaf()){
			Integer[] indexOverlap = getMinOverlap(childList,r);
			
			/*Si existe mas de un indice con el overlap minimo, entonces hay empate*/
			if(indexOverlap.length>1){
				LinkedList<NodeElem> minAreaList = new LinkedList<NodeElem>();
				for(int j = 0; j<indexOverlap.length; j++){
					minAreaList.add(childList.get(indexOverlap[j]));
				}
				
				Integer[] indexArea = getMinAreaEnlarg(minAreaList,r);
				
				/*Si hay empate nuevamente, se decide buscando el rectangulo con menor area*/
				if(indexArea.length>1){
					LinkedList<NodeElem> lastList = new LinkedList<NodeElem>();
					
					for(int j = 0; j<indexArea.length; j++){
						lastList.add(minAreaList.get(indexArea[j]));
					}
					
					return chooseSubtree(getMinArea(lastList),r);
				}else{
					return chooseSubtree(minAreaList.get(indexArea[0]).getNode(),r);
				}
			}else{
				return chooseSubtree(childList.get(indexOverlap[0]).getNode(),r);
			}		
		}else{
			Integer[] indexArea = getMinAreaEnlarg(childList,r);
			
			LinkedList<NodeElem> minAreaList = new LinkedList<NodeElem>();
			for(int j = 0; j<childList.size(); j++){
				minAreaList.add(childList.get(indexArea[j]));
			}
						
			/*Si hay empate, se decide buscando el rectangulo con menor area*/
			if(indexArea.length>1){
				LinkedList<NodeElem> lastList = new LinkedList<NodeElem>();
				
				for(int j = 0; j<indexArea.length; j++){
					lastList.add(minAreaList.get(indexArea[j]));
				}
				
				return chooseSubtree(getMinArea(lastList),r);
			}else{
				return chooseSubtree(minAreaList.get(indexArea[0]).getNode(),r);
			}
		}	
	}
	
	/*Entrega un arreglo con los indices de los elementos que tengan el minimo overlap*/
	public Integer[] getMinOverlap(LinkedList<NodeElem> children, Rectangle r){
		float overlap = Float.MAX_VALUE;
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int i = 0;  i < children.size(); i++){
			NodeElem child = children.get(i);
			
			if(child.getRectangle().overlapEnlargement(r,child.getNode().getList())<overlap){
				index = new ArrayList<Integer>();
				index.add(i);
				overlap = child.getRectangle().overlapEnlargement(r,child.getNode().getList());		
			}else{
				if(child.getRectangle().overlapEnlargement(r,child.getNode().getList())==overlap){
					index.add(i);
				}
			}	
		}
		
		Integer[] iArray = new Integer[index.size()];
		iArray = index.toArray(iArray);
		
		return iArray;	
	}
	
	public Integer[] getMinAreaEnlarg(LinkedList<NodeElem> children, Rectangle r){
		float area = Float.MAX_VALUE;
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int i = 0;  i < children.size(); i++){
			NodeElem child = children.get(i);
			
			if(child.getRectangle().areaEnlargement(r)<area){
				index = new ArrayList<Integer>();
				index.add(i);
				area = child.getRectangle().areaEnlargement(r);		
			}else{
				if(child.getRectangle().areaEnlargement(r)==area){
					index.add(i);
				}
			}	
		}
		
		Integer[] iArray = new Integer[index.size()];
		iArray = index.toArray(iArray);
		
		return iArray;	
	}
	
	public Node getMinArea(LinkedList<NodeElem> children){
		float area = Float.MAX_VALUE;
		int index = 0;
		
		for(int i = 0;  i < children.size(); i++){
			NodeElem child = children.get(i);
			
			if(child.getRectangle().getArea()<area){
				index = i;
				area = child.getRectangle().getArea();		
			}
		}

		return children.get(index).getNode();	
	}
	
	
	
}
