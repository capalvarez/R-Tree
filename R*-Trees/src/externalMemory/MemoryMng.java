package externalMemory;

import java.io.FileNotFoundException;
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
	private long[] dirNodes;
	private boolean[] changed;
	
	private int buffSize;
	private int nodeSize;
	private int max;
	
	public MemoryMng(int buffSize, int nodeSize, int M) throws FileNotFoundException{
		file = new RandomAccessFile("tree.obj", "rw");
		
		priority = new PriorityBuffer(buffSize);
		buffer = new Node[buffSize];
		
		dirNodes = new long[buffSize];
		changed = new boolean[buffSize];
		
		this.buffSize = buffSize;
		this.nodeSize = nodeSize;
		this.max = M;
		
	}
	
	/*Recibe un nodo que guardar en memoria secundaria*/
	public void saveNode(Node n) throws IOException{
		long posNode = n.getPos();
		
		for(int i = 0; i < buffSize; i++){
			if(dirNodes[i] == posNode){
				priority.improvePriority(i);
				
				buffer[i] = n;
				changed[i] = true;
				
				return;
			}		
		}
		
		int freeBufPos = priority.getLastPriority();
		
		if(changed[freeBufPos]){
			byte[] byteForm = buffer[freeBufPos].getByteForm(); 
			writeDisk(byteForm, dirNodes[freeBufPos]);
		}
		
		buffer[freeBufPos] = n;
		
		dirNodes[freeBufPos] = posNode;
		changed[freeBufPos] = true;
		
	}
	
	/*Recibe la posicion en disco de un nodo y devuelve el nodo correspondiente*/ 
	public Node loadNode(long pos) throws IOException{
		/*Buscamos entre los nodos guardados en el buffer si esta el correspondiente a la
		 * direccion entregada*/
		for(int i = 0; i < buffSize; i++){
			if(dirNodes[i] == pos){
				priority.improvePriority(i);
				return buffer[i];
			}		
		}
		
		/*Si el nodo no esta en el buffer, debemos sacarlo de disco, reemplazando al nodo con menor prioridad, primero
		 * obtenemos la posicion en el buffer a utilizar*/
		int freeBufPos = priority.getLastPriority();
		
		if(changed[freeBufPos]){
			writeDisk(buffer[freeBufPos].getByteForm(), dirNodes[freeBufPos]);
		}
		
		byte[] nodeBytes = new byte[nodeSize];
		readDisk(nodeBytes, pos);
		Node n = createNode(nodeBytes,pos);
		
		dirNodes[freeBufPos] = pos;
		changed[freeBufPos] = false;
		
		return n;
	}	

	private void writeDisk(byte[] info , long pos) throws IOException{
		file.seek(pos);
		file.write(info);
	}
	
	private void readDisk(byte[] info , long pos) throws IOException{
		file.seek(pos);
		file.read(info);
	}

	private Node createNode(byte[] nodeInfo, long nodePos){
		int pos = 0;
		
		boolean isRoot  = ByteBuffer.wrap(nodeInfo, pos, 4).getInt()==1? true:false;
		pos += 4;
		
		boolean isLeaf = ByteBuffer.wrap(nodeInfo, pos, 4).getInt()==1? true:false;
		pos += 4;
		
		long archivePos = ByteBuffer.wrap(nodeInfo, pos, 8).getLong();
		pos += 8;
		
		int entryCount = ByteBuffer.wrap(nodeInfo, pos, 4).getInt();
		pos += 4;
		
		Rectangle[] rectangles = new Rectangle[entryCount];
				
		for (int i = 0; i < entryCount; i++) {
			float left = ByteBuffer.wrap(nodeInfo, pos, 8).getFloat();	
			pos += 8;
			
			float right = ByteBuffer.wrap(nodeInfo, pos, 8).getFloat();	
			pos += 8;
			
			float bottom = ByteBuffer.wrap(nodeInfo, pos, 8).getFloat();	
			pos += 8;
			
			float top = ByteBuffer.wrap(nodeInfo, pos, 8).getFloat();	
			pos += 8;
			
			rectangles[i] = new Rectangle(left,right,bottom,top);
		}
		
		pos += (max - entryCount) * 16;
		
		LinkedList<NodeElem> children = new LinkedList<NodeElem>();
	
		for (int i = 0; i < entryCount; i++) {
			NodeElem e = new NodeElem(ByteBuffer.wrap(nodeInfo, pos, 8).getLong(),rectangles[i]);
			children.add(e);
		
			pos +=8;
		}

		Node node;	
		if(isLeaf){
			node = new LeafNode(children,max);
		}else{
			node = new InnerNode(children,max);
		}
		
		if(isRoot){
			node.setAsRoot();
		}else{
			node.setAsNotRoot();
		}
		
		node.setPos(nodePos);
		
		return node;		
	}
	
}

