package tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import structures.*;

public class RSTree {
	/*Parametros a probar*/
	private int t;
	private int m;
	private int p;
		
	public Node root;
	public int height = 0;
	
	/*Variable que guarda los niveles que he visitado para esta insercion (insercion con reinsert)*/
	private LinkedList<Integer> levelsVisited = new LinkedList<Integer>();
		
	/*Inicializa un nuevo R*-Tree*/	
	public RSTree(int t, int m, int p){
		this.t = t;
		this.m = m;
		this.p = p; 
		
		root = new LeafNode();
		root.setAsRoot();
	}
	
	public RSTree(int t, int m, int p, Node r){
		this.t = t;
		this.m = m;
		this.p = p; 
		
		root = r;
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
		
		NodeElem newNE = new NodeElem();
		newNE.setRectangle(r);
		c.getNode().getNodeList().add(newNE);
				
		if(c.getNode().getEntryCount()>2*t){
			Node[] newNodes = split(c.getNode());		
			c.setNode(newNodes[0]);			
			adjustTreeSplit(c,newNodes[1]);
		}else{		
			adjustTree(c);
		}
	}

	public NodeFamily chooseSubtree(NodeFamily family, Rectangle r){
		NodeElem e = new NodeElem();
		e.setRectangle(r);
		
		return chooseSubtree(family,e,getHeight());
	}
	
	public NodeFamily chooseSubtree(NodeFamily family, NodeElem e, int level){
		if(family.getNode().isLeaf() || level==0){
			return family;
		}
		
		LinkedList<NodeElem> childList = family.getNode().getNodeList();
		
		/*Los hijos son hojas, basta verificar que el primero lo sea*/
		if(childList.get(0).getNode().isLeaf()){
			Integer[] indexOverlap = getMinOverlap(childList,e.getRectangle());
			
			/*Si existe mas de un indice con el overlap minimo, entonces hay empate*/
			if(indexOverlap.length>1){
				LinkedList<NodeElem> minAreaList = new LinkedList<NodeElem>();
				for(int j = 0; j<indexOverlap.length; j++){
					minAreaList.add(childList.get(indexOverlap[j]));
				}
				
				Integer[] indexArea = getMinAreaEnlarg(minAreaList,e.getRectangle());
				
				/*Si hay empate nuevamente, se decide buscando el rectangulo con menor area*/
				if(indexArea.length>1){
					LinkedList<NodeElem> lastList = new LinkedList<NodeElem>();
					
					for(int j = 0; j<indexArea.length; j++){
						lastList.add(minAreaList.get(indexArea[j]));
					}

					family.addDesc(family.getNode());
					family.setNode(getMinArea(lastList));
					
					return chooseSubtree(family,e,level-1);
				}else{
					family.addDesc(family.getNode());
					family.setNode(minAreaList.get(indexArea[0]).getNode());
					
					return chooseSubtree(family,e,level-1);
				}
			}else{
				family.addDesc(family.getNode());
				family.setNode(childList.get(indexOverlap[0]).getNode());
				
				return chooseSubtree(family,e,level-1);
			}		
		}else{
			Integer[] indexArea = getMinAreaEnlarg(childList,e.getRectangle());
			LinkedList<NodeElem> minAreaList = new LinkedList<NodeElem>();
			
			for(int j = 0; j<indexArea.length; j++){
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
				
				return chooseSubtree(family,e,level-1);
			}else{
				family.addDesc(family.getNode());
				family.setNode(minAreaList.get(0).getNode());
				
				return chooseSubtree(family,e,level-1);
			}
		}	
	}
	
	/*Entrega un arreglo con los indices de los elementos que tengan el minimo overlap*/
	public Integer[] getMinOverlap(LinkedList<NodeElem> children, Rectangle r){
		float overlap = Float.MAX_VALUE;
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int i = 0;  i < children.size(); i++){
			NodeElem child = children.get(i);
			float newOverlap = child.overlapEnlargement(r,children);
			
			if(newOverlap<overlap){
				index = new ArrayList<Integer>();
				index.add(i);
				overlap = newOverlap;		
			}else{
				if(newOverlap==overlap){
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
			
			NodeElem eNN = new NodeElem(n2,getBoundingRectangle(n2.getNodeList()));
		
			if(p.getEntryCount()<2*t){
				p.getNodeList().add(eNN);
				fam.setNode(p);
				
				adjustTree(fam);
				return;
				
			}else{
				p.getNodeList().add(eNN);
				Node[] newNodes = split(p);
				n = newNodes[0];
				n2 = newNodes[1];		
			}
		}
		/* Si salí del loop sin haber escapado a adjustTree, es porque la raiz fue dividida*/
		Node newRoot = new InnerNode();
		NodeElem e1 = new NodeElem();
		NodeElem e2 = new NodeElem();
		
		e1.setChild(n);
		e1.adjustRectangle();
		
		e2.setChild(n2);
		e2.adjustRectangle();
		
		/*Los hijos ya no son la raiz*/
		n.setAsNotRoot();
		n2.setAsNotRoot();
		newRoot.setAsRoot();
		
		LinkedList<NodeElem> rootChildren = new LinkedList<NodeElem>();
		rootChildren.add(e1);
		rootChildren.add(e2);
		newRoot.setNodeList(rootChildren);
		
		root = newRoot;
		height++;
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
				
		LinkedList<NodeElem> infoToSplit = n.getNodeList();
		
		sortByDimMin(infoToSplit,dim);
		List<NodeElem> lNode1 = infoToSplit.subList(0, m + k - 1);
		List<NodeElem> lNode2 = infoToSplit.subList(m + k - 1, infoToSplit.size());

		LinkedList<NodeElem> lN1 = new LinkedList<NodeElem>();
		lN1.addAll(lNode1);

		LinkedList<NodeElem> lN2 = new LinkedList<NodeElem>();
		lN2.addAll(lNode2);

		/* El tipo de los nodos a retornar es dependiente del nodo original */
		if (n.isLeaf()) {
			Node[] returnArray = new LeafNode[2];
			returnArray[0] = n;
			n.setNodeList(lN1);
			returnArray[1] = new LeafNode(lN2);

			return returnArray;

		} else {
			Node[] returnArray = new InnerNode[2];
			returnArray[0] = n;
			n.setNodeList(lN1);
			returnArray[1] = new InnerNode(lN2);

			return returnArray;
		}
	}
	
	public int chooseSplitAxis(Node n){
		/*Consideramos solamente dos dimensiones*/
		float[] sum = new float[2];
		
		for(int d = 0; d<2; d++){
			LinkedList<NodeElem> list = n.getNodeList();		
			sortByDimMin(list,d);
				
			for(int k=1; k<=(2*t-2*m+2); k++){
				List<NodeElem> l1 = list.subList(0,m + k - 1);
				List<NodeElem> l2 = list.subList(m + k - 1,list.size());				
			
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
						
				sum[d] = sum[d] + MBR1.getMargin() + MBR2.getMargin();	
			}
			
			sortByDimMax(list,d);
				
			for(int k=1; k<=(2*t-2*m+2); k++){
				List<NodeElem> l1 = list.subList(0,m + k - 1);
				List<NodeElem> l2 = list.subList(m + k - 1,list.size());
			
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
		LinkedList<NodeElem> list = n.getNodeList(); 
		sortByDimMin(list,dim);
		
		float inter = Float.MAX_VALUE;
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int k = 1;k<=(2*t-2*m+2); k++){
			List<NodeElem> l1 = list.subList(0, m + k -1);
			List<NodeElem> l2 = list.subList(m + k -1, list.size());
			
			Rectangle MBR1 = getBoundingRectangle(l1);
			Rectangle MBR2 = getBoundingRectangle(l2);
			
			float newInter;
			if(MBR1.intersection(MBR2)!=null){
				newInter = MBR1.intersection(MBR2).getArea();
			}else{
				newInter = 0;
			}		 
					
			if(newInter<inter){
				index = new ArrayList<Integer>();
				index.add(k);
				inter = newInter;			
			}else{
				if(newInter==inter)
					index.add(k);
			}
		}
		
		/*Caso en que hay empate en el area de la interseccion de los rectangulos*/
		if(index.size()>1){
			int index2 = 0;
			float areaTot = Float.MAX_VALUE;
			
			for(int i = 0;i<index.size();i++){
				List<NodeElem> l1 = list.subList(0, m + index.get(i) -2);
				List<NodeElem> l2 = list.subList(m + index.get(i) -1, list.size());
				
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
	public void sortByDimMax(LinkedList<NodeElem> l, final int dim){
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
	public Rectangle getBoundingRectangle(List<NodeElem> list){
		Rectangle bound = list.get(0).getRectangle();
		
		for(int i=1; i<list.size();i++){
			Rectangle newR = list.get(i).getRectangle();
			if(!bound.contains(newR)){
				bound = bound.union(newR);
			}
		}
		
		return bound;	
	}
		
	/*Insertar un rectangulo usando el algoritmo de reinsert en caso de overflow*/
	public void insertReinsertRectangle(Rectangle r){
		NodeElem newR = new NodeElem();
		newR.setRectangle(r);
		insertData(newR,0);
	}
	
	/*Insertar un nodo a una cierta altura de forma obligatoria*/
	public void insertData(NodeElem e, int level){
		NodeFamily fam = new NodeFamily();
		chooseSubtree(fam,e,level);
		
		Node n = fam.getNode(); 
		if(n.getEntryCount()<2*t){
			n.getNodeList().add(e);
			
			/*Ajustar el MBR del nodeElem correspondiente al nodo donde inserte*/
			NodeElem eN = fam.getParent().findEntry(n);
			eN.adjustRectangle();
		}else{
			OverflowRes ovr = overflowTreatment(n,level);
			
			/*Si flag es verdadera, entonces hubo split y es necesario propagarlo hacia
			 arriba*/
			if(ovr.split()){
				propagateSplit(fam,ovr);
			}
		}
		
		Node p = fam.getParent();
		while(!n.isRoot()){
			p.findEntry(n).adjustRectangle();
			
			n = p;	
			p = fam.getParent();	
		}
		levelsVisited = new LinkedList<Integer>();
	}
		
	public OverflowRes overflowTreatment(Node n, int level){
		OverflowRes res = new OverflowRes(n);
				
		if(!n.isRoot()){
			if(!levelsVisited.contains(level)){
				//reinsert(n,level);
			}else{
				
			}
		}
		
		return null;
	}
	
	public void propagateSplit(NodeFamily fam, OverflowRes over){
		Node ll = over.getNewNode();
		Node p = fam.getParent();
		
		/*Crear una nueva entrada a insertar en el padre*/
		NodeElem eLL = new NodeElem();
		eLL.setChild(ll);
		eLL.setRectangle(getBoundingRectangle(ll.getNodeList()));
		
		p.getNodeList().add(eLL);
		
		/*Reviso si hay overflowy propago de ser necesario*/
		if(p.getEntryCount()>2*t){
			OverflowRes res = overflowTreatment(p,fam.getTreeHeight());
			
			if(res.split()){
				propagateSplit(fam,res);
			}	
		}	
	}
		
	public void reinsert(Node n, Rectangle mbr){
		LinkedList<NodeElem> list = n.getNodeList();
		sortByDistanceCenter(list, mbr);
		
		List<NodeElem> deleteList = list.subList(0,p);
		int[] height = new int[p];
				
		for(int i=0; i<deleteList.size(); i++){
			height[i] = deleteEntry(deleteList.get(i));
		}
		
		for(int i=0; i<deleteList.size(); i++){
			insertData(deleteList.get(i),height[i]);
		}
	}
	
	public void sortByDistanceCenter(LinkedList<NodeElem> list, Rectangle mbr){
		final float [] center = mbr.getCenter();
		
		Collections.sort(list, new Comparator<NodeElem>() {
	         @Override
	         public int compare(NodeElem r1, NodeElem r2) {
	        	 float diff = r1.getRectangle().distance(center) - r2.getRectangle().distance(center); 
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
	
	public void deleteRectangle(Rectangle r){
		NodeElem entry = new NodeElem();
		entry.setRectangle(r);
		
		deleteEntry(entry);
	}
	
	/*Eliminar una entrada*/
	public int deleteEntry(NodeElem r){
		NodeFamily newFamily = new NodeFamily();
		newFamily.setNode(root);
		NodeFamily cont = searchNode(r,newFamily);
		
		/*Si el rectangulo pedido no esta en el arbol, no hacemos nada*/
		if(cont==null){
			return -1;
		}
		
		/*Si encuentro un nodo que contenga la entrada, lo elimino de la lista*/
		cont.getNode().getNodeList().remove(r);
		
		/*Propago los cambios*/
		condenseTree(cont);
		
		/*Si luego de condensar el arbol, la raiz me queda con un solo elemento*/
		if(root.getEntryCount()==1){
			root = (Node)root.getNodeList().get(0);
			root.setAsRoot();
		}
		
		return cont.getTreeHeight();

	}
	
	/*Busca en el arbol el nodo que contiene la entrada entregada*/
	public NodeFamily searchNode(NodeElem e, NodeFamily fam){
		Node t = fam.getNode();		
		
		if(t.isLeaf()){
			LinkedList<NodeElem> rectangleList = t.getNodeList();
			
			for (int i = 0; i < rectangleList.size(); i++) {
				if(rectangleList.get(i).equals(e)){
					return fam;
				}
			}
			
			/*En el caso en que recorro toda la lista y no encuentro el rectangulo que estoy buscando*/
			return fam;
			
		}else{
			LinkedList<NodeElem> childList = t.getNodeList();
			
			for (int i = 0; i < childList.size(); i++) {
				/*Notar que aqui hay que hacer una modificacion para traer el nodo de memoria externa, por
				 * ahora esta considerado que esta en memoria principal*/
			
				Rectangle rect = childList.get(i).getRectangle();
				
				if(rect.contains(e.getRectangle())){
					fam.setNode(childList.get(i).getNode());
					NodeFamily result = searchNode(e,fam);
				
					if(result.getNode()!=null){
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
				parent.getNodeList().remove(entryN);
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
			Node eliminated = deleteList.get(i);
			int heightElim = heightList.get(i); 
			
			LinkedList<NodeElem> childList = eliminated.getNodeList();
			for(int j=0;j<childList.size();j++){
				insertData(childList.get(i),heightElim);
			}
			
		}	
	}
	
	/*Busco todos los rectangulos que intersectan el entregado*/
	public Rectangle[] findRectangle(Rectangle r){
		return getRectangleArray(search(root,r));
	}
	
	/*Busca todos los rectangulos en el arbol que se intersectan con el entregado*/
	public ArrayList<Rectangle> search(Node t, Rectangle r){
		ArrayList<Rectangle> returnList = new ArrayList<Rectangle>();
		
		if(t.isLeaf()){
			LinkedList<NodeElem> rectangleList = t.getNodeList();
			
			for (int i = 0; i < rectangleList.size(); i++) {
				if(rectangleList.get(i).getRectangle().intersects(r)){
					returnList.add(rectangleList.get(i).getRectangle());
				}
			}

		}else{
			LinkedList<NodeElem> childList = t.getNodeList();
			
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
