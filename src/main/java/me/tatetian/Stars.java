package me.tatetian;

import processing.core.PApplet;
import processing.core.PImage;

public class Stars extends PApplet {	
	private Star[] stars;
	private PImage bg_image;
	
	public void setup() {
		Processing.language = this;
		
		size(1440, 810, P2D);
	  smooth();
	  frameRate(24);

	  colorMode(HSB, 360, 99, 99);	  
	  //fill(255,102,0);
	  stroke(0);

	  bg_image = loadImage("../../data/max.png");
	  background(0);
	  
	  // Nebulas
	  GraphicsStarGenerator purpleStarGen = GraphicsStarGenerator.getPurple();
	  Nebulas.RandomNebulas purpleNebulas = new Nebulas.RandomNebulas(20, 60, width, height, purpleStarGen);
	  GraphicsStarGenerator pinkStarGen = GraphicsStarGenerator.getPink();
	  Nebulas.RandomNebulas pinkNebulas = new Nebulas.RandomNebulas(12, 32, width, height, pinkStarGen);
	  Nebulas.CompositeNebulas nebulas = new Nebulas.CompositeNebulas();
	  nebulas.add(pinkNebulas, 0.8f);
	  nebulas.add(purpleNebulas, 0.2f);
	  stars = nebulas.generate(50);
	}
	
	public void draw() {
		background(0);
		image(bg_image, 0, 0, width, height);
		for(Star star : stars)
			star.draw();
	}
}
