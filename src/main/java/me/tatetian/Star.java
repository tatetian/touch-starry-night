package me.tatetian;

import processing.core.PApplet;
import processing.core.PImage;

public class Star {
	public 	int x, y;
	private PImage image;
	
	public Star(int x, int y, PImage image) {
		this.x = x;
		this.y = y;
		this.image = image;
	}
	
	public void draw() {
		Processing.language.image(image, x - image.width / 2, y - image.height / 2);
	}
}
