package game.entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.animation.Animation;
import game.animation.Sound;
import game.map.TileMap;

public class ActionObject extends Entity {
	
	int x,y;
	int width,height;
	Animation nothing;
	
	long startTime;
	
	boolean addBlock;
	boolean disable;
	boolean isOnItem;
	boolean isTree;
	boolean isDiamond;
	
	long mineTimeStart;
	long mineTimeSeconds;
	long mineTimeFinish;
	
	boolean isOnTool;
	
	boolean hasDiamondPick;
	
	Image diamondPick;
	Sound mineDiamonds;
	Sound takeLong;
	
	public ActionObject(String id,int x,int y,int width,int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		loadImage("/res/spritesheet.png");
		Image[] picture = {getSprite(image,0,2,16)};
		nothing = new Animation(picture,10);
		diamondPick = image.getSubImage(16,48,16,16);
		mineTimeFinish = 10;
		mineDiamonds = new Sound("res/mineDiamonds.wav");
		takeLong = new Sound("res/takeLong.wav");
	}

	@Override
	public void render(Graphics g, GameContainer gc) throws SlickException {
		if(isOnItem)g.setColor(Color.orange);
		else if(isOnTool) {
			g.setColor(Color.red);
			g.drawImage(nothing.getSprite(),x,y,null);
		}
		if(!hasDiamondPick)g.drawImage(nothing.getSprite(),x,y,null);
		else if(hasDiamondPick && isOnItem) {
			g.setColor(Color.white);
			g.drawImage(diamondPick,x,y,null);
		}
		//g.drawRect(x, y, width, height); TEST
	}

	@Override
	public void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity> objects) throws SlickException {
		//Mine time
		if(mineTimeStart != 0) {
			if(mineTimeSeconds > mineTimeFinish) {
				mineDiamonds.stop();
				mineDiamonds.startClipOver();
				takeLong.stop();
				takeLong.startClipOver();
				mineTimeStart = 0;
			}
		}
		//make sure the time resets before mining next block 
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).getId() == "PLAYER" && !disable && isOnItem) {
				Player o = (Player)objects.get(i);
				if(input.isKeyDown(Input.KEY_RIGHT)) {
					startTime = System.currentTimeMillis();
					if(mineTimeStart == 0 && mineTimeSeconds != 0)mineTimeStart = System.currentTimeMillis();
					x = o.x+o.width;
					y = o.y+(o.height/4);
				}
				else if(input.isKeyDown(Input.KEY_LEFT)) {
					startTime = System.currentTimeMillis();
					if(mineTimeStart == 0 && mineTimeSeconds != 0)mineTimeStart = System.currentTimeMillis();
					x = o.x-o.width/2;
					y = o.y+(o.height/4);
				}
				else if(input.isKeyDown(Input.KEY_DOWN)) {
					startTime = System.currentTimeMillis();
					if(mineTimeStart == 0 && mineTimeSeconds != 0)mineTimeStart = System.currentTimeMillis();
					x = o.x+(o.width/4);
					y = o.y+(o.height);
				}
				else if(input.isKeyDown(Input.KEY_UP)) {
					startTime = System.currentTimeMillis();
					if(mineTimeStart == 0 && mineTimeSeconds != 0)mineTimeStart = System.currentTimeMillis();
					x = o.x+(o.width/4);
					y = o.y-(o.height/2);
				} else {
					mineTimeStart = 0;
					mineTimeSeconds = 0;
					mineDiamonds.stop();
					mineDiamonds.startClipOver();
					takeLong.stop();
					takeLong.startClipOver();
				}
				//Take away block from Map
					for(int a = 0;a < map.getWidth();a++) {
						for(int b = 0;b < map.getHeight();b++) {
							if(x < ((a*32)+32) && x + width > (a*32) &&
									y < ((b*32)+32) && y + height > b*32) {
								if(map.getMap()[a][b] == TileMap.BLOCKED && map.getTMap()[a][b] != 2 && map.getBMap()[a][b] != 3 && map.getDMap()[a][b] != 4) {
									mineTimeSeconds = (System.currentTimeMillis() - mineTimeStart)/100;
									if(!hasDiamondPick)mineTimeFinish = 10;
									else if(hasDiamondPick)mineTimeFinish = 2;
									if(mineTimeSeconds > mineTimeFinish && mineTimeStart != 0) {
										map.getMap()[a][b] = TileMap.CAVE_GROUND;
										//id = "BLOCK";
										addBlock = true;
									}
								}
								if(map.getMap()[a][b] == TileMap.BLOCKED && map.getTMap()[a][b] == 2 && map.getBMap()[a][b] != 3  && map.getDMap()[a][b] != 4 && hasDiamondPick) {
									System.out.println(mineTimeSeconds);
									//Updates seconds while mining
									mineTimeSeconds = (System.currentTimeMillis() - mineTimeStart)/100;
									mineTimeFinish = 190; //300
									takeLong.play();
									if(mineTimeSeconds > mineTimeFinish && mineTimeStart != 0) {
										map.getMap()[a][b] = TileMap.CAVE_GROUND;
										map.getTMap()[a][b] = 0;
										isTree = true;
										addBlock = true;
										//mineTimeStart = 0;
									}
								} 
								if(map.getMap()[a][b] == TileMap.BLOCKED && map.getTMap()[a][b] != 2 && map.getBMap()[a][b] != 3  && map.getDMap()[a][b] == 4) {
									System.out.println(mineTimeSeconds);
									//Updates seconds while mining
									mineTimeSeconds = (System.currentTimeMillis() - mineTimeStart)/100;
									mineTimeFinish = 250; //250
									mineDiamonds.play();
									//mineDiamonds.loop();
									if(mineTimeSeconds > mineTimeFinish && mineTimeStart != 0) {
										map.getMap()[a][b] = TileMap.CAVE_GROUND;
										map.getTMap()[a][b] = 0;
										isDiamond = true;
										addBlock = true;
										//mineTimeStart = 0;
									}
								}
								if(map.getMap()[a][b] == TileMap.BLOCKED && map.getTMap()[a][b] != 2 && map.getBMap()[a][b] == 3  && map.getDMap()[a][b] != 4) {
									mineTimeSeconds = 0;
									mineTimeStart = 0;
								}  
						 }
					 }
				  }
		   }
			if(objects.get(i).getId() == "PLAYER" && !disable && isOnTool && !isOnItem) {
				Player o = (Player)objects.get(i);
				if(input.isKeyDown(Input.KEY_RIGHT)) {
					startTime = System.currentTimeMillis();
					x = o.x+o.width;
					y = o.y+(o.height/4);
					if(map.getMap()[(o.getX()+o.getWidth()+10)/32][o.getY()/32] == TileMap.CAVE_GROUND) {
						map.getMap()[(o.getX()+o.getWidth()+10)/32][o.getY()/32] = TileMap.CLEAR;
					}
				}
				else if(input.isKeyDown(Input.KEY_LEFT)) {
					startTime = System.currentTimeMillis();
					x = o.x-o.width/2;
					y = o.y+(o.height/4);
					if(map.getMap()[(o.getX()-32)/32][o.getY()/32] == TileMap.CAVE_GROUND)
						map.getMap()[(o.getX()-32)/32][o.getY()/32] = TileMap.CLEAR;
				}
				else if(input.isKeyDown(Input.KEY_DOWN)) {
					startTime = System.currentTimeMillis();
					x = o.x+(o.width/4);
					y = o.y+(o.height);
					if(map.getMap()[o.getX()/32][(o.getY()+o.getHeight()+10)/32] == TileMap.CAVE_GROUND)
						map.getMap()[o.getX()/32][(o.getY()+o.getHeight()+10)/32] = TileMap.CLEAR;
				}
				else if(input.isKeyDown(Input.KEY_UP)) {
					startTime = System.currentTimeMillis();
					x = o.x+(o.width/4);
					y = o.y-(o.height/2);
					if(map.getMap()[o.getX()/32][(o.getY()-10)/32] == TileMap.CAVE_GROUND)
						map.getMap()[o.getX()/32][(o.getY()-10)/32] = TileMap.CLEAR;
				}
			}
		   if(objects.get(i).getId() == "INV") {
			   Inventory o = (Inventory) objects.get(i);
			   if(o.showInventory)disable = true;
			   else if(!o.showInventory)disable = false;
			   //if(o.removeBlock)removeBlock = true;
			   //else if(o.removeBlock)removeBlock = false;
		   }
	    }
		if(x != -64 && y != -64) {
			long elapsedSeconds = (System.currentTimeMillis() - startTime)/10;
			if(elapsedSeconds > 1) {
				x = -64;
				y = -64;
			}
		}
		
		
     }
}
