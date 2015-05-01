package display;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

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
		Rectangle r1 = new Rectangle(0,100,0,100);
		Rectangle r2 = new Rectangle(50,150,50,150);
		Rectangle r3 = new Rectangle(50,75,50,75);
		Rectangle r4 = new Rectangle(200,300,200,300);
		Rectangle r5 = new Rectangle(100,200,100,200);
		Rectangle r6 = new Rectangle(100,120,100,120);
		Rectangle r7 = new Rectangle(120,180,160,250);
		Rectangle r8 = new Rectangle(10,190,100,230);
		Rectangle r9 = new Rectangle(150,250,150,250);
		Rectangle r10 = new Rectangle(160,180,170,250);
		Rectangle r11 = new Rectangle(10,100,100,110);
		Rectangle r12 = new Rectangle(100,250,120,260);
		Rectangle r13 = new Rectangle(0,200,0,200);
		Rectangle r14 = new Rectangle(230,250,300,280);
		Rectangle r15 = new Rectangle(0,20,10,80);
		Rectangle r16 = new Rectangle(10,120,50,180);
		Rectangle r17 = new Rectangle(10,250,100,250);
		
		//Rectangle[] rectangles =  {r1,r2,r3,r4,r5,r6,r7,r8,r9,r10,r11,r12,r13,r14,r15,r16,r17};
		
		RSTree tree = new RSTree(2,1,1,5,68);
		tree.insertReinsertRectangle(r1);
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
		tree.insertReinsertRectangle(r17);
		
		
				
		//for(int i=0;i<17;i++){
			//System.out.println(rectangles[i].contains(r2));
		//}
		
		//System.out.println(tree.findRectangle(r2).length);

		/*MemoryMng mem = tree.mem;
		Node n1 = mem.loadNode(tree.root.getNodeList().get(0).getNode());
		Node n2 = mem.loadNode(n1.getNodeList().get(1).getNode());
		Node n3 = mem.loadNode(n2.getNodeList().get(1).getNode());
		Node n4 = mem.loadNode(n3.getNodeList().get(1).getNode());
		Node n5 = mem.loadNode(n4.getNodeList().get(1).getNode());
		
		System.out.println(tree.root.getNodeList().get(1).getRectangle());
		System.out.println(n1.getPos());
		System.out.println(n2.getPos());
		System.out.println(n3.getPos());
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
