package com.philipp_mandler.dave;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;


public class Main {
	
	public boolean closeRequest = false;
	final String windowTitle = "Dave the Diver";
	long time = 0;
	long lastTime = 0;
	float dt = 0.0f;
	long tick = 0;
    Level level;
    Camera camera;
    Animation logo;
    int screenWidth = 1024;
    int screenHeight = 600;
    boolean fullscreen = false;
    Animation stone;
    Animation dave;
    float davePositionX = 0.0f, davePositionY = 0.0f;
    Random random = new Random();
    static public ArrayList<Integer> keyboardBuffer = new ArrayList<Integer>();
    
	public static void main(String[] args) {
		Main game = new Main();
		
		for(String arg : args) {
			if(arg.equals("fullscreen")) {
				game.fullscreen = true;
			}
		}
		
		game.run();
	}
	
	public void createWindow() {		
		try {
			
			if(fullscreen) {
				DisplayMode displayMode = new DisplayMode(Display.getDesktopDisplayMode().getWidth(), Display.getDesktopDisplayMode().getHeight());
				Display.setDisplayModeAndFullscreen(displayMode);
				
				screenHeight = Display.getDisplayMode().getHeight();
				screenWidth = Display.getDisplayMode().getWidth();
			}
			else {
				DisplayMode displayMode = new DisplayMode(screenWidth, screenHeight);
				Display.setDisplayMode(displayMode);
			}
			Display.setTitle(windowTitle);
			Display.create(new PixelFormat().withSamples(2));
		} 
		catch(LWJGLException e) {
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}		
	}
	
	public void initGL() {
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL11.glViewport(0, 0, screenWidth, screenHeight);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, screenWidth, screenHeight, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void render() {	
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		GL11.glLoadIdentity();
		
		if(level.loaded()){
			camera.render(dt);
			level.render(dt);
		}
		else {
			renderMenu();
		}
		
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Art.font.drawString(10f, 10f, "FPS: " + (int)(1.0f/dt));
		GL11.glPopMatrix();
	}
	
	private void renderMenu() {
		for(int x = 0; x < screenWidth/64 + 1; x++) {			
			for(int y = 0; y < screenHeight/64 + 1; y++) {
				GL11.glPushMatrix();
				GL11.glTranslatef(64f*x, 64f*y, 0);
				stone.render();
				GL11.glPopMatrix();
			}
		}
		GL11.glPushMatrix();
		GL11.glTranslatef(davePositionX - dave.width/2f, davePositionY - dave.height/2f, 0);
		dave.render();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		Art.font.drawString((screenWidth/2f) - 75, (screenHeight/2f) + 100, "Press return to start...");
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef((screenWidth/2f) - (logo.width/2f), (screenHeight/2f) - (logo.height/2f), 0);
		logo.render();
		GL11.glPopMatrix();
	}
	
	private void tickMenu() {
		dave.tick(dt);
		if(davePositionY < -(dave.height/2f)) {
			davePositionY = screenHeight + (dave.height/2f);
			davePositionX = random.nextInt(screenWidth);
		}
		else 
			davePositionY -= dt*30;
	}
	
	public void init() {
		logo = new Animation(Art.titleLogo, 1);
		stone = new Animation(Art.stone, 1, 64, 64);
		dave = new Animation(Art.playerAnimation_up, 4, 128, 128);
		dave.setFPS(7);
		
		camera = new Camera(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, 0);
		level = new Level(camera);
	}
	
	public void input() {		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				keyboardBuffer.add(Keyboard.getEventKey());
			}
		}
		
		for(int key : keyboardBuffer) {
			switch (key) {
			case Key.exit: if(!level.loaded()) closeRequest = true; break;
			case Key.select: if(!level.loaded()) level.start(); break;
			}
		}
		
		if (Display.isCloseRequested()) {
			closeRequest = true;
		}
	}
	
	public void tick() {
		time = Sys.getTime();
		dt = (time - lastTime)/1000.0f;
		lastTime = time;
		if(!level.loaded()) tickMenu();
		level.tick(dt);
		camera.lookAt(level.getPlayerPos().x, level.getPlayerPos().y);
		camera.tick(dt);
		System.out.println("Tick "+ tick+" took "+ dt +" seconds.");
		keyboardBuffer.clear();
		tick++;		
	}
	
	public void cleanup () {		
		Display.destroy();		
	}
	
	public void run() {		
		createWindow();
		initGL();
		init();
		while(!closeRequest) {
			input();
			tick();
			render();
			
			Display.update();
//			Display.sync(60);
		}
		
		cleanup();
	}
}