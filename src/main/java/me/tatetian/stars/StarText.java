package me.tatetian.stars;

import java.util.ArrayList;
import java.util.List;

import geomerative.RFont;
import geomerative.RG;
import geomerative.RGroup;
import geomerative.RPoint;
import me.tatetian.common.DrawableObject;
import me.tatetian.common.Transparentible;
import me.tatetian.effects.Showable;

public class StarText extends DrawableObject implements Showable, Transparentible {
	private Star[] stars;
	private StarsRenderable renderer; 
	private Stars nebula;
	
	private static final int MIN_ALPHA = 0, MAX_ALPHA = 150;
	
	public StarText(String starImgPath, 
									String text, int textSize, 
									float x, float y, float z) {
		super(x, y, 0, 0, 255);
		// prepare to process text
	  RG.init(E);	  
	  RFont font =new RFont(E.BASE_PATH + "new_spirit.ttf");
	  font.setAlign(RFont.CENTER);
	  font.setSize(textSize);	  
  	RG.setPolygonizer(RG.UNIFORMLENGTH);
  	RG.setPolygonizerLength(textSize / 15);
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
	  		starList.add(new Star(p.x, p.y, z));
	  	delta_y += line_height;
	  }
		stars = starList.toArray(new Star[]{});
		
		renderer = new SpriteStarsRenderer(E, starImgPath, 100); 
	  nebula = new Stars(stars, renderer);
	}
	
	public void draw() {
		if(alpha > 0) {
			E.pushMatrix();
			transform();
			renderer.setStarColor(E.color(255, 255, 255, alpha));
			nebula.draw();
			E.popMatrix();
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
