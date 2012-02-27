package com.philipp_mandler.dave;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.MathUtils;
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
	Animation shadow;
	
	public Bullet(Body shooter, World world, Direction flyDirection, float posX, float posY) {
		texture = new Animation(Art.fishTexture, 1);
		shadow = new Animation(Art.fish_shadow, 1);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.angularDamping = 0.3f;
		
		switch(flyDirection) {
		case Up: bodyDef.position = new Vec2((posX + 12)/64f, (posY - 30)/64f); bodyDef.linearVelocity = new Vec2(0, -10); break;
		case UpRight: bodyDef.position = new Vec2((posX + 30)/64f, (posY - 20)/64f); bodyDef.linearVelocity = new Vec2(10, -10); bodyDef.angle = 45 * MathUtils.DEG2RAD; break;
		case Right: bodyDef.position = new Vec2((posX + 25)/64f, (posY + 10)/64f); bodyDef.linearVelocity = new Vec2(10, 0); bodyDef.angle = 90 * MathUtils.DEG2RAD; break;
		case DownRight: bodyDef.position = new Vec2((posX + 20)/64f, (posY + 30)/64f); bodyDef.linearVelocity = new Vec2(7.071f, 7.071f); bodyDef.angle = 135 * MathUtils.DEG2RAD; break;
		case Down: bodyDef.position = new Vec2((posX - 10)/64f, (posY + 25)/64f); bodyDef.linearVelocity = new Vec2(0, 7.071f); bodyDef.angle = 180 * MathUtils.DEG2RAD; break;
		case DownLeft: bodyDef.position = new Vec2((posX - 30)/64f, (posY + 20)/64f); bodyDef.linearVelocity = new Vec2(-7.071f, 7.071f); bodyDef.angle = 225 * MathUtils.DEG2RAD; break;
		case Left: bodyDef.position = new Vec2((posX - 25)/64f, (posY - 10)/64f); bodyDef.linearVelocity = new Vec2(-7.071f, 0); bodyDef.angle = 270 * MathUtils.DEG2RAD; break;
		case UpLeft: bodyDef.position = new Vec2((posX - 25)/64f, (posY - 20)/64f); bodyDef.linearVelocity = new Vec2(-7.071f, -7.071f); bodyDef.angle = 315 * MathUtils.DEG2RAD; break;
		}
		
		if(shooter != null) {
			bodyDef.linearVelocity = new Vec2(bodyDef.linearVelocity.x + shooter.getLinearVelocity().x, bodyDef.linearVelocity.y + shooter.getLinearVelocity().y);
		}
		
		body = world.createBody(bodyDef);
		FixtureDef fixDef = new FixtureDef();
		fixDef.density = 1f;
		fixDef.friction = 0.2f;
		CircleShape shape = new CircleShape();
		shape.m_radius = 10/64f; 
		fixDef.shape = shape;
		body.createFixture(fixDef);
	}

	public void tick(float dt) {
		
		
	}

	public void render(float dt) {
		GL11.glPushMatrix();
		GL11.glTranslatef(body.getPosition().x * 64f, body.getPosition().y * 64f, 0);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(5, 5, 0);
		GL11.glRotatef(body.getAngle() * MathUtils.RAD2DEG, 0, 0, 1);
		GL11.glTranslatef(-16f, -16f, 0);
		shadow.render();
		GL11.glPopMatrix();
		
		GL11.glRotatef(body.getAngle() * MathUtils.RAD2DEG, 0, 0, 1);
		GL11.glTranslatef(-16f, -16f, 0);		
		texture.render();
		GL11.glPopMatrix();		
	}
	
	
}
