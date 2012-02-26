package com.philipp_mandler.dave;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Animation {
	
	private int frame = 0, frames = 1, fps = 24;
	private double count = 0;
	private double last = 0;
	Texture texture;
	int width = 16, height = 16;
	
	public Animation(Texture texture, int frames) {
		this.frames = frames;
		this.texture = texture;
		width = texture.getTextureWidth()/frames;
		height = texture.getTextureHeight();
	}
	
	public Animation(Texture texture, int frames, int width, int height) {
		this.frames = frames;
		this.texture = texture;
		this.width = width;
		this.height = height;
	}
	
	public void nextFrame() {
		if(frame < frames-1)
			frame++;
		else
			frame = 0;
	}
	
	public void setFrame(int frame) {
		if(frame <= frames)
			this.frame = frame;
	}
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public int getFPS() {
		return fps;
	}
	
	public void tick(float dt) {
		count += dt;
		if(count - last >= 1.0f/fps) {
			nextFrame();
			last = count;
		}
	}
	
	public void render() {
		Color.white.bind();
		texture.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBegin(GL11.GL_QUADS);		
		GL11.glTexCoord2f(1.0f/frames * frame, 0);		
		GL11.glVertex2f(0, 0);		
		GL11.glTexCoord2f(1.0f/frames + (1.0f/frames * frame), 0);		
		GL11.glVertex2f(width, 0);		
		GL11.glTexCoord2f(1.0f/frames + (1.0f/frames * frame), 1);		
		GL11.glVertex2f(width, height);		
		GL11.glTexCoord2f(1.0f/frames * frame, 1);		
		GL11.glVertex2f(0, height);		
		GL11.glEnd();
	}
}
