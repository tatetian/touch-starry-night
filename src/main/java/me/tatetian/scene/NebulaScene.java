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
		NebulaScene scene = scene_cache.get(name);
		if(scene == null && NebulaScene.isSceneAvailable(name)) {
			scene = new NebulaScene(name);
			scene_cache.put(name, scene);
		}
		return scene;
	}	
	
	private Properties properties;
	
	private Nebula nebula;
	private NebulaSceneBackground background;
	private StarText starText;
	private String scenePath;
	
	private int lastClickTime = -1;
	private Animation nebulaAnim;
	private boolean textShown = false;
	
	private Stars[] flyingStars;
		
	private Star2TextAnimationMaker stars2TextAnimator;

	public static boolean isSceneAvailable(Name name) {
		String sceneDir = E.BASE_PATH + "nebula_scenes/" + name + "/";
		File dir = new File(sceneDir);
		return dir.listFiles() != null;
	}
	
	public NebulaScene(Name name) {
		super(name);
	}
	
	@Override
	protected void setup() {
		this.properties = new Properties();
		this.scenePath = "nebula_scenes/" + name + "/";
  
		loadProperties();
		// init background
		background = new NebulaSceneBackground(G, scenePath + "background.png", E.WIN_D / 3);
		// init nebula
		int numNebulaStars = getInt("nebula.stars.total", 60);
		Stars[] stars = generateStars(numNebulaStars);
		nebula = new Nebula(G, stars, E.WIN_W / 2, E.WIN_H / 2, E.WIN_D);
		// init text
		initText();
		// init moving stars
		flyingStars = new Stars[] { new Stars(
							StarsGenerator.generate(500, E.width, E.height, 1.5f, 10 * E.WIN_D, - E.WIN_D), 
							new SpriteStarsRenderer(G, E.BASE_PATH + scenePath + "flying_star.png", 60) ) };
	}
	
	private void initText() {
		String quote = properties.getProperty("quote");
		float quote_x = getFloat("quote.x", 0),
					quote_y = getFloat("quote.y", 0);
		int quote_size = getInt("quote.size", 120);
		starText = new StarText(G, scenePath + "flying_star.png", quote, quote_size,
														E.WIN_W / 2 + quote_x, E.WIN_H / 2 + quote_y, E.WIN_D );
		starText.hide(0);
	}

	@Override
	public void show() {		
		background.show(4000);
		background.rotate(0.0004f, -1);
		nebula.reset();		
		nebula.show(4000);
		nebulaAnim = nebula.rotate(0.0004f, -1);
		starText.reset();		
		stars2TextAnimator = new Star2TextAnimationMaker(starText, nebula.stars(), nebula);
		
		lastClickTime = E.millis();
		
		StarFlyingAnimation a = new StarFlyingAnimation(flyingStars, 
																  10 * E.WIN_D, - 5 * E.WIN_D, 1);
		addAnimation(a);
	}
	
	@Override
	protected void drawGraphics() {
		G.background(0);
		// draw nebula background
		background.draw();
		// draw nebula stars
		nebula.draw();
		// draw flying stars
		for(Stars ss : flyingStars) {
			ss.draw();
		}
		// draw text
		starText.draw();
	}
	
	@Override
	protected void beforeDraw() {
		G.imageMode(G.CENTER);
		G.blendMode(G.BLEND);
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
		// if press 'm', then go back to main scene
		if(key == 'm')
			E.switchScene(E.getMainScene());
	}
	
	@Override
	public void click(int clientX, int clientY) {
		if(E.millis() - lastClickTime > 4000) {
			lastClickTime = E.millis();
			if(!textShown) {
				if(nebulaAnim != null) nebulaAnim.pause();
				background.hide(2000);
				starText.delay(1000);
				starText.show(1500);
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
	
				starText.hide(1000);
				background.delay(500);
				background.show(1500);
				nebulaAnim.resume();
				
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
			
//			Stars[] stars = new Stars[7];
//			stars[0] = new Stars(
//					StarsGenerator.generate(5, E.width, E.height, 1.5f, -200, 0), 
//					new SpriteStarsRenderer(G, "small_stars/1.png", 50) );
//			stars[1] = new Stars(
//					StarsGenerator.generate(10, E.width , E.height, 1.5f, -200, 0), 
//					new SpriteStarsRenderer(G, "small_stars/2.png", 50) );
//			stars[2] = new Stars(
//					StarsGenerator.generate(2, E.width, E.height, 1.5f, 0, 200), 
//					new SpriteStarsRenderer(G, "small_stars/3.png", 50) );
//			stars[3] = new Stars(
//					StarsGenerator.generate(25, E.width, E.height, 1.75f, -100, 0), 
//					new SpriteStarsRenderer(G, "small_stars/4.png", 50) );
//			stars[4] = new Stars(
//					StarsGenerator.generate(10, E.width, E.height, 1.5f, -200, -100), 
//					new SpriteStarsRenderer(G, "small_stars/5.png", 50) );
//			stars[5] = new Stars(
//					StarsGenerator.generate(6, E.width, E.height, 1.5f, -1200, -200), 
//					new SpriteStarsRenderer(G, "small_stars/6.png", 20) );
//			stars[6] = new Stars(
//					StarsGenerator.generate(10, E.width, E.height, 2f, -200, 0), 
//						new SpriteStarsRenderer(G, "small_stars/7.png", 50) );
			
//
//			Stars[] stars = new Stars[1];
//			stars[0] = new Stars(
//					new Star[] { new Star(0, 0, 0) },
//					new SpriteStarsRenderer(E, "small_stars/1.png", 100) );
//			
			return stars;
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
		return properties.getProperty(key);
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
