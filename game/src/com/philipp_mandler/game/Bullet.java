package com.philipp_mandler.game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.opengl.GL11;

public class Bullet implements IGameObject {

	Direction flyDirection;
	boolean alive = true;
	Body body;
	Animation texture; 
	
	public Bullet(Body shooter, World world, Direction flyDirection, float posX, float posY) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = true;
		bodyDef.type = BodyType.DYNAMIC;
		texture = new Animation(Art.fishTexture, 8, 32, 32);
		
		switch(flyDirection) {
		case Up: texture.setFrame(0); bodyDef.position = new Vec2((posX + 12)/64f, (posY - 30)/64f); bodyDef.linearVelocity = new Vec2(0, -10); break;
		case UpRight: texture.setFrame(1); bodyDef.position = new Vec2((posX + 30)/64f, (posY - 20)/64f); bodyDef.linearVelocity = new Vec2(10, -10); break;
		case Right: texture.setFrame(2); bodyDef.position = new Vec2((posX + 25)/64f, (posY + 10)/64f); bodyDef.linearVelocity = new Vec2(10, 0); break;
		case DownRight: texture.setFrame(3); bodyDef.position = new Vec2((posX + 20)/64f, (posY + 30)/64f); bodyDef.linearVelocity = new Vec2(7.071f, 7.071f); break;
		case Down: texture.setFrame(4); bodyDef.position = new Vec2((posX - 10)/64f, (posY + 25)/64f); bodyDef.linearVelocity = new Vec2(0, 7.071f); break;
		case DownLeft: texture.setFrame(5); bodyDef.position = new Vec2((posX - 30)/64f, (posY + 20)/64f); bodyDef.linearVelocity = new Vec2(-7.071f, 7.071f); break;
		case Left: texture.setFrame(6); bodyDef.position = new Vec2((posX - 25)/64f, (posY - 10)/64f); bodyDef.linearVelocity = new Vec2(-7.071f, 0); break;
		case UpLeft: texture.setFrame(7); bodyDef.position = new Vec2((posX - 25)/64f, (posY - 20)/64f); bodyDef.linearVelocity = new Vec2(-7.071f, -7.071f); break;
		}
		
		if(shooter != null) {
			bodyDef.linearVelocity = new Vec2(bodyDef.linearVelocity.x + shooter.getLinearVelocity().x, bodyDef.linearVelocity.y + shooter.getLinearVelocity().y);
		}
		
		body = world.createBody(bodyDef);
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 1f;
		CircleShape shape = new CircleShape();
		shape.m_radius = 10/64f; 
		fixDef.shape = shape;
		body.createFixture(fixDef);
	}

	public void tick(float dt) {
		
		
	}

	public void render(float dt) {
		GL11.glPushMatrix();	
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTranslatef(body.getPosition().x * 64f - 16f, body.getPosition().y * 64f - 16f, 0);
		texture.render();		
		GL11.glPopMatrix();		
	}
	
	
}
