package me.tatetian.stars;

import me.tatetian.Engine;
import me.tatetian.common.Drawable;
import processing.core.PImage;
import processing.opengl.PShader;

public class SpriteStarsRenderer implements StarsRenderable {
	private PShader starShader;
	private int starColor;
	private float weight;
	private Engine E;
	
	public SpriteStarsRenderer(Engine e, String starImgPath, float weight) {
		this.E = e;
	  this.weight = weight;
	  this.starColor = E.color(255);
		
		this.starShader = E.loadShader(e.BASE_PATH + "spritefrag.glsl", 
														   		 e.BASE_PATH + "spritevert.glsl");
	  this.starShader.set("weight", weight);
	  PImage star = E.loadImage(e.BASE_PATH + starImgPath);
	  this.starShader.set("sprite", star);  
	}
	
	public void setStarColor(int starColor) {
		this.starColor = starColor;
	}
	
	public void draw(Star[] stars) {		
		E.shader(starShader, E.POINTS);
		E.strokeWeight(weight);
	  E.strokeCap(E.SQUARE);
	  E.stroke(starColor);
	  for(Star star : stars)
	  	E.point(star.x, star.y, star.z);
	  E.resetShader();
	}
}
