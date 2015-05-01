package test;

import java.io.IOException;

import structures.Rectangle;
import tree.RSTree;
import dataGenerator.RectangleGenerator;

public class Main {

	public static void main(String[] args) throws IOException{
		float min = 0;
		float max = 500000;
		int minArea = 0;
		int maxArea = 100;
				
		int M = 200;
		int m = (int) Math.floor(M*0.4);
		int p = (int) Math.floor(M*0.3);
		int buffSize = 3;
		int nodeSize = 4820;
		
		RectangleGenerator rG = new RectangleGenerator(min,max,minArea,maxArea);
		
		for(int i=9;i<15;i=i+3){
			int rNumber = (int) Math.pow(2, i);
			
			/*Generar los 2^i rectangulos a insertar*/
			System.out.println("Iniciando prueba con 2^" + i + " rectangulos");
			long startTimeRectangles = System.currentTimeMillis();
			
			Rectangle[] rectangles = rG.getRectangleArray(rNumber);
			
			long endTimeRectangles = System.currentTimeMillis();
			System.out.println("Rectangulos generados en " + (endTimeRectangles - startTimeRectangles) + " milis");
		
			System.out.println("");
			
			/*Construir el arbol utilizando insercion con split*/
			System.out.println("Iniciando construccion con split");
			RSTree splitTree = new RSTree(M,m,p,buffSize,nodeSize);
			long startTimeSplit = System.currentTimeMillis();
			
			for(int j=0;j<rectangles.length;j++){
				splitTree.insertSplitRectangle(rectangles[j]);
			}
			
			long endTimeSplit = System.currentTimeMillis();
			System.out.println("Tiempo utilizado: " + (endTimeSplit - startTimeSplit) + " milis");
			System.out.println("Cantidad de accesos a disco: " + splitTree.getMemoryAccess());
			
			splitTree.resetMemoryAccess();
			
			System.out.println("");
			
			/*Construir el arbol utilizando insercion con reinsert*/
			/*System.out.println("Iniciando construccion con reinsert");
			RSTree reinsertTree = new RSTree(M,m,p,buffSize,nodeSize);
			long startTimeReinsert = System.currentTimeMillis();
			
			for(int j=0;j<rectangles.length;j++){
				reinsertTree.insertReinsertRectangle(rectangles[j]);
			}
			
			long endTimeReinsert = System.currentTimeMillis();
			System.out.println("Tiempo utilizado: " + (endTimeReinsert - startTimeReinsert) + " milis");
			System.out.println("Cantidad de accesos a disco: " + reinsertTree.getMemoryAccess());
			
			reinsertTree.resetMemoryAccess();*/
			
			System.out.println("");
			
			/*Busqueda con 2^i/10 rectangulos*/
			System.out.println("Generando " + (rNumber/10) +" rectangulos para consulta");
			long startTimeSearchRectangles = System.currentTimeMillis();
			
			Rectangle[] search = rG.getRectangleArray((int)rNumber);
			
			long endTimeSearchRectangles = System.currentTimeMillis();
			System.out.println("Rectangulos generados en " + (endTimeSearchRectangles - startTimeSearchRectangles) + " milis");
			
			System.out.println("");
			
			/*Realizar la busqueda en el arbol de split*/
			System.out.println("Iniciando busqueda en el arbol de split");
			long startTimeSearchSplit = System.currentTimeMillis();
			
			for(int k=0;k<search.length;k++){
				Rectangle[] result = splitTree.findRectangle(search[k]);
			}
			
			long endTimeSearchSplit = System.currentTimeMillis();
			System.out.println("Busqueda realizada en:  " + (endTimeSearchSplit - startTimeSearchSplit) + " milis");
			System.out.println("Cantidad de accesos a disco: " + splitTree.getMemoryAccess());
			
			System.out.println("");
			
			/*Realizar la busqueda en el arbol de reinsert*/
			/*System.out.println("Iniciando busqueda en el arbol de reinsert");
			long startTimeSearchReinsert = System.currentTimeMillis();
			
			for(int l=0;l<search.length;l++){
				Rectangle[] result2 = reinsertTree.findRectangle(search[l]);
			}
			
			long endTimeSearchReinsert = System.currentTimeMillis();
			System.out.println("Busqueda realizada en:  " + (endTimeSearchReinsert - startTimeSearchReinsert) + " milis");
			System.out.println("Cantidad de accesos a disco: " + reinsertTree.getMemoryAccess());*/
	
		}
		
	}
	
}
