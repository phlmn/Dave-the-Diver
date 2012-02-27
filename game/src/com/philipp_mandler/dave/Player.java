package com.philipp_mandler.dave;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Player implements IGameObject {
	private float dt;
	private Animation anim_up, anim_up_right, anim_up_left, anim_down, anim_down_right, anim_down_left, anim_right, anim_left;
	private World world;
	private Body body;
	private Direction walkDirection = Direction.Up;
	private Direction shootDirection = Direction.Up;
	private boolean walk = false;
	private boolean shoot = false;
	private float shootDelay = 0.0f;
	private List<Bullet> bullets = new ArrayList<Bullet>();
	
	public Player(float x, float y, World world) {
		this.world = world;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.linearDamping = 2f;
		bodyDef.fixedRotation = true;
		bodyDef.position = new Vec2(x/64f, y/64f);
		bodyDef.type = BodyType.DYNAMIC;
		
		body = world.createBody(bodyDef);
		FixtureDef fixDef = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.m_radius = 30f/64f;
		fixDef.shape = circle;
		fixDef.density = 0.5f;
		fixDef.friction = 0.3f;
		body.createFixture(fixDef);
		
		anim_up = new Animation(Art.playerAnimation_up, 4, 128, 128);
		anim_up.setFPS(10);
		anim_up_right = new Animation(Art.playerAnimation_up_right, 4, 128, 128);
		anim_up_right.setFPS(10);
	}
	
	public void tick(float dt) {
		this.dt = dt;
		
		for(Bullet b: bullets) {
			b.tick(dt);
		}
		if(shootDelay > 0.0f) shootDelay -= dt;
		
		input();
		
		if(walk) {
			anim_up.tick(dt);
			anim_up_right.tick(dt);
			
			switch(walkDirection) {
			case Up: body.applyForce(new Vec2(0, -2), new Vec2());break;
			case Down: body.applyForce(new Vec2(0, 2), new Vec2());break;
			case Right: body.applyForce(new Vec2(2, 0), new Vec2());break;
			case Left: body.applyForce(new Vec2(-2, 0), new Vec2());break;
			case UpRight: body.applyForce(new Vec2(1.414f, -1.414f), new Vec2());break;
			case UpLeft: body.applyForce(new Vec2(-1.414f, -1.414f), new Vec2());break;
			case DownRight: body.applyForce(new Vec2(1.414f, 1.414f), new Vec2()); break;
			case DownLeft: body.applyForce(new Vec2(-1.414f, 1.414f), new Vec2());break;
			}
		}
		
		if(shoot) {
			if(shootDelay <= 0.0f) {
				bullets.add(new Bullet(body, world, shootDirection, body.getPosition().x * 64f, body.getPosition().y * 64f));
				shootDelay = 0.2f;
			}
		}
	}

	private void input() {
		/*
		 * Walking
		 */
		if(Keyboard.isKeyDown(Key.right)) walkDirection = Direction.Right;
		if(Keyboard.isKeyDown(Key.left)) walkDirection = Direction.Left;
		if(Keyboard.isKeyDown(Key.down)) {
			walkDirection = Direction.Down;
			if(Keyboard.isKeyDown(Key.left)) walkDirection = Direction.DownLeft;
			if(Keyboard.isKeyDown(Key.right)) walkDirection = Direction.DownRight;			
		}
		if(Keyboard.isKeyDown(Key.up)) {
			walkDirection = Direction.Up;
			if(Keyboard.isKeyDown(Key.left)) walkDirection = Direction.UpLeft;
			if(Keyboard.isKeyDown(Key.right)) walkDirection = Direction.UpRight;			
		}
		
		if(Keyboard.isKeyDown(Key.down) || Keyboard.isKeyDown(Key.up) || Keyboard.isKeyDown(Key.left) || Keyboard.isKeyDown(Key.right)) {
			walk = true;
		}
		else {
			walk = false;
		}
		
		/*
		 * Shooting
		 */		
		if(Keyboard.isKeyDown(Key.shoot_right)) shootDirection = Direction.Right;
		if(Keyboard.isKeyDown(Key.shoot_left)) shootDirection = Direction.Left;
		if(Keyboard.isKeyDown(Key.shoot_down)) {
			shootDirection = Direction.Down;
			if(Keyboard.isKeyDown(Key.shoot_left)) shootDirection = Direction.DownLeft;
			if(Keyboard.isKeyDown(Key.shoot_right)) shootDirection = Direction.DownRight;			
		}
		if(Keyboard.isKeyDown(Key.shoot_up)) {
			shootDirection = Direction.Up;
			if(Keyboard.isKeyDown(Key.shoot_left)) shootDirection = Direction.UpLeft;
			if(Keyboard.isKeyDown(Key.shoot_right)) shootDirection = Direction.UpRight;			
		}
		
		if(Keyboard.isKeyDown(Key.shoot_down) || Keyboard.isKeyDown(Key.shoot_up) || Keyboard.isKeyDown(Key.shoot_left) || Keyboard.isKeyDown(Key.shoot_right)) {
			shoot = true;
		}
		else {
			shoot = false;
		}
		
	}
	
	public void render(float dt) {
		
		for(Bullet b: bullets) {
			b.render(dt);
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(body.getPosition().x * 64f - 64f, body.getPosition().y * 64f - 64f, 0);
		
		Direction direction;
		if(shoot) {
			direction = shootDirection;
		}
		else {
			direction = walkDirection;
		}
		switch(direction) {
		case Up: anim_up.render(); break;
//		case Down: anim_down.render(); break;
//		case Right: anim_right.render(); break;
//		case Left: anim_left.render(); break;
		case UpRight: anim_up_right.render(); break;
//		case UpLeft: anim_up_left.render(); break;
//		case DownRight: anim_down_right.render(); break;
//		case DownLeft: anim_down_left.render(); break;
		default: anim_up.render(); break;
		}
		GL11.glPopMatrix();
	}

	public Vec2 getPosition() {
		return new Vec2(body.getPosition().x * 64f, body.getPosition().y * 64f);
	}
}
