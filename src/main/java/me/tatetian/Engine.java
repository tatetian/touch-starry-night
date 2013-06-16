package me.tatetian;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.tatetian.common.Drawable;
import me.tatetian.effects.Animation;
import me.tatetian.scene.MainScene;
import me.tatetian.scene.NebulaScene;
import me.tatetian.scene.Scene;

import processing.core.PApplet;

public class Engine extends PApplet {	
	public final float FOV = PI / 3;
	public final int WIN_W  = 1440;	// width of window
	public final int WIN_H = 810;		// height of window
	public final int WIN_D  = (int) (- WIN_H / 2 / tan(FOV / 2));	// standard depth of window
	public static final String BASE_PATH  = "../../data/";
	public final int FRAME_RATE  	= 25;
	
	private Controller controller;
	
	private LinkedList<Animation> animations;
	private List<Animation> newlyAdded;
	private Map<Scene, LinkedList<Animation>> animMemory;
	
	private Scene currentScene;
	private MainScene mainScene;
	
	public void setup() {		
		Drawable.setEngine(this);
		Animation.setEngine(this);
		
		// init processing
		size(WIN_W, WIN_H, P3D);
		background(0);
		smooth(4);
		imageMode(CENTER);
	  frameRate(FRAME_RATE);
	  
	  perspective(FOV, (float)WIN_W/WIN_H, 1, WIN_D *10); 
	  camera(WIN_W/2, WIN_H/2, 1, WIN_W/2, WIN_H/2, 0, 0, 1, 0); 
	  
	  background(0);
	  
		// init variables
		controller 		= new Controller(this);
		mainScene 		= new MainScene();

		animations 		= new LinkedList<Animation>();
		newlyAdded		= new ArrayList<Animation>();
		animMemory		= new HashMap<Scene, LinkedList<Animation>>();
		
		currentScene 	= mainScene;
//		currentScene  = NebulaScene.get(NebulaScene.Name.M51) ;
		//		currentScene 	= new TextScene();
	}
	
	public void draw() {
		background(0);
		lights();
		currentScene.draw();
//		translate(WIN_W/2, WIN_H/2, WIN_D);
//		sphere(50);
		doAnimations();
	}
	
	public void mouseClicked() {
		controller.click(mouseX, mouseY);
	}

	public void keyPressed() {
		controller.press(key);
	}
	
	public Scene getMainScene() {
		return mainScene ;
	}
	
	public void switchScene(Scene scene) {
		Scene fromScene = currentScene;
		currentScene = scene;	
		
		saveNewAnimations();
		pauseAnimations();
		animMemory.put(fromScene, animations);
		animations = animMemory.get(currentScene);
		if(animations == null)
			animations = new LinkedList<Animation>();
		resumeAnimations();
						
		currentScene.transit(fromScene);
	}
	
	public void addAnimation(Animation a) {
		newlyAdded.add(a);
		a.start();
	}
	
	private void saveNewAnimations() {
		if(!newlyAdded.isEmpty()) {
			animations.addAll(newlyAdded);
			newlyAdded.clear();
		}
	}
	
	private void pauseAnimations() {
		for(Animation a : animations) 
			a.pause();
	}
	
	private void resumeAnimations() {
		for(Animation a : animations)
			a.resume();
	}
	
	public void doAnimations() {
		saveNewAnimations();
		
		Iterator<Animation> iter = animations.iterator() ;
		while(iter.hasNext()) {
			Animation a = iter.next();
			a.step();
			if(a.progress() == 1) {
				iter.remove();
				a.end();
			}
		}
	}
	
	public static class Controller {
		private Engine engine;
		
		public Controller(Engine engine) {
			this.engine = engine;
		}
		
		public void click(int mouseX, int mouseY) {
			engine.currentScene.click(mouseX, mouseY);
		}
		
		public void press(char key) {
			engine.currentScene.press(key);
		}
	}
}
