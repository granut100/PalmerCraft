package game.entity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.animation.Sound;
import game.main.Game;
import game.map.TileMap;

public class NPC extends Entity {
	
	int width,height;
	int x,y;
	float dx,dy;
	float speed;
	
	boolean pointer;
	String[] dialogue;
	int numLines;
	boolean isTalking;
	int counter = -1;
	long startTime;
	long elapsedSeconds;
	Image sprite;
	Image closeup;
	Image hey;
	String b = "";
	int s;
	
	//Sound
	Sound talking;
	
	public NPC(int x,int y,int width,int height,String id,String lines,int numLines) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(lines)))) {
			 String curLine = null;
			 int i = 0;
			 dialogue = new String[numLines];
			 while((curLine = in.readLine()) != null) {
				 dialogue[i] = curLine;
				 System.out.println(dialogue[i]);
				 i++;
			 }
		 } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadImage("res/spritesheet.png");
		if(id == "NOTCH") {
			sprite = image.getSubImage(32, 32, 32, 32);
			closeup = image.getSubImage(32, 32, 32, 16);
		}
		hey = image.getSubImage(0, 48, 16, 16);
		
		talking = new Sound("res/talk2.wav");
	}

	@Override
	public void render(Graphics g, GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		//g.setColor(Color.blue);
		//g.drawRect(x, y, width, height);
		g.drawImage(sprite,x,y);
		if(pointer) {
			g.drawImage(hey,x+width/4,y-height/2-8);
		}
		if(isTalking) {
			if(s == 0) {
				talking.play();
				talking.loop();
			}
			//System.out.println(talking);
			g.pushTransform();
			g.resetTransform();
			g.setColor(Color.black);
			g.fillRect(0,Game.HEIGHT-Game.HEIGHT/4, Game.WIDTH, Game.HEIGHT/4);
			g.setColor(Color.white);
			for(int i =0;i < dialogue.length;i++) {
				char arr[] = dialogue[i].toCharArray();
				if(i == counter && s < arr.length) {
					b = b+arr[s];
					s++;
				}
				if(i == counter)g.drawString(b, 168, Game.HEIGHT-128);
				if(s >= arr.length && i == counter)talking.stop();
			}
			g.scale(4, 4);
			g.drawImage(closeup,10,115);
			g.popTransform();
		}
	}

	@Override
	public void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity> objects)
			throws SlickException {
		// TODO Auto-generated method stub
		x += dx;
		y += dy;
		int nx = x;
		int ny = y;
		boolean shouldMoveX = true;
		boolean shouldMoveY = true;
		
		//Movement code here
		
		
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
		if(shouldMoveX == true) {
			dx = (speed*delta); 
		} else if(shouldMoveX == true) {
			dx = -(speed*(delta/2));
		} else {
			dx = 0;
		}
		if(shouldMoveY == true) {
			dy = (speed * delta);
		} else if(shouldMoveY == true) {
			dy = -(speed*(delta/2));
		} else {
			dy = 0;
		}
		
		/*
		 * Talking NPC Code
		 */
		if(startTime != 0) {
			elapsedSeconds = (System.currentTimeMillis() - startTime)/100;
			if(elapsedSeconds > 2)startTime = 0;
		}
		
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).id == "PLAYER") {
				Player o = (Player)objects.get(i);
				if(x < (o.x+o.width) && x + width > o.x &&
						y < o.y+o.height && y + height > o.y) {
						pointer = true;
				} else {
					pointer = false;
				}
				
				if(isTalking) {
					o.dx = 0;
					o.dy = 0;
				}
			}
			if(objects.get(i).id == "INV") {
				Inventory o = (Inventory)objects.get(i);
				if(isTalking)o.showInventory = false;
			}
		}
		if(input.isKeyDown(Input.KEY_E) && startTime == 0 && pointer) {
			b = "";
			s = 0;
			counter++;
			isTalking = true;
			startTime = System.currentTimeMillis();
			if(counter >= dialogue.length) {
				isTalking = false;
				counter = -1;
			}
		}
		
		
			
	}
	
	

}
