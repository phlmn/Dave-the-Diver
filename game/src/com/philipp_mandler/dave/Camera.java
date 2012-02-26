package com.philipp_mandler.dave;

import org.jbox2d.common.Vec2;
import org.lwjgl.opengl.GL11;

public class Camera implements IGameObject{

	private float x = 0.0f;
	private float y = 0.0f;
	private int screenWidth;
	private int screenHeight;
	
	public Camera(int screenWidth, int screenHeight,float x, float y) {
		this.x = x;
		this.y = y;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	public void tick(float dt) {
		input();
	}
	
	private void input() {
		
	}
	
	public Vec2 getPos() {
		return new Vec2(x, y);
	}
	
	public void lookAt(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void render(float dt) {
		GL11.glTranslatef(-x + (screenWidth/2f), -y + (screenHeight/2), 0);
	}
}
