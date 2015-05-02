package display;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

import dataGenerator.RectangleGenerator;

import externalMemory.MemoryMng;

import structures.*;
import tree.RSTree;


public class RectWindow extends JFrame{
	MyPanel Panel;
	
	public void showWindow(){
		setSize(600, 400);
	    setLocationRelativeTo(null);
	    Panel = new MyPanel();
	    getContentPane().add(Panel);

	    setVisible(true);
	}
	
	public void drawRectangle(Rectangle r){
		Panel.addRectangle(r);
		repaint();
		validate();		
	}
	
	public static void main(String[] args) throws IOException{
		RectangleGenerator rG = new RectangleGenerator(0,300,0,100); 
		Rectangle[] rectangles =  rG.getRectangleArray(20);
		
		RSTree tree = new RSTree(10,4,3,5,68,"tree.obj");
		for(int i=0;i<rectangles.length;i++){
			tree.insertReinsertRectangle(rectangles[i]);
		}
		
		/*tree.insertReinsertRectangle(r1);
		tree.insertReinsertRectangle(r2);
		tree.insertReinsertRectangle(r3);
		tree.insertReinsertRectangle(r4);
		tree.insertReinsertRectangle(r5);
		tree.insertReinsertRectangle(r6);
		tree.insertReinsertRectangle(r7);
		tree.insertReinsertRectangle(r8);
		tree.insertReinsertRectangle(r9);
		tree.insertReinsertRectangle(r10);
		tree.insertReinsertRectangle(r11);
		tree.insertReinsertRectangle(r12);
		tree.insertReinsertRectangle(r13);
		tree.insertReinsertRectangle(r14);
		tree.insertReinsertRectangle(r15);
		tree.insertReinsertRectangle(r16);
		tree.insertReinsertRectangle(r17);*/
		
		
				
		//for(int i=0;i<17;i++){
			//System.out.println(rectangles[i].contains(r2));
		//}
		
		//System.out.println(tree.findRectangle(r2).length);

		/*MemoryMng mem = tree.mem;
		Node n1 = mem.loadNode(tree.root.getNodeList().get(0).getNode());
		Node n2 = mem.loadNode(n1.getNodeList().get(0).getNode());*/
		/*Node n3 = mem.loadNode(n2.getNodeList().get(1).getNode());
		Node n4 = mem.loadNode(n3.getNodeList().get(1).getNode());
		Node n5 = mem.loadNode(n4.getNodeList().get(1).getNode());*/
		
		//System.out.println(n1.getNodeList().get(0).getRectangle());
		//System.out.println(n1.getNodeList().get(1).getRectangle());
		//System.out.println(n1.getNodeList().get(0).getRectangle());
		//System.out.println(n1.getNodeList().get(1).getRectangle());
		/*System.out.println(n3.getPos());
		System.out.println(n4.getPos());
		System.out.println(n5.getPos());*/
		
		
		
		//System.out.println(n.getNodeList().get(0).getRectangle());
		//System.out.println(tree.root.isLeaf());
		
	
	  	//RectWindow dw = new RectWindow();
		//dw.showWindow();
		//dw.drawRectangle(r1.intersection(r3));
		//dw.drawRectangle(r);
		//dw.drawRectangle(r2);
		//dw.drawRectangle(r4);
		//dw.drawRectangle(r2.intersection(r));
		
	}
}
