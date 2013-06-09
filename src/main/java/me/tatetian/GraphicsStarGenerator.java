package me.tatetian;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PGraphics;

public class GraphicsStarGenerator implements StarGeneratable {
	private Map<Integer, PGraphics> existingStars;
	
	private float relativeRadius;
	private int color;
	private int innerAdjustBase;
	private int innerAdjustStep;
	private int outerAdjustBase;
	private int outerAdjustStep;
	
	public static GraphicsStarGenerator getPurple() {
		int hsbPurple = Processing.language.color(262, 84, 61);
		return new GraphicsStarGenerator(0.2f, hsbPurple, 0, 200, 25 ,20);
	}
		
	public static GraphicsStarGenerator getWhite() {
		int hsbWhite = Processing.language.color(300, 84, 61);
		return new GraphicsStarGenerator(0.2f, hsbWhite, 0, 200, 25, 20);
	}

	public static GraphicsStarGenerator getPink() {
		int hsbWhite = Processing.language.color(300, 84, 61);
		return new GraphicsStarGenerator(0.2f, hsbWhite, 0, 200, 25, 20);
	}
	
	public GraphicsStarGenerator(float relativeRadius, int color) {
		this(relativeRadius, color, 0, 200, 25, 20);
	}
	
	public GraphicsStarGenerator(float relativeRadius, int color,
															 int innerAdjustBase, int innerAdjustStep, 
															 int outerAdjustBase, int outerAdjustStep) {
		this.relativeRadius = relativeRadius;
		this.color = color;
		this.existingStars = new HashMap<Integer, PGraphics>();
		this.innerAdjustBase = innerAdjustBase;
		this.innerAdjustStep = innerAdjustStep;
		this.outerAdjustBase = outerAdjustBase;
		this.outerAdjustStep = outerAdjustStep;
	}
	
	public Star generate(int x, int y, int size) {
		PGraphics canvas = existingStars.get(size);
		if(canvas == null) {
			PApplet P = Processing.language;

			int center_x = size / 2, center_y = size / 2;
			int radius = (int) (relativeRadius * size / 2);
			int h = (int) P.hue(color), 
				  s = (int) P.saturation(color),
				  b = (int) P.brightness(color);
			
			canvas = P.createGraphics(size, size);
			canvas.colorMode(P.HSB, 360, 99, 99);
			canvas.smooth();
			canvas.noStroke();
			canvas.fill(color);
			
			canvas.beginDraw();
			canvas.loadPixels();
			for(int xx = 0; xx < size; xx++) {
				for(int yy = 0; yy < size; yy++) {
					float d = P.dist(center_x, center_y, xx, yy);
					float relative_d = (radius-d)/radius;
					// add noise
					//relative_d = relative_d * ( 1 + 0.5f * (P.noise(xx, yy) - 0.5f));
					int adjust = (int) (relative_d >= 0 ? 
																innerAdjustBase + innerAdjustStep * relative_d : 
															- outerAdjustBase + outerAdjustStep * relative_d);
					int new_b = P.constrain(b + adjust, 0, 90);
					int new_s = P.constrain(s - adjust, 5, 99);
					int new_a = new_b < 20 ? new_b : 255 ; 
					canvas.pixels[xx + yy * size] = P.color(h, new_s, new_b, new_a);
				}
			}
			canvas.updatePixels();
			canvas.endDraw();
		}
		return new Star(x, y, canvas);
	}

}
