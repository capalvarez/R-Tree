package structures;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class InnerNode implements Node {
	private boolean isRoot;
	private LinkedList<NodeElem> childList = new LinkedList<NodeElem>();
	private long archivePos;	
	private int max;
	
	public InnerNode(long aPos, int M){
		this.archivePos = aPos;
		this.max = M;
	}
	
	public InnerNode(LinkedList<NodeElem> list, int M){
		childList = list;
		this.max = M;
	}
	
	public int getEntryCount(){
		return childList.size();
	}

	public boolean isLeaf(){
		return false;
	}

	public LinkedList<NodeElem> getNodeList() {
		return childList;
	}
	
	public void addChild(NodeElem e){
		
	}
	
	public long getChildPos(int i){
		return childList.get(i).getNode();
	}
	
	public void setNodeList(LinkedList<NodeElem>  l) {
		childList = l;	
	}

	public boolean isRoot() {
		return isRoot;
	}
	
	public void setAsRoot(){
		isRoot = true;
	}
	
	public void setAsNotRoot(){
		isRoot = false;
	}

	public byte[] getByteForm(){
		/*cambiar el largo a futuro, hay que calcularlo*/
		byte[] bytes = new byte[90];
		
		int pos = 0;
		int root = isRoot? 1:0;
		
		ByteBuffer.wrap(bytes, pos, 4).putInt(root);
		pos += 4;
		
		ByteBuffer.wrap(bytes, pos, 4).putInt(0);
		pos += 4;
		
		ByteBuffer.wrap(bytes, pos, 4).putLong(archivePos);
		pos += 8;
		
		ByteBuffer.wrap(bytes, pos, 8).putInt(getEntryCount());
		pos += 4;
		
		for (int i = 0; i <getEntryCount(); i++) {
			childList.get(i).getRectangle().getByteForm(bytes,pos);
			pos += 16 ;
		}
		
		pos += (max - getEntryCount()) * 16;
		
		for (int i = 0; i < getEntryCount(); i++) {
			ByteBuffer.wrap(bytes, pos, 8).putLong(childList.get(i).getNode());
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

	@Override
	public void removeChild(NodeElem e) {
		// TODO Auto-generated method stub
		
	}

}
