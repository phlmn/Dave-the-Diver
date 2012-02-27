package com.philipp_mandler.dave;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


public class Level {
	private World world;
	private Player player;
	private Block blocks[][] = new Block[64][64];
	private Animation ground = new Animation(Art.stone, 1, 64, 64);
	private Animation editorFrame = new Animation(Art.editorFrame, 1, 64, 64);
	private boolean loaded = false;
	private boolean editormode = false;
	private int selectedBlockX = 32;
	private int selectedBlockY = 32;
	private Camera camera;
	
	public Level(Camera camera) {
		this.camera = camera;
		world = new World(new Vec2(0, 0), true);		
		player = new Player(32*64f, 32*64f, world);
		for(int x = 0; x < 64; x++) {
			for(int y = 0; y < 64; y++) {
				blocks[x][y] = new Block(world, BlockType.AIR, x, y);
			}
		}
		
	}
	
	private void startEditor() {
		editormode = true;
	}
	
	public void start() {
		loaded = true;
	}
	
	public Vec2 getPlayerPos() {
		return player.getPosition();
	}
	
	public boolean loaded() {
		return loaded;
	}
	
	public void render(float dt) {
		int screenWidth = Display.getDisplayMode().getWidth();
		int screenHeight = Display.getDisplayMode().getHeight();
		
		for(int x = -1; x < screenWidth/64 + 2; x++) {			
			for(int y = -1; y < screenHeight/64 + 2; y++) {
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glTranslatef(64f*x - ((camera.getPos().x/64f - (int)(camera.getPos().x/64f))*64f), 64f*y - ((camera.getPos().y/64f - (int)(camera.getPos().y/64f))*64f), 0);
				ground.render();
				GL11.glPopMatrix();
			}
		}
		
		player.render(dt);
		
		GL11.glPushMatrix();
		for(int x = 0; x < 64; x++) {			
			for(int y = 0; y < 64; y++) {
				if(blocks[x][y] != null) blocks[x][y].render();
			}
		}
		GL11.glPopMatrix();
		
		if(editormode) {
			GL11.glPushMatrix();	
			GL11.glTranslatef(selectedBlockX *64f, selectedBlockY*64f, 0);
			editorFrame.render();
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			if(editormode) Art.font.drawString(10f, 110f, "Selected Block: " + selectedBlockX + ", " + selectedBlockY + "\nBlockType: " + blocks[selectedBlockX][selectedBlockY].getType());
			GL11.glPopMatrix();
		}
		
	}
	
	public void tick(float dt) {
		if(loaded) {
			for(int key : Main.keyboardBuffer) {
				switch (key) {
				case Key.leveleditor: startEditor(); break;
				case Key.block_right: if(editormode) if(selectedBlockX < 63) selectedBlockX++; break;
				case Key.block_left: if(editormode) if(selectedBlockX > 0) selectedBlockX--; break;
				case Key.block_up: if(editormode) if(selectedBlockY > 0) selectedBlockY--; break;
				case Key.block_down: if(editormode) if(selectedBlockY < 63) selectedBlockY++; break;
				case Key.menu: loaded = false; break;
				case Key.blocktype_last: if(editormode) blocks[selectedBlockX][selectedBlockY].setType(blocks[selectedBlockX][selectedBlockY].getType()-1); break;
				case Key.blocktype_next: if(editormode) blocks[selectedBlockX][selectedBlockY].setType(blocks[selectedBlockX][selectedBlockY].getType()+1); break;
				}
			}
						
			player.tick(dt);
			world.step(dt, 6, 2);
		}
		
	}
}
