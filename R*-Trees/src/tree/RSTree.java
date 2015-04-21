package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import structures.*;

public class RSTree {
	private int t;
	private int m;
	private Node root;
	private int height;
	
	/*Inicializa un nuevo R*-Tree*/	
	public RSTree(int t){
		this.t = t;
		root = new LeafNode();
	}
	
	public int getHeight(){
		return height;
	}
	
	/*Insertar un rectangulo usando el algoritmo de split en caso de overflow*/
	public void insertSplitRectangle(Rectangle r){
		/*Busco el nodo hoja en el que debo insertar el nuevo rectangulo*/
		NodeFamily init = new NodeFamily();
		init.setNode(root);
		
		NodeFamily c = chooseSubtree(init,r);
		c.getNode().getList().add(r);
		
		if(c.getNode().getEntryCount()>2*t){
			Node[] newNodes = split(c.getNode());
			c.setNode(newNodes[0]);
			adjustTreeSplit(c,newNodes[1]);
		}else{
			adjustTree(c);
		}
	}

	/*Busca el nodo hoja que contiene el rectangulo entregado*/
	public NodeFamily chooseSubtree(NodeFamily family, Rectangle r){
		if(family.getNode().isLeaf()){
			return family;
		}
		
		LinkedList<NodeElem> childList = family.getNode().getList();
		
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

					family.addDesc(family.getNode());
					family.setNode(getMinArea(lastList));
					
					return chooseSubtree(family,r);
				}else{
					family.addDesc(family.getNode());
					family.setNode(minAreaList.get(indexArea[0]).getNode());
					
					return chooseSubtree(family,r);
				}
			}else{
				family.addDesc(family.getNode());
				family.setNode(childList.get(indexOverlap[0]).getNode());
				
				return chooseSubtree(family,r);
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
				
				family.addDesc(family.getNode());
				family.setNode(getMinArea(lastList));
				
				return chooseSubtree(family,r);
			}else{
				family.addDesc(family.getNode());
				family.setNode(minAreaList.get(indexArea[0]).getNode());
				
				return chooseSubtree(family,r);
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
	
	public void adjustTreeSplit(NodeFamily fam, Node n2){
		Node n = fam.getNode();
		while(!n.isRoot()){
			Node p = fam.getParent();
			NodeElem eN = p.findEntry(n);
			eN.adjustRectangle();
			
			NodeElem eNN = new NodeElem(n2,getBoundingRectangle(n2.getList()));
		
			if(p.getEntryCount()<2*t){
				p.getList().add(eNN);
				fam.setNode(p);
				
				adjustTree(fam);
				return;
				
			}else{
				Node[] newNodes = split(p);
				n = newNodes[0];
				n2 = newNodes[1];		
			}
		}
	}
	
	public void adjustTree(NodeFamily fam){
		Node n = fam.getNode();
		
		while(!n.isRoot()){
			Node p = fam.getParent();
			NodeElem eN = p.findEntry(n);
			
			eN.adjustRectangle();
			n = p;
			
		}		
	}

	/*Realiza split de un nodo, devolviendo los dos nodos en los que se divide*/
	public Node[] split(Node n){
		int dim = chooseSplitAxis(n);
		int k = chooseSplitIndex(n,dim);
				
		/*El tipo de los nodos a retornar es dependiente del nodo original*/
		if(n.isLeaf()){
			LinkedList<Rectangle> infoToSplit = n.getList();
			List<Rectangle> lNode1 = infoToSplit.subList(0, m + k -2);
			List<Rectangle> lNode2 = infoToSplit.subList(m + k - 1, infoToSplit.size());
			
			LinkedList<Rectangle> lN1 = new LinkedList<Rectangle>();
			lN1.addAll(lNode1);
			
			LinkedList<Rectangle> lN2 = new LinkedList<Rectangle>();
			lN1.addAll(lNode2);
						
			Node[] returnArray = new LeafNode[2];			
			returnArray[0] = new LeafNode(lN1);
			returnArray[1] = new LeafNode(lN2);
			
			return returnArray;
		
		}else{
			LinkedList<NodeElem> infoToSplit = n.getList();
			List<NodeElem> lNode1 = infoToSplit.subList(0, m + k -2);
			List<NodeElem> lNode2 = infoToSplit.subList(m + k - 1, infoToSplit.size());
						
			LinkedList<NodeElem> lN1 = new LinkedList<NodeElem>();
			lN1.addAll(lNode1);
			
			LinkedList<NodeElem> lN2 = new LinkedList<NodeElem>();
			lN1.addAll(lNode2);
						
			Node[] returnArray = new InnerNode[2];			
			returnArray[0] = new InnerNode(lN1);
			returnArray[1] = new InnerNode(lN2);
			
			return returnArray;
		}	
	}
	
	public int chooseSplitAxis(Node n){
		/*Consideramos solamente dos dimensiones*/
		float[] sum = new float[2];
		
		for(int d = 0; d<2; d++){
			LinkedList list = n.getList();
			sortByDimMin(list,d);
		
			for(int k=1; k<(2*t-2*m+2); k++){
				List l1 = list.subList(0,m + k - 2);
				List l2 = list.subList(m + k - 1,list.size());
			
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
				
				sum[d] = sum[d] + MBR1.getMargin() + MBR2.getMargin();	
			}
			
			sortByDimMax(list,d);
			
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
		LinkedList list = n.getList(); 
		sortByDimMin(list,dim);
		
		float inter = Float.MAX_VALUE;
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int k = 1;k<(2*t-2*m+2); k++){
			List l1 = list.subList(0, m + k -2);
			List l2 = list.subList(m + k -1, list.size());
			
			Rectangle MBR1 = getBoundingRectangle(l1);
			Rectangle MBR2 = getBoundingRectangle(l2);
			
			if(MBR1.intersection(MBR2).getArea()<inter){
				index = new ArrayList<Integer>();
				index.add(k);
				inter = MBR1.intersection(MBR2).getArea();			
			}else{
				if(MBR1.intersection(MBR2).getArea()==inter)
					index.add(k);
			}
		}
		
		/*Caso en que hay empate en el area de la interseccion de los rectangulos*/
		if(index.size()>1){
			int index2 = 0;
			float areaTot = Float.MAX_VALUE;
			
			for(int i = 0;i<index.size();i++){
				List l1 = list.subList(0, m + index.get(i) -2);
				List l2 = list.subList(m + index.get(i) -1, list.size());
				
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
				
				float sum = MBR1.getArea() + MBR2.getArea();
				
				if(sum<areaTot){
					areaTot = sum;
					index2 = index.get(i);
				}			
			}
			
			return index.get(index2);			
		}
		
		return index.get(0);
	}
		
	/*Ordena la lista segun los valores de la dimension dim de menor a mayor*/ 
	public void sortByDimMin(LinkedList<NodeElem> l, final int dim){
		Collections.sort(l, new Comparator<NodeElem>() {
	         @Override
	         public int compare(NodeElem r1, NodeElem r2) {
	        	 float diff = r1.getMinDim(dim) - r2.getMinDim(dim); 
	             if(diff<0){
	             	return -1;
	             }else{
	            	if(diff==0)
	            		return 0;
	            	else
	            		return 1;
	             }
	         }
	     });	
	}
	
	/*Ordena la lista segun los valores de la dimension dim de mayor a menor*/ 
	public void sortByDimMax(LinkedList l, final int dim){
		Collections.sort(l, new Comparator<NodeElem>() {
	         @Override
	         public int compare(NodeElem r1, NodeElem r2) {
	        	 float diff = r1.getMaxDim(dim) - r2.getMaxDim(dim); 
	             if(diff<0){
	             	return -1;
	             }else{
	            	if(diff==0)
	            		return 0;
	            	else
	            		return 1;
	             }
	         }
	     });	
	}
	
	/*Entrega el rectangulo que cubre a todos los elementos de la lista*/
	public Rectangle getBoundingRectangleNE(List<NodeElem> list){
		Rectangle bound = list.get(0).getRectangle();
		
		for(int i=1; i<list.size();i++){
			Rectangle newR = list.get(i).getRectangle();
			if(!bound.contains(newR)){
				bound = bound.union(newR);
			}
		}
		
		return bound;	
	}
	
	/*Entrega el rectangulo que cubre a todos los elementos de la lista*/
	public Rectangle getBoundingRectangleR(List<Rectangle> list){
		Rectangle bound = list.get(0);
		
		for(int i=1; i<list.size();i++){
			Rectangle newR = list.get(i);
			if(!bound.contains(newR)){
				bound = bound.union(newR);
			}
		}
		
		return bound;	
	}
	
	/*Insertar un rectangulo usando el algoritmo de reinsert en caso de overflow*/
	public void insertReinsertRectangle(Rectangle r){
		insertData(r,0);
	}

	public void insertData(Rectangle r, int level){
		
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
	public Rectangle[] findRectangle(Rectangle r){ArrayList<Integer> index = new ArrayList<Integer>();
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
	
	
	
	
}
