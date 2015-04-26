package display;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JFrame;

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
	
	public static void main(String[] args){
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
		Rectangle r = new Rectangle(10,250,100,250);
		
		NodeElem e = new NodeElem();
		NodeElem e1 = new NodeElem();
		NodeElem e2 = new NodeElem();
		NodeElem e3 = new NodeElem();
		NodeElem e4 = new NodeElem();
		NodeElem e5 = new NodeElem();
		NodeElem e6 = new NodeElem();
		NodeElem e7 = new NodeElem();
		NodeElem e8 = new NodeElem();
		NodeElem e9 = new NodeElem();
		NodeElem e10 = new NodeElem();
				
		e.setRectangle(r);
		e1.setRectangle(r1);
		e2.setRectangle(r2);
		e3.setRectangle(r3);
		e4.setRectangle(r4);
		e5.setRectangle(r5);
		e6.setRectangle(r6);
		e7.setRectangle(r7);
		e8.setRectangle(r8);
		e9.setRectangle(r9);
		e10.setRectangle(r10);
		
		
		RSTree tree = new RSTree(2,1,1);
		tree.insertSplitRectangle(r8);
		tree.insertSplitRectangle(r9);
		tree.insertSplitRectangle(r10);
		tree.insertSplitRectangle(r1);
		tree.insertSplitRectangle(r6);
		tree.insertSplitRectangle(r7);
		tree.insertReinsertRectangle(r2);
		tree.insertReinsertRectangle(r3);
		tree.insertReinsertRectangle(r4);
		tree.insertReinsertRectangle(r5);
			
		Node n = tree.root.getNodeList().get(1).getNode();
		Node n1 = n.getNodeList().get(0).getNode();
		Node n2 = n1.getNodeList().get(0).getNode();
		Node n3 = n2.getNodeList().get(0).getNode();
		
		
		//tree.deleteEntry(n1.getNodeList().get(0), cont);

		//Node n = tree.root.getNodeList().get(1).getNode();
		//Node n1 = n.getNodeList().get(0).getNode();
		//Node n2 = n1.getNodeList().get(0).getNode();
		//Node n3 = n2.getNodeList().get(1).getNode();
		
		
		
		System.out.println(n1.getNodeList().get(1).getRectangle());
		//System.out.println(tree.getMinOverlap(children, r)[0]);
		
	
	  	RectWindow dw = new RectWindow();
		//dw.showWindow();
		//dw.drawRectangle(r1.intersection(r3));
		//dw.drawRectangle(r);
		//dw.drawRectangle(r2);
		//dw.drawRectangle(r4);
		//dw.drawRectangle(r2.intersection(r));
		
	}
}
