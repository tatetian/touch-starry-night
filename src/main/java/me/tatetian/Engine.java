package me.tatetian;

import java.util.Map;

import me.tatetian.scene.MainScene;
import me.tatetian.scene.NebulaScene;
import me.tatetian.scene.NebulaScene;
import me.tatetian.scene.Scene;

import processing.core.PApplet;

public class Engine extends PApplet {
	public static final int WINDOW_WIDTH  = 1440;
	public static final int WINDOW_HEIGHT = 810;
	public static final int WINDOW_DEPTH  = 10 * WINDOW_HEIGHT;
	public static final String BASE_PATH  = "../../data/";
	
	private Controller controller;
	
	private Scene currentScene;
	private MainScene mainScene;
	
	public static Engine INSTANCE;
	
	public void setup() {		
		INSTANCE			= this;
		
		// init processing
		size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
		background(0);
		smooth();
		imageMode(CENTER);
	  frameRate(24);
	  
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
		currentScene	= NebulaScene.get(NebulaScene.Name.M51);
	}
	
	public void draw() {
		background(0);
		lights();
		
		blendMode(ADD);
		currentScene.draw();
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
	
	public static class Controller {
		private Engine engine;
		
		public Controller(Engine engine) {
			this.engine = engine;
		}
		
		public void click(int mouseX, int mouseY) {
			if( engine.isMainScene() ) {
				
			}
		}
		
		public void press(char key) {
			
		}
	}
}
