package me.tatetian;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import me.tatetian.common.Drawable;
import me.tatetian.effects.Animation;
import me.tatetian.scene.MainScene;
import me.tatetian.scene.NebulaScene;
import me.tatetian.scene.Scene;

import processing.core.PApplet;

public class Engine extends PApplet {	
	public final float FOV = PI / 3;
	public final int WINDOW_WIDTH  = 1440;
	public final int WINDOW_HEIGHT = 810;
	public final int WINDOW_DEPTH  = (int) (- WINDOW_HEIGHT / 2 / tan(FOV / 2));
	public final String BASE_PATH  = "../../data/";
	public final int FRAME_RATE  	= 25;
	
	private Controller controller;
	private LinkedList<Animation> animations;
	private List<Animation> newlyAdded;
	
	private Scene currentScene;
	private MainScene mainScene;
	
	public void setup() {		
		Drawable.setEngine(this);
		Animation.setEngine(this);
		
		// init processing
		size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
		background(0);
		smooth(4);
		imageMode(CENTER);
	  frameRate(FRAME_RATE);
	  
	  perspective(FOV, (float)WINDOW_WIDTH/WINDOW_HEIGHT, 1, WINDOW_DEPTH *10); 
	  camera(width/2, height/2, 1, width/2, height/2, 0, 0, 1, 0); 
	  
	  background(0);
	  
		// init variables
		controller 		= new Controller(this);
		mainScene 		= new MainScene();

		animations 		= new LinkedList<Animation>();
		newlyAdded		= new ArrayList<Animation>();
		
		currentScene 	= mainScene;
//		currentScene  = NebulaScene.get(NebulaScene.Name.M51) ;
		//		currentScene 	= new TextScene();
	}
	
	public void draw() {
		background(0);
		lights();
		currentScene.draw();
		
		doAnimations();
	}
	
	public void mouseClicked() {
		controller.click(mouseX, mouseY);
	}

	public void keyPressed() {
		controller.press(key);
	}
	
	public boolean isMainScene() {
		return currentScene == mainScene ;
	}
	
	public void switchScene(Scene scene) {
		Scene fromScene = currentScene;
		currentScene = scene;
		currentScene.transit(fromScene);
	}
	
	public void addAnimation(Animation a) {
		newlyAdded.add(a);
		a.start();
	}
	
	public void doAnimations() {
		if(!newlyAdded.isEmpty()) {
			animations.addAll(newlyAdded);
			newlyAdded.clear();
		}
		
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
