package game.entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.map.TileMap;

public class Creeper extends Entity {

	int x,y;
	float dy,dx;
	int width,height;
	float speed;
	boolean goRight,goLeft,goDown,goUp;
	int health;
	boolean isHurt;
	
	Image creeper,hurt;
	
	public Creeper(int x,int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		id = "CREEPER";
		speed = 0.06f;
		health = 300;
		loadImage("res/spritesheet.png");
		creeper = image.getSubImage(64, 32, 32, 32);
		hurt = image.getSubImage(64+32, 32, 32, 32);
	}
	
	@Override
	public void render(Graphics g, GameContainer gc) throws SlickException {
		if(isHurt)g.drawImage(hurt,x,y);
		else g.drawImage(creeper,x,y);
	}

	@Override
	public void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity> objects)
			throws SlickException {
		x += dx;
		y += dy;
		int nx = x;
		int ny = y;
		boolean shouldMoveX = true;
		boolean shouldMoveY = true;
		
		//Movement code here
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).getId() == "PLAYER") {
				Player o = (Player)objects.get(i);
				if(x-64 < ((o.x)+32) && x + width + 45 > (o.x) &&
						y < ((o.y+45)+32) && y + height > o.y-64) {
					//System.out.println("In my space!!");
					//if(o.x > x)nx += 1;
					//if(o.x < x)nx -= 1;
					if(o.x > x) {
						nx += 1;
						goRight = true;
					}
					else if(o.x < x) {
						nx -= 1;
						goLeft = true;
					}
					if(o.y < y) {
						ny -= 1;
						goUp = true;
					}
					if(o.y > y) {
						ny += 1;
						goDown = true;
					} 
					if(o.y == y) {
						shouldMoveY = false;
					//	shouldMoveX = false;
					}
					if(o.x == x)shouldMoveX = false;
				}
				//Take away the player health if collision.
				if(x < ((o.x)+32) && x + width > (o.x) &&
						y < ((o.y)+32) && y + height > o.y) {
					o.health -= 1;
					o.isHurt = true;
				}
				else o.isHurt = false;
				if(health <= 0)o.isHurt = false;
			}
			if(objects.get(i).getId() == "PICKAXE") {
				ActionObject o = (ActionObject)objects.get(i);
				if(x < ((o.x)+o.width) && x + width > (o.x) &&
						y < ((o.y)+o.height) && y + height > o.y) {
						health -= 1;
						isHurt = true;
				} else {
					isHurt = false;
				}
			}
			if(objects.get(i).getId() == "CREEPER") {
				Creeper o = (Creeper)objects.get(i);
				if(health <= 0)objects.remove(i);
			}
		}
		
		for(int a = 0;a < map.getWidth();a++) {
			for(int b = 0;b < map.getHeight();b++) {
				//X Collision
				if(nx < ((a*32)+32) && nx + width > (a*32) &&
						y < ((b*32)+32) && y + height > b*32) {
					if(map.getMap()[a][b] == TileMap.BLOCKED) {
						shouldMoveX = false;
					}
				}
				
				//Y Collision
				if(x < ((a*32)+32) && x + width > (a*32) &&
						ny < ((b*32)+32) && ny + height > b*32) {
					if(map.getMap()[a][b] == TileMap.BLOCKED) {
						shouldMoveY = false;
						
					}
				}
			}
		}
		if(shouldMoveX == true && goRight && !goLeft) {
			dx = (speed*delta); 
			goRight = false;
		} else if(shouldMoveX == true && goLeft && !goRight) {
			dx = -((speed)*(delta/17));
			goLeft = false;
		} else {
			dx = 0;
			shouldMoveX = false;
			goLeft = false;
			goRight = false;
		}
		if(shouldMoveY == true && goDown && !goUp) {
			dy = (speed * delta);
			goDown = false;
		} else if(shouldMoveY == true && goUp && !goDown) {
			dy = -((speed)*(delta/17));
			goUp = false;
		} else {
			dy = 0;
			shouldMoveY = false;
			goUp = false;
			goDown = false;
		}
	}

}
