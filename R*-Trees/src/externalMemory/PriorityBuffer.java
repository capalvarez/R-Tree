package externalMemory;

import java.util.LinkedList;

public class PriorityBuffer {

	private LinkedList<Integer> list;
	
	public PriorityBuffer(int numEl) {
		list = new LinkedList<Integer>();
		
		for(int i=numEl-1;i >= 0;i--){
			list.add(i);
		}
	}
	
	public int getLastPriority(){
		int res =  list.getLast();
		
		list.remove((Integer)res);
		list.addFirst(res);
		
		return res;
	}
	
	public void improvePriority(int elem){
		list.remove((Integer)elem);
		list.addFirst(elem);
	}
}