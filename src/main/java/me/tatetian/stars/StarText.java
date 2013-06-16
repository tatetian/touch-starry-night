package me.tatetian.stars;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

import geomerative.RFont;
import geomerative.RG;
import geomerative.RGroup;
import geomerative.RPoint;
import me.tatetian.Engine;
import me.tatetian.common.DrawableObject;
import me.tatetian.common.Transparentible;
import me.tatetian.effects.Showable;

public class StarText extends DrawableObject implements Showable, Transparentible {
	private Star[] stars;
	private StarsRenderable renderer; 
	private Stars nebula;
	
	public StarText(PGraphics G,
									String starImgPath, 
									String text, int textSize, 
									float x, float y, float z) {
		super(G, x, y, z, 0, 255);
		MAX_ALPHA = 50;
		// prepare to process text
	  RG.init(E);	  
	  RFont font =new RFont(Engine.BASE_PATH + "new_spirit.ttf");
	  font.setAlign(RFont.CENTER);
	  font.setSize(textSize);	  
  	RG.setPolygonizer(RG.UNIFORMLENGTH);
  	RG.setPolygonizerLength(textSize / 12);
  	float line_height = font.getLineSpacing();
  	
	  // init stars
	  List<Star> starList = new ArrayList<Star>();
	  String[] lines = text.split("\n");
	  float delta_y = 0;
	  for(String line : lines) {
	  	RGroup grp = font.toGroup(line);
	  	grp.translate(0, delta_y);
	  	RPoint[] points = grp.getPoints();
	  	for(RPoint p : points) 
	  		starList.add(new Star(p.x, p.y, 0));
	  	delta_y += line_height;
	  }
		stars = starList.toArray(new Star[]{});
//		stars = new Star[] { new Star(0, 0, 0) };
		renderer = new SpriteStarsRenderer(G, starImgPath, 10); 
	  nebula = new Stars(stars, renderer);
	}
	
	public void draw() {
		if(alpha > 0) {
			G.pushMatrix();
			transform();
			renderer.setStarColor(E.color(255, 255, 255, alpha));
			nebula.draw();
			G.popMatrix();
		}
	}

	public void show(int millis) {
		fadeIn(millis);
	}

	public void hide(int millis) {
		fadeOut(millis);
	}
	
	public Star[] stars() {
		return stars;
	}
}
