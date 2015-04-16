package externalMemory;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import structures.Node;
	 
public class ExtMemory {
	private String fileName;
	
	public ExtMemory(String fName){
		fileName = fName;
	}	
	  
	public void serializeNode(Node node){
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(node);
			oos.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	 public Node deserializeNode(){
		Node node;

		try {
			FileInputStream fin = new FileInputStream("");
			ObjectInputStream ois = new ObjectInputStream(fin);
			node = (Node) ois.readObject();
			ois.close();

			return node;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
