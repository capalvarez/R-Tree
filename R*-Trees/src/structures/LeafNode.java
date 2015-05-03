package structures;

import java.nio.ByteBuffer;
import java.util.LinkedList;


public class LeafNode implements Node {
	private boolean isRoot;
	private LinkedList<NodeElem> infoList = new LinkedList<NodeElem>();
	private long archivePos;	
	private int max;
	
	public LeafNode(int M){
		this.max = M;
	}
	
	public LeafNode(LinkedList<NodeElem> info, int M){
		infoList = info;
		max = M;
	}
		
	public int getEntryCount(){
		return infoList.size();
	}

	public boolean isLeaf(){
		return true;
	}	
		
	public LinkedList<NodeElem> getNodeList() {
		return infoList;
	}
	
	public void addChild(NodeElem e){
		infoList.add(e);
	}
	
	public long getChildPos(int i){
		return infoList.get(i).getNode();
	}
	
	public void setNodeList(LinkedList<NodeElem> l) {
		infoList = l;	
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setAsRoot() {
		isRoot = true;	
	}

	public void setAsNotRoot() {
		isRoot = false;
	}

	public byte[] getByteForm(){
		byte[] bytes = new byte[20+24*max];
		
		int pos = 0;
		int root = isRoot? 1:0;
		
		ByteBuffer.wrap(bytes, pos, 4).putInt(root);
		pos += 4;
		
		ByteBuffer.wrap(bytes, pos, 4).putInt(1);
		pos += 4;
		
		ByteBuffer.wrap(bytes, pos, 8).putLong(archivePos);
		pos += 8;
		
		ByteBuffer.wrap(bytes, pos, 4).putInt(getEntryCount());
		pos += 4;
		
		for (int i = 0; i < getEntryCount(); i++) {
			infoList.get(i).getRectangle().getByteForm(bytes,pos);
			pos += 16 ;
		}
		
		pos += (max - getEntryCount()) * 16;
		
		for (int i = 0; i < getEntryCount(); i++) {
			ByteBuffer.wrap(bytes, pos, 8).putLong(infoList.get(i).getNode());
			pos +=8;
		}
	
		return bytes;	
	}

	public long getPos() {
		return archivePos;
	}

	public void setPos(long pos) {
		archivePos = pos;
	}
	
	public void removeChild(NodeElem e) {
		infoList.remove(e);
	}
	
}
