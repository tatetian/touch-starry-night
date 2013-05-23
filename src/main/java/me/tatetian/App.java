package me.tatetian;

import processing.core.*;

public class App extends PApplet
{
	// Base directory
	private static final String BASE_DIR = "../../";

	// Parameters related animation
	private static final float MAX_ZOOM_GRASS = 1.5f, 
														 MAX_ZOOM_TREES = 1.3f, 
														 MAX_ZOOM_HILLS = 1.2f, 
														 MAX_ZOOM_STARS = 1.1f;
	private static final int ANIMATION_PERIOD_MS = 20 * 1000; 
	
	private static final int SCREEN_WIDTH  = 1440;
	private static final int SCREEN_HEIGHT = 810; 
	private static final int CENTER_X = SCREEN_WIDTH 	/ 2;
	private static final int CENTER_Y = SCREEN_HEIGHT / 2;
	
	
	private PImage imgTrees, imgGrass, imgHills, imgStars;
	private long lastDrawTime = 0;
	
  public void setup() {
  	String mainSceneDir = BASE_DIR + "data/main_scene/";
	  imgTrees = loadImage(mainSceneDir + "trees.png");
	  imgGrass = loadImage(mainSceneDir + "grass.png");
	  imgHills = loadImage(mainSceneDir + "hills.png");
	  imgStars = loadImage(mainSceneDir + "stars.png");
 
	  smooth();
	  size(SCREEN_WIDTH, SCREEN_HEIGHT, P2D);
  }

  public void draw() {
  	int now = millis();
  	int t = now % ANIMATION_PERIOD_MS;
  	animate(t);
  	//System.out.println("frame rate = " + frameRate);
  }
  
  // t \in [0, ANIMATION_PERIOD_MS)
  private void animate(int t) {
  	translate(CENTER_X, CENTER_Y);
  	
  	animateObject(t, imgStars, MAX_ZOOM_STARS);  	
  	animateObject(t, imgHills, MAX_ZOOM_HILLS);
  	animateObject(t, imgTrees, MAX_ZOOM_TREES);
  	animateObject(t, imgGrass, MAX_ZOOM_GRASS);
  }
  
  private void animateObject(int t, PImage img, float maxZoom) {
  	
  	//float animZoom = f(t, maxZoom);
  	
//  	float zoomDelta = animZoom - 1.0f;
//  	int x = (int) (-CENTER_X * zoomDelta);
//  	int y = (int) (-CENTER_Y * zoomDelta);
  	pushMatrix();
  		float animZoom = f(t, maxZoom);
  		scale(animZoom);
  		image(img, -CENTER_X, -CENTER_Y);
  	popMatrix();
  }
  
  private float f(int t, float maxZoom) {
  	assert(t >= 0 && t < ANIMATION_PERIOD_MS);
  	
  	double speed = 2 * (maxZoom - 1.0) / ANIMATION_PERIOD_MS;
  	// camera move forward
  	if(t <= ANIMATION_PERIOD_MS / 2) {
  		return (float) (1.0 + speed * t);
  	}
  	// camera move backward
  	else {
  		return (float) (1.0 + speed * (ANIMATION_PERIOD_MS - t) );
  	}
  } 
}
