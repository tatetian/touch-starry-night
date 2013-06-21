package me.tatetian.scene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import processing.core.PGraphics;

import me.tatetian.Engine;
import me.tatetian.effects.Animation;
import me.tatetian.effects.SceneTransition;
import me.tatetian.effects.Star2TextAnimationMaker;
import me.tatetian.effects.Star2TextAnimationMaker.Stars2TextAnimation;
import me.tatetian.effects.StarFlyingAnimation;
import me.tatetian.effects.ZoomFadeSceneTransition;
import me.tatetian.stars.Nebula;
import me.tatetian.stars.SpriteStarsRenderer;
import me.tatetian.stars.Star;
import me.tatetian.stars.StarText;
import me.tatetian.stars.Stars;
import me.tatetian.stars.StarsGenerator;

public class NebulaScene extends Scene {
	/* =======================================================================
	 * Nebulas Cache
	 * =======================================================================*/
	public static Map<Name, NebulaScene> scene_cache;
	public static NebulaScene get(Name name) {
		if(scene_cache == null) 
			scene_cache = new HashMap<Name, NebulaScene>();
		
		// special rule to combine CHE2 and CHE3
		if(name == Name.CHE3) name = Name.CHE2;
		
		NebulaScene scene = scene_cache.get(name);
		if(scene == null && NebulaScene.isSceneAvailable(name)) {
			
			scene = new NebulaScene(name);
			scene_cache.put(name, scene);
		}
		return scene;
	}
	
	private static final char[] KEYS = new char[] {
		'0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9',
		'a', 'b', 'c'
	} ;
	public static char getKey(Name name) {
		if(name == Name.CHE3) name = Name.CHE2;
		
		return KEYS[name.ordinal()-1];
	}
	
	private Properties properties;
	
	private Nebula nebula;
	private NebulaSceneBackground background;
	private StarText starText;
	private String scenePath;
	
	private int lastClickTime = -1;
	private boolean textShown = false;
	private Animation nebulaAnim;
	
	private Stars flyingStars;
	private Animation flyingAnim;

	private char sceneKey;
	
	private Stars hiddenStars;
		
	private Star2TextAnimationMaker stars2TextAnimator;

	public static boolean isSceneAvailable(Name name) {
		String sceneDir = E.BASE_PATH + "nebula_scenes/" + name + "/";
		File dir = new File(sceneDir);
		return dir.listFiles() != null;
	}
	
	public NebulaScene(Name name) {
		super(name);
		sceneKey = getKey(name);
	}
	
	@Override
	protected void setup() {
		this.properties = new Properties();
		this.scenePath = "nebula_scenes/" + name + "/";
  
		loadProperties();
		// init background
		float bg_dx = getFloat("background.dx", 0),
					bg_dy = getFloat("background.dy", 0);
		float bg_z  = getFloat("background.z", 3);
		background = new NebulaSceneBackground(G, E.WIN_W / 2 + bg_dx, E.WIN_H / 2+ bg_dy, 
																						scenePath + "background.png", E.WIN_D / bg_z );
		// init nebula
		int numNebulaStars = getInt("nebula.stars.total", 60);
		Stars[] stars = generateStars(numNebulaStars);
		nebula = new Nebula(G, stars, E.WIN_W / 2, E.WIN_H / 2, E.WIN_D);
		// init text
		initText();
		// init moving stars
		int numFlyingStars = getInt("flying.stars.total", 500);
		flyingStars = generateFlyingStars(numFlyingStars, 1.5f, 10 * E.WIN_D, - E.WIN_D); 
		flyingStars.save();
	}
		
	private void initText() {
		String quote = properties.getProperty("quote");
		float quote_x = getFloat("quote.x", 0),
					quote_y = getFloat("quote.y", 0);
		int quote_size = getInt("quote.size", 140);
		starText = new StarText(G, scenePath + "flying_star.png", quote, quote_size,
														quote_x, quote_y, E.WIN_D );
		starText.hide(0);
	}

	@Override
	public void show() {
		textShown = false;
		
		background.reset();
		background.show(4000);
		
		String bgAnimType = getString("background.anim.type", "rotation");
		if(bgAnimType.equals("loop-fly")) {
			float from_x = getFloat("background.anim.from_x", -800),
						from_y = getFloat("background.anim.from_y", -300),
						to_x 	 = getFloat("background.anim.to_x", 800),
						to_y	 = getFloat("background.anim.to_y", 300);
			int time	 = getInt("background.anim.time", 100000);
			background.fly(from_x, from_y, to_x, to_y, time);
		}
		else
			background.rotate(0.0004f, -1);
		
		nebula.reset();		
		nebula.show(4000);
//		float rot_x = getFloat("nebula.animation.x", 0),
//					rot_y = getFloat("nebula.animation.y", 0);
//		nebula.setRotationCenter(rot_x, rot_y);
		nebulaAnim = nebula.rotate(0.0004f, -1);
				
		flyingStars.reset();
		flyingAnim = new StarFlyingAnimation(flyingStars, 10 * E.WIN_D, - 5 * E.WIN_D, 1);
		addAnimation(flyingAnim);
		
		initStar2TextAnimation();
		
		lastClickTime = E.millis();
	}
	
	private void initStar2TextAnimation() {
		Star[] textStars  	= starText.stars();
		Stars[] nebulaStars = nebula.stars();
		Star[] flyingStars	= this.flyingStars.stars();

		int numAllStars			= textStars.length;
		int numNebulaStars	= countStars(nebulaStars);
		int numFlyingStars	= flyingStars.length;
		
		int numHiddenStars  = Math.max(numAllStars - numNebulaStars - numFlyingStars, 0);
		hiddenStars	= generateFlyingStars(numHiddenStars, 2.5f, 11 * E.WIN_D, 10 * E.WIN_D);
		
		Stars[] allStars = new Stars[nebulaStars.length + 2];
		System.arraycopy(nebulaStars, 0, allStars, 0, nebulaStars.length);
		allStars[allStars.length - 2] = this.flyingStars;
		allStars[allStars.length - 1] = this.hiddenStars;
		stars2TextAnimator = new Star2TextAnimationMaker(starText, allStars, nebula);
	}
	
	private int countStars(Stars[] stars) {
		int count = 0;
		for(Stars ss: stars) 
			count += ss.stars().length;
		return count;
	}
	
	@Override
	protected void drawGraphics() {
		G.background(0);
		G.blendMode(G.ADD);
		// draw nebula background
		background.draw();
		// draw nebula stars
		nebula.draw();
		// draw flying stars
		G.pushMatrix();
		G.translate(E.WIN_W /2,  E.WIN_H / 2, E.WIN_D);
		if(flyingStars != null) flyingStars.draw();
		if(hiddenStars != null) hiddenStars.draw();
		G.popMatrix();
		// draw text
		starText.draw();
	}
	
	@Override
	protected void beforeDraw() {
		G.imageMode(G.CENTER);
		G.blendMode(G.ADD);
		G.hint(G.DISABLE_DEPTH_TEST);
		G.lights();
	}
	
	/**
	 * All nebulas scenes have the same transition
	 * */
	@Override
	public void transit(Scene fromScene) {
		// if in transition, no effect
		if(this.fromScene != null) return;
		
		this.fromScene = fromScene;
		if(fromScene instanceof MainScene) {
			SceneTransition trans = new ZoomFadeSceneTransition(1000, fromScene, this, true);
			E.addAnimation(trans);
		}
	}
	
	@Override
	public void press(char key) {
		// while in transition, reject any key event
		if(fromScene != null) return;
		
		// if press 'm', then go back to main scene
		if(key == sceneKey) 
			click(0, 0);
		else //if(key == 'm')
			E.switchScene(E.getMainScene());
	}
	
	@Override
	public void click(int clientX, int clientY) {
		if(E.millis() - lastClickTime > 3000) {
			lastClickTime = E.millis();
			if(!textShown) {
				if(nebulaAnim != null) nebulaAnim.pause();
				if(flyingAnim != null) flyingAnim.pause();
				background.hide(2000);
//				starText.delay(500);
//				starText.show(1500);
			//	starText.show(0);
				// Moving star to form the shape of text
				Stars2TextAnimation anim = stars2TextAnimator.toText(2000);
				//anim.delay(1000);
				E.addAnimation(anim);
				
				textShown = true;
			}
			else {	
				// Moving star back to their origin positions 
				Stars2TextAnimation anim = stars2TextAnimator.backStars(2000);
				E.addAnimation(anim);
	
//				starText.hide(1000);
//				background.delay(1000);
				background.show(2000);
				
				if(nebulaAnim != null) nebulaAnim.resume();
				if(flyingAnim != null) flyingAnim.resume();
				
				textShown = false;
			}
		}
	}
	
	public Stars[] generateStars(int totalStars) {
		float xy_scale = 1.5f;
		float min_z = -200;
		float max_z = 0;
		float star_size = 50;
		return generateStars(totalStars, xy_scale, min_z, max_z, star_size);
	}
	
		public Stars[] generateStars(int totalStars,float xy_scale, 
																 float min_z, float max_z, 
																 float star_size) {
			// list all star files
			String baseDir = E.BASE_PATH + scenePath;
			File sceneFolder  = new File(baseDir);
			File[] sceneFiles = sceneFolder.listFiles();
			List<String> starImgFiles = new ArrayList<String>();
			for(File f : sceneFiles) {
				String fn = f.getName();
				// star image files are 1.png, 2.png, ...
				if( fn.endsWith(".png") && Character.isDigit( fn.charAt(0) ) ) {
					starImgFiles.add(fn);
				}
			}
			// construct stars
			Stars[] stars = new Stars[starImgFiles.size()];
			for(int si = 0; si < stars.length; si++) {
				int numStars = totalStars / stars.length;
				stars[si] = new Stars(
					StarsGenerator.generate(numStars, E.WIN_W, E.WIN_H, xy_scale, min_z, max_z),
					new SpriteStarsRenderer(G, baseDir + starImgFiles.get(si), star_size) );	
			}
			return stars;
		}

	private Stars generateFlyingStars(int numStars, float scale_xy, float min_z, float max_z) {
		return new Stars( 
							StarsGenerator.generate(numStars, E.width, E.height, scale_xy, min_z, max_z), 
							new SpriteStarsRenderer(G, E.BASE_PATH + scenePath + "flying_star.png", 60) );
	}
		
	private void loadProperties() {
		try {
  		String propertyPath = E.BASE_PATH + scenePath + "scene.properties";
  		properties.load(new FileInputStream(propertyPath));
  	}
  	catch(IOException e) {
  		throw new RuntimeException(e);
  	}
	}	
		
	private String getString(String key) {
		return getString(key, null);
	}
	
	private String getString(String key, String defaultValue) {
		String strVal = properties.getProperty(key);
		return strVal != null ? strVal : defaultValue;
	}
	
	private int getInt(String key, int defaultVal) {
		String strVal = properties.getProperty(key);
		if(strVal == null) 
			return defaultVal;
		else 
			return Integer.parseInt(strVal);
	}
	
	private float getFloat(String key, float defaultVal) {
		String floatVal = properties.getProperty(key);
		if(floatVal == null) 
			return defaultVal;
		else 
			return Float.parseFloat(floatVal);
	}
}
