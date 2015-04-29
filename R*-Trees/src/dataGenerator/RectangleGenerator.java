package dataGenerator;

import java.util.Random;

import structures.Rectangle;

public class RectangleGenerator {
	private float minValue;
	private float maxValue;
	private int minArea;
	private int maxArea;
	private Random rand = new Random();
	
	public RectangleGenerator(float min, float max, int minArea, int maxArea){
		minValue = min;
		maxValue = max- maxArea;
		this.minArea = minArea;
		this.maxArea = maxArea;
	}
	
	public Rectangle getRectangle(){
		float initPX = rand.nextFloat()*(maxValue - minValue) + minValue;
		float initPY = rand.nextFloat()*(maxValue - minValue) + minValue;
	
		double area = rand.nextDouble()*(maxArea - minArea) + minArea;
		
		float endPX = rand.nextFloat()*maxArea + initPX;
		float endPY = (float) (area/(endPX-initPX) + initPY);
		
		return new Rectangle(initPX,endPX,initPY,endPY);
	
	}
	
	public Rectangle[] getRectangleArray(int n){
		Rectangle[] rectangles = new Rectangle[n];
		
		for(int i=0;i<n;i++){
			rectangles[i] = getRectangle();
		}
		
		return rectangles;
	}
}
