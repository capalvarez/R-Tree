package externalMemory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import structures.InnerNode;
import structures.LeafNode;
import structures.Node;
import structures.NodeElem;
import structures.Rectangle;

public class MemoryMng {
	private RandomAccessFile file;
	private PriorityBuffer priority;
	private Node[] buffer;
	private boolean[] changed;
	
	private int buffSize;
	private int nodeSize;
	private int max;
	
	private int memoryAccess;
	
	public MemoryMng(int buffSize, int nodeSize, int M) throws IOException{
		file = new RandomAccessFile("tree.obj", "rw");
		file.setLength(0);
		
		priority = new PriorityBuffer(buffSize);
		buffer = new Node[buffSize];

		changed = new boolean[buffSize];
		
		this.buffSize = buffSize;
		this.nodeSize = nodeSize;
		this.max = M;
		
	}
	
	public int getMemoryAccess(){
		return memoryAccess;
	}
	
	public void resetMemoryAccess(){
		memoryAccess = 0;
	}
	/*Recibe un nodo que guardar en memoria secundaria*/
	public void saveNode(Node n) throws IOException{
		long posNode = n.getPos();
		
		for(int i = 0; i < buffSize; i++){
			if(buffer[i]!=null && buffer[i].getPos() == posNode){
				priority.improvePriority(i);
				buffer[i] = n;
				changed[i] = true;
				
				return;
			}		
		}
		
		int freeBufPos = priority.getLastPriority();
		
		if(changed[freeBufPos]){
			byte[] byteForm = buffer[freeBufPos].getByteForm(); 
			writeDisk(byteForm, buffer[freeBufPos].getPos());
		}
		
		buffer[freeBufPos] = n;
		changed[freeBufPos] = true;
		
	}
	
	/*Recibe la posicion en disco de un nodo y devuelve el nodo correspondiente*/ 
	public Node loadNode(long pos) throws IOException{
		//System.out.println(pos);
		/*Buscamos entre los nodos guardados en el buffer si esta el correspondiente a la
		 * direccion entregada*/
		for(int i = 0; i < buffSize; i++){
			if(buffer[i]!=null && buffer[i].getPos() == pos){
				priority.improvePriority(i);

				return buffer[i];
			}		
		}
		
		/*Si el nodo no esta en el buffer, debemos sacarlo de disco, reemplazando al nodo con menor prioridad, primero
		 * obtenemos la posicion en el buffer a utilizar*/
		int freeBufPos = priority.getLastPriority();
		
		if(changed[freeBufPos]){
			writeDisk(buffer[freeBufPos].getByteForm(), buffer[freeBufPos].getPos());
		}
		
		byte[] nodeBytes = new byte[nodeSize];
		readDisk(nodeBytes, pos);
		
		Node n = createNode(nodeBytes,pos);
		changed[freeBufPos] = false;
		
		return n;
	}	

	private void writeDisk(byte[] info , long pos) throws IOException{
		file.seek(pos);
		file.write(info);
		memoryAccess++;
	}
	
	private void readDisk(byte[] info , long pos) throws IOException{
		file.seek(pos);
		file.read(info);
		memoryAccess++;
	}

	private Node createNode(byte[] nodeInfo, long nodePos){
		int pos = 0;
		
		int isRoot  = ByteBuffer.wrap(nodeInfo, pos, 4).getInt();
		pos += 4;
		
		int isLeaf = ByteBuffer.wrap(nodeInfo, pos, 4).getInt();
		pos += 4;
		
		long archivePos = ByteBuffer.wrap(nodeInfo, pos, 8).getLong();
		pos += 8;
		
		int entryCount = ByteBuffer.wrap(nodeInfo, pos, 4).getInt();
		pos += 4;
		
		//System.out.println("estoy creando un nodo con " + entryCount + " hijos  que es hoja? " + isLeaf);
		Rectangle[] rectangles = new Rectangle[entryCount];
				
		for (int i = 0; i < entryCount; i++) {
			float left = ByteBuffer.wrap(nodeInfo, pos, 4).getFloat();	
			pos += 4;
			
			float right = ByteBuffer.wrap(nodeInfo, pos, 4).getFloat();	
			pos += 4;
			
			float bottom = ByteBuffer.wrap(nodeInfo, pos, 4).getFloat();	
			pos += 4;
			
			float top = ByteBuffer.wrap(nodeInfo, pos, 4).getFloat();	
			pos += 4;
			
			rectangles[i] = new Rectangle(left,right,bottom,top);
		}
		
		pos += (max - entryCount) * 16;
		
		LinkedList<NodeElem> children = new LinkedList<NodeElem>();
	
		NodeElem[] elems = new NodeElem[entryCount];
		for (int i = 0; i < entryCount; i++) {
			elems[i] = new NodeElem(ByteBuffer.wrap(nodeInfo, pos, 8).getLong(),rectangles[i]);
			children.add(elems[i]);
		
			pos +=8;
		}

		Node node;	
		if(isLeaf!=0){
			node = new LeafNode(children,max);
		}else{
			node = new InnerNode(children,max);
		}
		
		if(isRoot!=0){
			node.setAsRoot();
		}else{
			node.setAsNotRoot();
		}
		
		node.setPos(nodePos);
		
		return node;		
	}
	
}

