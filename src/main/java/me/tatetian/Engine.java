package me.tatetian;

import java.awt.Frame;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

import me.tatetian.common.Drawable;
import me.tatetian.effects.Animation;
import me.tatetian.scene.MainScene;
import me.tatetian.scene.NebulaScene;
import me.tatetian.scene.Scene;

import processing.core.PApplet;

public class Engine extends PApplet {	
	public final float FOV  = PI / 3;
	public final int WIN_W  = 1440;	// width of window
	public final int WIN_H  = 900;	// height of window
	public final int WIN_D  = (int) (- WIN_H / 2 / tan(FOV / 2));	// standard depth of window
	public static final String BASE_PATH  = "data/";
	public final int FRAME_RATE  	= 25;
	
	private Controller controller;
	
	private LinkedList<Animation> animations;
	private List<Animation> newlyAdded;
	
	private Scene currentScene;
	private MainScene mainScene;
	
	private char pressKey = 0;
	
	public static void main(String[] args) {
    PApplet.main("me.tatetian.Engine",
    		// the following parameters are not working. But why?
    		new String[] { "--full-screen", "--hide-stop", 
    									 "--bgcolor=#000000", "--display=1", 
    									 "--location=0,0" } );
  }
	
	// force the window to be full screen
	@Override
	public boolean sketchFullScreen() {
		return true;
	}
	
	@Override
	public void setup() {	
		Drawable.setEngine(this);
		Animation.setEngine(this);
		
		// init processing
		size(WIN_W, WIN_H, P3D);
		frame.setResizable(false);
		
		background(0);
//		smooth(4);
		imageMode(CENTER);
	  frameRate(FRAME_RATE);
	  
	  perspective(FOV, (float)WIN_W/WIN_H, 1, WIN_D *10); 
	  camera(WIN_W/2, WIN_H/2, 1, WIN_W/2, WIN_H/2, 0, 0, 1, 0); 
	  
	  background(0);
	  
		// init variables
		controller 		= new Controller(this);

		animations 		= new LinkedList<Animation>();
		newlyAdded		= new ArrayList<Animation>();

		// init scenes
		mainScene 		= new MainScene();
		switchScene(mainScene);
//		switchScene(NebulaScene.get(NebulaScene.Name.HIP_10064) );
//		switchScene(NebulaScene.get(NebulaScene.Name.LOU1) );
		//		currentScene 	= new TextScene();		
//		switchScene(NebulaScene.get(NebulaScene.Name.M51) );
		TouchEventHandler.start(this);
		
		// play background music
//		Minim minim = new Minim(this);
//		AudioPlayer player = minim.loadFile(BASE_PATH + "sound/background.mp3");
//	  player.play();
//	  player.loop();
	}
	
	@Override
	public void draw() {
		perspective(FOV, (float)WIN_W/WIN_H, 1, WIN_D *10); 
	  camera(WIN_W/2, WIN_H/2, 1, WIN_W/2, WIN_H/2, 0, 0, 1, 0); 

		background(0);
		lights();
		currentScene.draw();
		doAnimations();
		
		// handle key press event generated by arduino in another thread
		if(pressKey > 0) {
			controller.press(pressKey);
			pressKey = 0;
		}
	}
	
	public void mouseClicked() {
		controller.click(mouseX, mouseY);
	}

	public void keyPressed() {
		controller.press(key);
	}

	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public Scene getMainScene() {
		return mainScene ;
	}
	
	public Controller getController() {
		return controller;
	}
	
	public void queueKeyEvent(char key) {
		if(pressKey == 0)
			pressKey = key;
	}
	
	public void switchScene(Scene scene) {
		Scene fromScene = currentScene;
		currentScene = scene;	
		
		if(fromScene != null)
			fromScene.hide();
		animations.clear();
		
		currentScene.show();
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
	
	public LinkedList<Animation> getAnimations() {
		return animations;
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
			if(key > 0) {
				engine.currentScene.press(key);
			}
		}
	}
}
