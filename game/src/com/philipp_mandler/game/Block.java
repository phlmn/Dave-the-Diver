package com.philipp_mandler.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.opengl.GL11;

public class Block {
	private int type = BlockType.AIR;
	private Animation texture = null;
	private Animation shadow = null;
	private World world;
	private Body body;
	float posX = 0.0f, posY = 0.0f;
	
	public Block(World world, int type, float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
		this.world = world;
		setType(type);
	}
	
	public void setType(int type) {
		boolean standardBlock = false;
		
		if(type >= 0 && type < BlockType.count) {
			if(body != null){
				world.destroyBody(body);
				body = null;
			}
			
			this.type = type;
			
			switch(type) {
			case BlockType.AIR: texture = null; shadow = null; break;
			case BlockType.Cookie: texture = new Animation(Art.block_cookie, 1, 64, 64); standardBlock = true; break;
			case BlockType.Stone: texture = new Animation(Art.block_stone, 1, 64, 64); standardBlock = true; break;
			}		
			
			if(standardBlock) {				
				shadow = new Animation(Art.block_shadow, 1, 64, 64);
				
				BodyDef bodyDef = new BodyDef();
				bodyDef.position = new Vec2(posX + 0.5f, posY + 0.5f);
				body = world.createBody(bodyDef);
				FixtureDef fixDef = new FixtureDef();
				fixDef.friction = 0.8f;
				fixDef.restitution = 0.2f;
				PolygonShape shape = new PolygonShape();
				shape.setAsBox(0.5f, 0.5f);
				fixDef.shape = shape;
				body.createFixture(fixDef);
			}		
			
		}
	}
	
	public int getType() {
		return type;
	}
	
	public void render() {
		GL11.glPushMatrix();		
		GL11.glTranslatef(posX * 64f + 5f, posY * 64f + 5f, 0);
		if(shadow != null) shadow.render();
		GL11.glTranslatef(-5f, -5f, 0);
		if(texture != null) texture.render();						
		GL11.glPopMatrix();
	}
}
