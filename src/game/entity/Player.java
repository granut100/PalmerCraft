package game.entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.animation.Animation;
import game.main.Game;
import game.map.TileMap;

public class Player extends Entity {

	int x,y;
	float dx,dy;
	int width,height;
	float speed;
	
	Animation standing;
	
	int health;
	boolean isHurt;
	
	public Player(int x,int y ,int width,int height) {
		this.id = "PLAYER";
		loadImage("res/spritesheet.png");
		Image[] walkRight = {getSprite(image,0,0,32)};
		standing = new Animation(walkRight,10);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		speed = .1f;
		standing.start();
		health = 200;
	}
	
	@Override
	public void render(Graphics g,GameContainer gc) throws SlickException {
		g.setColor(Color.white);
		if(isHurt)g.setColor(Color.red);
		g.drawImage(standing.getSprite(),x,y,null);
	}

	@Override
	public void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity> objects) throws SlickException {
		
		standing.update();
		
		x += dx;
		y += dy;
		int nx = x;
		int ny = y;
		boolean shouldMoveX = true;
		boolean shouldMoveY = true;
		if(input.isKeyDown(Input.KEY_D)) {
			nx += 1;
		}
		else if(input.isKeyDown(Input.KEY_A)) {
			nx -= 1;
		}
		if(input.isKeyDown(Input.KEY_S)) {
			ny += 1;
		}
		else if(input.isKeyDown(Input.KEY_W)) {
			ny -= 1;
		}
		
		
		for(int a = 0;a < map.getWidth();a++) {
			for(int b = 0;b < map.getHeight();b++) {
				//X Collision
				if(nx < ((a*32)+32) && nx + width > (a*32) &&
						y < ((b*32)+32) && y + height > b*32) {
					if(map.getMap()[a][b] == TileMap.BLOCKED) {
						//System.out.println("Collision!");
						shouldMoveX = false;
						
					}
				}
				
				//Y Collision
				if(x < ((a*32)+32) && x + width > (a*32) &&
						ny < ((b*32)+32) && ny + height > b*32) {
					if(map.getMap()[a][b] == TileMap.BLOCKED) {
						//System.out.println("Collision!");
						shouldMoveY = false;
						
					}
				}
			}
		}
		if(shouldMoveX == true && input.isKeyDown(Input.KEY_D)) {
			dx = (speed*delta); 
		} else if(shouldMoveX == true && input.isKeyDown(Input.KEY_A)) {
			dx = -(speed*(delta/2));
		} else {
			dx = 0;
		}
		if(shouldMoveY == true && input.isKeyDown(Input.KEY_S)) {
			dy = (speed * delta);
		} else if(shouldMoveY == true && input.isKeyDown(Input.KEY_W)) {
			dy = -(speed*(delta/2));
		} else {
			dy = 0;
		}
			
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
