package me.tatetian.stars;

import me.tatetian.Engine;
import processing.core.PImage;
import processing.opengl.PShader;

public class SpriteStarsRenderer implements StarsRenderable {
	private PShader starShader;
	private int starColor;
	private float weight;
	
	public SpriteStarsRenderer(String starImgPath, float weight) {
		Engine e = Engine.INSTANCE;
				
	  this.weight = weight;
	  this.starColor = e.color(255);
		
		this.starShader = e.loadShader(Engine.BASE_PATH + "spritefrag.glsl", 
														   Engine.BASE_PATH + "spritevert.glsl");
	  this.starShader.set("weight", weight);
	  PImage star = e.loadImage(Engine.BASE_PATH + starImgPath);
	  this.starShader.set("sprite", star);  
	}
	
	public void setStarColor(int starColor) {
		this.starColor = starColor;
	}
	
	public void draw(Star[] stars) {		
		Engine e = Engine.INSTANCE;
		e.shader(starShader, e.POINTS);
		e.strokeWeight(weight);
	  e.strokeCap(e.SQUARE);
	  e.stroke(starColor);
	  for(Star star : stars)
	  	e.point(star.x, star.y, star.z);
	  e.resetShader();
	}
}
