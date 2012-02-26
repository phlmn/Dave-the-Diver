package com.philipp_mandler.dave;

import java.awt.Color;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Art {

	static Texture playerAnimation_up = loadTexture("res/dave_up.png");
	static Texture playerAnimation_up_right = loadTexture("res/dave_up_right.png");
	static Texture fishTexture = loadTexture("res/fish.png");
	static Texture stone = loadTexture("res/stone.png");
	static Texture titleLogo = loadTexture("res/title.png");
	static Texture block_cookie = loadTexture("res/block_cookie.png");
	static Texture editorFrame = loadTexture("res/editor_frame.png");
	static Texture block_shadow = loadTexture("res/block_shadow.png");
	static Texture block_stone = loadTexture("res/block_stone.png");
	
	static UnicodeFont font = loadFont("res/default.ttf");

	
	static private Texture loadTexture(String path) {
		try {
			Texture texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
			System.out.println("Texture Loaded - Path: \""+path+"\" Size: "+texture.getTextureWidth()+" x "+texture.getTextureHeight());
			return texture;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	static private UnicodeFont loadFont(String path) {
		try {
			UnicodeFont font = new UnicodeFont(path, 14, false, false);
			font.addAsciiGlyphs();
			font.getEffects().add(new ColorEffect(Color.WHITE));
			try {
				font.loadGlyphs();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			return font;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
