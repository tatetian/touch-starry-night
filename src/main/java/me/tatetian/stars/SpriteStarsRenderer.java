package me.tatetian.stars;

import me.tatetian.Engine;
import me.tatetian.common.Drawable;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;

public class SpriteStarsRenderer implements StarsRenderable {
	private PShader starShader;
	private int starColor;
	private float weight;
	private PGraphics G;
	
	public SpriteStarsRenderer(PGraphics G, String starImgPath, float weight) {
		this.G = G;
	  this.weight = weight;
	  this.starColor = G.color(255);
		
		this.starShader = G.loadShader(Engine.BASE_PATH + "spritefrag.glsl", 
														   		 Engine.BASE_PATH + "spritevert.glsl");
	  this.starShader.set("weight", weight);
	  PImage star = Drawable.E.loadImage(Engine.BASE_PATH + starImgPath);
	  this.starShader.set("sprite", star);  
	}
	
	public void setStarColor(int starColor) {
		this.starColor = starColor;
	}
	
	public void draw(Star[] stars) {		
		G.shader(starShader, G.POINTS);
		G.strokeWeight(weight);
	  G.strokeCap(G.SQUARE);
	  G.stroke(starColor);
	  for(Star star : stars)
	  	G.point(star.x, star.y, star.z);
	  G.resetShader();
	}
}
