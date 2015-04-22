package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import javax.swing.JPanel;
import structures.Rectangle;

public class MyPanel extends JPanel {
	protected Color color = Color.blue;
	private LinkedList<Rectangle> rectangles = new LinkedList<Rectangle>();
	
	private void doDrawing(Graphics g) {	
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
          
        for(int i=0; i<rectangles.size(); i++){
        	rectangles.get(i).draw(g2d);   
        }
           
    }
	
	public void addRectangle(Rectangle r){
		rectangles.add(r);
	}
	
	@Override
    public void paintComponent(Graphics g) {   
        super.paintComponent(g);
        doDrawing(g);
	}    

}
