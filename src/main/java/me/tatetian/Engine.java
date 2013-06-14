package me.tatetian;

import java.util.Iterator;
import java.util.LinkedList;

import me.tatetian.common.Drawable;
import me.tatetian.effects.Animation;
import me.tatetian.scene.MainScene;
import me.tatetian.scene.NebulaScene;
import me.tatetian.scene.Scene;

import processing.core.PApplet;

public class Engine extends PApplet {
	public final int WINDOW_WIDTH  = 1440;
	public final int WINDOW_HEIGHT = 810;
	public final int WINDOW_DEPTH  = 10 * WINDOW_HEIGHT;
	public final String BASE_PATH  = "../../data/";
	public final int FRAME_RATE  	= 25;
	
	private Controller controller;
	private LinkedList<Animation> animations;
	
	private Scene currentScene;
	private MainScene mainScene;
	
	public void setup() {		
		Drawable.setEngine(this);
		Animation.setEngine(this);
		
		// init processing
		size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
		background(0);
		smooth();
		imageMode(CENTER);
	  frameRate(FRAME_RATE);
	  
	  float fov = PI/3;
	  float cameraZ = (height/2) / tan(fov/2);
	  assert(10 * cameraZ == WINDOW_DEPTH);
	  System.out.println((cameraZ + cameraZ/10) + ":" + (cameraZ + cameraZ*10));
	  perspective(fov, (float)width/height, cameraZ/10, cameraZ*10); 
	  camera(width/2, height/2, cameraZ / 10, width/2, height/2, 0, 0, 1, 0); 
	  
	  background(0);
	  
		// init variables
		controller 		= new Controller(this);
		mainScene 		= new MainScene();
		animations 		= new LinkedList<Animation>();

		currentScene  = NebulaScene.get(NebulaScene.Name.M51) ;
		//		currentScene 	= new TextScene();
	}
	
	public void draw() {
		background(0);
		lights();
		
		blendMode(ADD);
		hint(DISABLE_DEPTH_TEST);
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
		animations.add(a);
		a.start();
	}
	
	public void doAnimations() {
		Iterator<Animation> iter = animations.iterator() ;
		while(iter.hasNext()) {
			Animation a = iter.next();
			a.step();
			if(a.finished()) iter.remove();
		}
	}
	
	public static class Controller {
		private Engine engine;
		
		public Controller(Engine engine) {
			this.engine = engine;
		}
		
		public void click(int mouseX, int mouseY) {
			if( engine.isMainScene() ) {
				
			}
			else {
				NebulaScene nbs = (NebulaScene) engine.currentScene;
				nbs.click(mouseX, mouseY);
			}
		}
		
		public void press(char key) {
			
		}
	}
}
