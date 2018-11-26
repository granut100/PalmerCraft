package game.entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.map.TileMap;

public abstract class Entity {
	
	Image image;
	
	ArrayList<Entity> objects;
	
	String id;
	
	public abstract void render(Graphics g, GameContainer gc) throws SlickException;
	
	public abstract void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity>objects) throws SlickException;
	
	public Image loadImage(String file) {
		try {
			image = new Image(file);
			image.setFilter(Image.FILTER_NEAREST);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	public Image getSprite(Image image,int x, int y, int tileSize) {
		image.setFilter(Image.FILTER_NEAREST);
		return image.getSubImage(x*tileSize, y*tileSize, tileSize, tileSize);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
