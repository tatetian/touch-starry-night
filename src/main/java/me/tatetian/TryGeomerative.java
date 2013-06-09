package me.tatetian;

import processing.core.PApplet;
import geomerative.*;

public class TryGeomerative extends PApplet {
	private final int WIDTH 	= 800;
	private final int HEIGHT  = 600;
	private final int NUM_STARS = 200;
	
	private RMovingPoint[] stars = null;
	private float[] starSizeNoises = null;
	
	private RShape text = null;
	private RPoint[] textPoints = null;

	
	public static interface MovingFunction {
		public void start();
		public void nextFrame();
		public void move(RMovingPoint p);
	}
	
	public static class NaiveMovingFunction implements MovingFunction {
		private PApplet parent;
		private int duration;
		private int startTime;
		private float progress;
		
		public NaiveMovingFunction(PApplet parent, int duration) {
			this.parent = parent;
			this.duration = duration;
		}
		
		public void move(RMovingPoint p) {
			p.x = p.fromPoint.x * (1-progress) + p.toPoint.x * progress;
			p.y = p.fromPoint.y * (1-progress) + p.toPoint.y * progress;
		}

		public void start() {
			startTime = parent.millis();
			progress = 0;
		}

		public void nextFrame() {
			int now = parent.millis();
			if(now - startTime > duration) 
				progress = 1.0f;
			else
				progress = (float) ( now - startTime ) / duration;
		}
	}
	
	public static class RMovingPoint extends RPoint {
		public static MovingFunction movingFunc = null;
		
		public RMovingPoint(float x, float y) {
			super(x, y);
			fromPoint = new RPoint(x, y);
		}
		
		public RPoint fromPoint = null;
		public RPoint toPoint = null;
		
		public void animate() {
			RMovingPoint.movingFunc.move(this);
		}
	}
	
	public void setup(){
	  // Initilaize the sketch
	  size(WIDTH, HEIGHT, P2D);
	  smooth();
	  frameRate(24);

	  // Choice of colors
	  background(255);
	  fill(255,102,0);
	  stroke(0);
	  
	  RG.init(this); //Initialise the library.
	  
	  // init text
	  text = RG.getText("Hello world!", "../../data/FreeSans.ttf", 72, CENTER); 
	  RG.setPolygonizer(RG.UNIFORMLENGTH);
	  RG.setPolygonizerLength(20);
	  text.translate(WIDTH/2, HEIGHT/2);
	  textPoints = text.getPoints();
	 
	  // init stars
	  stars = new RMovingPoint[NUM_STARS]; 
	  starSizeNoises = new float[NUM_STARS];
	  for(int i = 0; i < stars.length; i++) {
	  	float x = random(WIDTH), y = random(HEIGHT);
	  	stars[i] = new RMovingPoint(x, y);
	  	starSizeNoises[i] = random(7);
	  	stars[i].toPoint = textPoints[(int) random(textPoints.length)];
	  }
	  
	  // init animation
	  RMovingPoint.movingFunc = new NaiveMovingFunction(this, 10 * 1000);	// 10s
	  RMovingPoint.movingFunc.start();
	}

	public void draw(){
		// Clean frame
	  background(255);
	  
	  // Show text
	  //pushMatrix();
	  
		  // Set the origin to draw in the middle of the sketch
		  //translate(width/2, height/2);
		  
		  // Draw the group of shapes
		  noFill();
		  stroke(0,0,200,150);
		  RG.setPolygonizer(RG.ADAPTATIVE);
		  text.draw();
		  
		  drawPoints(textPoints, 5, null);
		//popMatrix();
	
		// Show stars
		stroke(200,0,0,150);
		RMovingPoint.movingFunc.nextFrame();
		for(RMovingPoint mp : stars) mp.animate();
 		drawPoints(stars, 5, starSizeNoises);
	}
	
	private void drawPoints(RPoint[] points, float radius, float[] radiusNoises) {
		RPoint p;
		float r;
		for(int i = 0; i < points.length; i++) {
			p = points[i];
			r = radius;
			if(radiusNoises != null)
				r += radiusNoises[i];
			ellipse(p.x, p.y, r, r);
		}
	}
}
