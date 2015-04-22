package display;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JFrame;

import structures.*;

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
		Rectangle r = new Rectangle(0,100,0,100);
		Rectangle r2 = new Rectangle(50,150,50,150);
		Rectangle r3 = new Rectangle(50,75,50,75);
		Rectangle r4 = new Rectangle(200,300,200,300);
		Rectangle r5 = new Rectangle(100,200,100,200);
		Rectangle r6 = new Rectangle(100,120,100,120);
		
		LinkedList<NodeElem> children = new LinkedList<NodeElem>();
		NodeElem e1 = new NodeElem();
		NodeElem e2 = new NodeElem();
		NodeElem e3 = new NodeElem();
		NodeElem e4 = new NodeElem();
		
		e1.setRectangle(r2);
		e2.setRectangle(r3);
		e3.setRectangle(r4);
		e4.setRectangle(r5);
		
		children.add(e1);
		children.add(e2);
		children.add(e3);
		children.add(e4);  
		
		System.out.println(r.overlapEnlargement(r6,children));
		System.out.println();
		System.out.println();
		System.out.println();

	  	RectWindow dw = new RectWindow();
		dw.showWindow();
		//dw.drawRectangle(r.union(r2));
		//dw.drawRectangle(r);
		dw.drawRectangle(r2);
		//dw.drawRectangle(r4);
		//dw.drawRectangle(r2.intersection(r));
		
	}
}
