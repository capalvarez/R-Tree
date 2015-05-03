package tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import externalMemory.MemoryMng;

import structures.*;

public class RSTree {
	/*Parametros a probar*/
	private int M;
	private int m;
	private int p;
		
	public Node root;
	public int height = 0;
	public MemoryMng mem;
	private long lastPos = 0;
	private int nodeSize;
	
	/*Variable que guarda los niveles que he visitado para esta insercion (insercion con reinsert)*/
	private Set<Integer> levelsVisited = new HashSet<Integer>();
		
	/*Inicializa un nuevo R*-Tree*/	
	public RSTree(int M, int m, int p, int buffSize, int nodeSize, String fileName) throws IOException{
		this.M = M;
		this.m = m;
		this.p = p; 
		this.nodeSize = nodeSize;
		
		root = new LeafNode(M);
		root.setAsRoot();
	
		mem = new MemoryMng(buffSize,nodeSize,M,fileName);
	}
	
	public int getHeight(){
		return height;
	}
	
	public long getNewPos(){
		lastPos = lastPos + nodeSize;
		
		return lastPos;
	}
	
	public int getMemoryAccess(){
		return mem.getMemoryAccess();
	}
	
	public void resetMemoryAccess(){
		mem.resetMemoryAccess();
	}
	 
	public Integer[] loop(int n) {
	    Integer[] a = new Integer[n];
	    
	    for(int i = 0; i < n; ++i){
	        a[i] = i;
	    }
	    
	    return a;
	}
	
	/*Insertar un rectangulo usando el algoritmo de split en caso de overflow*/
	public void insertSplitRectangle(Rectangle r) throws IOException{
		/*Busco el nodo hoja en el que debo insertar el nuevo rectangulo*/
		NodeFamily init = new NodeFamily();
		init.setNode(root);
		
		NodeElem e = new NodeElem(-1,r);
		NodeFamily c = chooseSubtree(init,e);
		
		c.getNode().addChild(e);
		
		if(c.getNode().getEntryCount()>M){
			Node[] newNodes = split(c.getNode());
			c.setNode(newNodes[0]);			
			adjustTreeSplit(c,newNodes[1]);
		}else{		
			adjustTree(c);
		}
		
	}

	public NodeFamily chooseSubtree(NodeFamily family, NodeElem e) throws IOException{	
		return chooseSubtree(family,e,getHeight());
	}
	
	public NodeFamily chooseSubtree(NodeFamily family, NodeElem e, int level) throws IOException{
		if(family.getNode().isLeaf() || level==0){			
			return family;
		}
		
		LinkedList<NodeElem> childList = family.getNode().getNodeList();
		Node firstChild = mem.loadNode(family.getNode().getChildPos(0));
		
		if(childList.size()==0){
			System.out.println("ouch");
		}
		
		/*Los hijos son hojas, basta verificar que el primero lo sea*/
		if(firstChild.isLeaf()){
			Integer[] indexOverlap = getMinOverlap(childList,e.getRectangle());
			if(indexOverlap.length==0){
				System.out.println("ouchis");
			}
			
			/*Si existe mas de un indice con el overlap minimo, entonces hay empate*/
			if(indexOverlap.length>1){	
				Integer[] indexArea = getMinAreaEnlarg(indexOverlap,childList,e.getRectangle());
				
				/*Si hay empate nuevamente, se decide buscando el rectangulo con menor area*/
				if(indexArea.length>1){
					int index = getMinArea(family.getNode(),indexArea);
					
					family.addDesc(family.getNode(),index);
					Node node = mem.loadNode(family.getNode().getChildPos(index));
					family.setNode(node);
					
					return chooseSubtree(family,e,level-1);
				}else{
					family.addDesc(family.getNode(),indexArea[0]);
					Node node = mem.loadNode(family.getNode().getChildPos(indexArea[0]));
					family.setNode(node);
					
					return chooseSubtree(family,e,level-1);
				}
			}else{
				family.addDesc(family.getNode(),indexOverlap[0]);
				Node node = mem.loadNode(family.getNode().getChildPos(indexOverlap[0]));
				family.setNode(node);
				
				return chooseSubtree(family,e,level-1);
			}		
		}else{
			Integer[] indexArea = getMinAreaEnlarg(loop(childList.size()),childList,e.getRectangle());
			
			/*Si hay empate, se decide buscando el rectangulo con menor area*/
			if(indexArea.length>1){
				int index = getMinArea(family.getNode(),indexArea);
				family.addDesc(family.getNode(),index);
				family.setNode(mem.loadNode(family.getNode().getChildPos(index)));
				
				return chooseSubtree(family,e,level-1);
			}else{
				family.addDesc(family.getNode(),indexArea[0]);
				family.setNode(mem.loadNode(family.getNode().getChildPos(indexArea[0])));
					
				return chooseSubtree(family,e,level-1);
			}
		}	
	}
	
	/*Entrega un arreglo con los indices de los elementos que tengan el minimo overlap*/
	public Integer[] getMinOverlap(LinkedList<NodeElem> children, Rectangle r){		
		float overlap = Float.MAX_VALUE;
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		int loneIndex = 0;
		int k = 0;	
		int j = 0;
		
		for(int i = 0;  i < children.size(); i++){
			Rectangle child = children.get(i).getRectangle();
			float newOverlap = child.overlapEnlargement(r,children);
			
			if(newOverlap<overlap){
				loneIndex = i;
				overlap = newOverlap;
				j++;
			}else{
				if(newOverlap==overlap){
					index.add(i);
				}
			}
			k++;
		}
		
		Integer[] iArray;
		if(index.size()>0){
			iArray = new Integer[index.size()];
			iArray = index.toArray(iArray);
		}else{
			iArray = new Integer[1];
			iArray[0] = loneIndex;
		}		
			
		return iArray;	
	}
	
	public Integer[] getMinAreaEnlarg(Integer[] indexList, LinkedList<NodeElem> children, Rectangle r){
		float area = Float.MAX_VALUE;
		ArrayList<Integer> index = new ArrayList<Integer>();
		int loneIndex = 0;
		int k=0;
		int j= 0;
		
		for(int i = 0;  i < indexList.length; i++){
			Rectangle child = children.get(indexList[i]).getRectangle();
						
			float newArea = child.areaEnlargement(r); 
			if(newArea<area){
				loneIndex = i;
				area = newArea;		
				j++;
			}else{
				if(newArea==area){
					index.add(i);
				}
			}
			k++;
		}
			
		Integer[] iArray;
		if(index.size()>0){
			iArray = new Integer[index.size()];
			iArray = index.toArray(iArray);
		}else{
			iArray = new Integer[1];
			iArray[0] = loneIndex;
		}
		
		return iArray;	
	}
	
	public int getMinArea(Node n, Integer[] indexList){
		float area = Float.MAX_VALUE;
		LinkedList<NodeElem> children = n.getNodeList();
		
		int index = 0;
		
		for(int i = 0;  i < indexList.length; i++){
			Rectangle child = children.get(indexList[i]).getRectangle();

			if(child.getArea()<area){
				index = i;
				area = child.getArea();		
			}
		}

		return index;			
	}
	
	public void adjustTreeSplit(NodeFamily fam, Node n2) throws IOException{
		Node n = fam.getNode();
		
		while(!n.isRoot()){
			Node p = fam.getParent();
			Integer i = fam.getNextIndex();
			
			Rectangle r = getBoundingRectangle(n.getNodeList());
			p.getNodeList().get(i).setRectangle(r);
		
			Rectangle r2 = getBoundingRectangle(n2.getNodeList());
			NodeElem newE = new NodeElem(n2.getPos(),r2);
		
			if(p.getEntryCount()<M){
				p.addChild(newE);
				fam.setNode(p);
				
				adjustTree(fam);
				return;
				
			}else{
				p.addChild(newE);
				Node[] newNodes = split(p);
				n = newNodes[0];
				n2 = newNodes[1];
			
				mem.saveNode(n);
				mem.saveNode(n2);
			}
		}
		/* Si salí del loop sin haber escapado a adjustTree, es porque la raiz fue dividida*/
		Node newRoot = new InnerNode(getNewPos(),M);
				
		/*Asigno como hijos de la raiz los dos nodos obtenidos del split*/
		Rectangle r = getBoundingRectangle(n.getNodeList());
		NodeElem child1 = new NodeElem(n.getPos(),r);
		newRoot.addChild(child1);
		
		Rectangle r2 = getBoundingRectangle(n2.getNodeList());
		NodeElem child2 = new NodeElem(n2.getPos(),r2);
		newRoot.addChild(child2);
				
		/*Los hijos ya no son la raiz*/
		n.setAsNotRoot();
		n2.setAsNotRoot();
		newRoot.setAsRoot();
				
		mem.saveNode(n);
		mem.saveNode(n2);
		
		root = newRoot;
		height++;
	}
	
	public void adjustTree(NodeFamily fam) throws IOException{
		Node n = fam.getNode();
		
		while(!n.isRoot()){
			Node p = fam.getParent();
			Integer i = fam.getNextIndex();
			
			Rectangle r = getBoundingRectangle(n.getNodeList());
			p.getNodeList().get(i).setRectangle(r);

			mem.saveNode(n);
			n = p;
		}		
	}

	/*Realiza split de un nodo, devolviendo los dos nodos en los que se divide*/
	public Node[] split(Node n) throws IOException{
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
			returnArray[1] = new LeafNode(lN2,M);
			returnArray[1].setPos(getNewPos());
			
			mem.saveNode(returnArray[0]);
			mem.saveNode(returnArray[1]);
			
			return returnArray;

		} else {
			Node[] returnArray = new InnerNode[2];
			returnArray[0] = n;
			n.setNodeList(lN1);
			returnArray[1] = new InnerNode(lN2,M);
			returnArray[1].setPos(getNewPos());
			
			mem.saveNode(returnArray[0]);
			mem.saveNode(returnArray[1]);
			
			return returnArray;
		}
	}
	
	public int chooseSplitAxis(Node n){
		float[] sum = new float[2];
		
		for(int d = 0; d<2; d++){
			LinkedList<NodeElem> list = n.getNodeList();		
			sortByDimMin(list,d);
				
			for(int k=1; k<=(M-2*m+2); k++){
				List<NodeElem> l1 = list.subList(0,m + k - 1);
				List<NodeElem> l2 = list.subList(m + k - 1,list.size());				
			
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
						
				sum[d] = sum[d] + MBR1.getMargin() + MBR2.getMargin();	
			}
			
			sortByDimMax(list,d);
				
			for(int k=1; k<=(M-2*m+2); k++){
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
		index.add(0);
		
		for(int k = 1;k<=(M-2*m+2); k++){
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
				List<NodeElem> l1 = list.subList(0, m + index.get(i) - 1);
				List<NodeElem> l2 = list.subList(m + index.get(i) - 1, list.size());
				
				Rectangle MBR1 = getBoundingRectangle(l1);
				Rectangle MBR2 = getBoundingRectangle(l2);
				
				float sum = MBR1.getArea() + MBR2.getArea();
				
				if(sum<areaTot){
					areaTot = sum;
					index2 = index.get(i);
				}			
			}
			
			return index2;			
		}
		
		return index.get(0);
	}
		
	/*Ordena la lista segun los valores de la dimension dim de menor a mayor*/ 
	public void sortByDimMin(LinkedList<NodeElem> list, final int dim){
		Collections.sort(list, new Comparator<NodeElem>() {
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
	public void sortByDimMax(LinkedList<NodeElem> list, final int dim){
		Collections.sort(list, new Comparator<NodeElem>() {
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
	public void insertReinsertRectangle(Rectangle r) throws IOException{
		NodeElem newR = new NodeElem(-1,r);
		insertData(newR,height,false);
	}
	
	/*Insertar un nodo a una cierta altura de forma obligatoria*/
	public void insertData(NodeElem e, int level, boolean reinsert) throws IOException{	
		NodeFamily fam = new NodeFamily();
		fam.setNode(root);
		
		fam = chooseSubtree(fam,e,level);	 
		Node n = fam.getNode(); 
		n.getNodeList().add(e);	
		
		if(n.getEntryCount()>M){
			OverflowRes ovr = overflowTreatment(n,fam,level);
		
			if(ovr.split()){
				propagateSplit(fam, ovr);
			}
			
		}else{
			adjustTree(fam);
		}
		
		/*Termine la insercion, por lo tanto debo reiniciar la lista de niveles visitados*/
		if(!reinsert){
			levelsVisited = new HashSet<Integer>();
		}
	}
		
	public OverflowRes overflowTreatment(Node n, NodeFamily fam, int level) throws IOException{
		OverflowRes res = new OverflowRes(n);
		
		boolean inserted = levelsVisited.add(level); 
		
		if (inserted && !n.isRoot()) {
			reinsert(n, fam, level);
		}else {
			/*Si ya pase por este nivel en la insercion de este rectangulo, entonces hago split del nodo */
			Node[] newNodes = split(n);
			res.setNewNode(newNodes[1]);
		}
		
		return res;
	}
	
	public void propagateSplit(NodeFamily fam, OverflowRes over) throws IOException{
		Node ll = over.getNewNode();
			
		if(!fam.getNode().isRoot()){
			Node p = fam.getParent();
			int i = fam.getNextIndex();
			
			Rectangle r = getBoundingRectangle(fam.getNode().getNodeList());
			p.getNodeList().get(i).setRectangle(r);
			
			/*Crear una nueva entrada a insertar en el padre*/
			NodeElem eLL = new NodeElem(ll.getPos(),getBoundingRectangle(ll.getNodeList()));
			
			p.getNodeList().add(eLL);
	
			mem.saveNode(p);
			mem.saveNode(ll);
			
			/*Reviso si hay overflow y propago de ser necesario*/
			if(p.getEntryCount()>M){
				fam.setNode(p);
				OverflowRes res = overflowTreatment(p,fam,fam.getTreeHeight());
				
				if(res.split()){
					NodeFamily copy = fam.getClone();
					
					propagateSplit(copy,res);
				}
			}		
		}else{
			/*Si recibo un split de la raíz, no puedo seguir propagando hacia arriba, solo me queda
			 * hacer crecer el arbol*/
			NodeElem e1 = new NodeElem();
			NodeElem e2 = new NodeElem();
			
			e1.setChild(fam.getNode().getPos());
			e1.setRectangle(getBoundingRectangle(fam.getNode().getNodeList()));
			
			e2.setChild(ll.getPos());
			e2.setRectangle(getBoundingRectangle(ll.getNodeList()));
			
			LinkedList<NodeElem> rootChildren = new LinkedList<NodeElem>();
			rootChildren.add(e1);
			rootChildren.add(e2);
			
			Node newRoot = new InnerNode(getNewPos(),M);
					
			/*Los hijos ya no son la raiz*/
			fam.getNode().setAsNotRoot();
			ll.setAsNotRoot();
			newRoot.setAsRoot();
			
			mem.saveNode(fam.getNode());
			mem.saveNode(ll);
			
			newRoot.setNodeList(rootChildren);
			
			/*Debo asignar a la nueva raiz como el nodo a cambiar*/
			fam.addDesc(newRoot,0);
			
			root = newRoot;
			height++;		
		}	
	}
	
	/*Para reinsertar, se debe entregar como parametro el MBR del padre*/
	public void reinsert(Node n, NodeFamily fam, int level) throws IOException{
		Node parent = fam.viewParent();
		Integer index = fam.viewIndex();
		
		Rectangle mbr = parent.getNodeList().get(index).getRectangle();
		LinkedList<NodeElem> list = n.getNodeList();
		sortByDistanceCenter(list, mbr);
		
		List<NodeElem> deleteList = list.subList(0,p);
		List<NodeElem> copy = new LinkedList<NodeElem>(deleteList);
		List<NodeElem> copy2 = new LinkedList<NodeElem>(deleteList);
		
		int[] height = new int[p];
				
		for(int i=0; i<p; i++){
			height[i] = deleteEntry(copy2.get(i),fam);
		}
				
		for(int i=0; i<p; i++){
			insertData(copy.get(i),height[i],true);
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
		
	/*Eliminar una entrada*/
	public int deleteEntry(NodeElem r, NodeFamily cont) throws IOException{
		/*Elimino la entrada del nodo correspondiente*/
		cont.getNode().removeChild(r);
		
		NodeFamily newFamily = cont.getClone();
		
		/*Propago los cambios*/
		condenseTree(newFamily);
		
		return cont.getTreeHeight();
	}
	
	/*Propaga los cambios por haber eliminado un nodo de abajo hacia arriba, como solo se utiliza
	 * para reinsert, jamas tendre el caso de underflow*/
	public void condenseTree(NodeFamily init) throws IOException{
		Node n = init.getNode();
		
		while(!n.isRoot()){
			Node parent = init.getParent();
			Integer i = init.getNextIndex();
						
			Rectangle r = getBoundingRectangle(n.getNodeList());
			parent.getNodeList().get(i).setRectangle(r);
			
			mem.saveNode(n);
			n = parent;		
		}		
	}
	
	/*Busco todos los rectangulos que intersectan el entregado*/
	public Rectangle[] findRectangle(Rectangle r) throws IOException{
		return getRectangleArray(search(root,r));
	}
	
	/*Busca todos los rectangulos en el arbol que se intersectan con el entregado*/
	public ArrayList<Rectangle> search(Node t, Rectangle r) throws IOException{
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
				Rectangle rect = childList.get(i).getRectangle();
					
				if(!rect.intersects(r)){
					continue;
				} 
				
				Node n = mem.loadNode(t.getChildPos(i));
				returnList.addAll(search(n,r));

			}
		}
		
		return returnList;	
	}
			
	public Rectangle[] getRectangleArray(List<Rectangle> rList){	
		Rectangle[] rArray = new Rectangle[rList.size()];
		rArray = rList.toArray(rArray);
		
		return rArray;
	}
		
}
