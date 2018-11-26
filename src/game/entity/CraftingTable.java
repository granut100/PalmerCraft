package game.entity;

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

public class CraftingTable extends NPC {
	
	boolean craftObs;
	int blocks = 0;
	int obs = 0;
	boolean disable;
	Image ct;
	boolean diamondPick;
	Sound talking;
	
	public CraftingTable(int x, int y, int width, int height, String id, String lines, int numLines) {
		super(x, y, width, height, id, lines, numLines);
		hey = image.getSubImage(0, 48, 16, 16);
		ct = image.getSubImage(32, 64, 32, 32);
		talking = new Sound("res/talk2.wav");
	}
	
	@Override
	public void render(Graphics g, GameContainer gc) throws SlickException {
		g.drawImage(ct,x,y,null);
		if(pointer) {
			g.drawImage(hey,x+width/4,y-height/2-8);
		}
		if(isTalking) {
			if(s == 0) {
				talking.play();
				talking.loop();
			}
			g.pushTransform();
			g.resetTransform();
			g.setColor(Color.black);
			g.fillRect(0,Game.HEIGHT-Game.HEIGHT/4, Game.WIDTH, Game.HEIGHT/4);
			g.setColor(Color.white);
			for(int i =0;i < dialogue.length;i++) {
				char arr[] = dialogue[i].toCharArray();
				if(i == counter && s < arr.length) {
					startTime = System.currentTimeMillis();
					b = b+arr[s];
					s++;
				}
				if(i == counter)g.drawString(b, 168, Game.HEIGHT-128);
				if(s >= arr.length && i == counter) {
					System.out.println("Test");
					talking.stop();
				}
			}
			g.scale(4, 4);
			g.popTransform();
		}
	}
	
	public void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity> objects)
			throws SlickException {
		//Talking to Crafting Table
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
				//Crafting Guides
				if(isTalking) {
					for(int k = 0;k < o.getItems().size();k++) {
						if(o.getItems().get(k).id == "DIAMOND" && !craftObs && !disable) {
							blocks++;
							//k -= 1;
							if(blocks >= 3) {
								craftObs = true;
							}
							System.out.println("huh");
						}
						if(k == o.getItems().size()-1 && !craftObs) {
							System.out.println("really");
							disable = true;
						}
					}
					for(int k = 0;k < o.getItems().size();k++) {
						if(counter == 1 && craftObs && blocks > 0) {
							if(o.getItems().get(k).id == "DIAMOND") {
								o.getItems().remove(k);
								blocks -= 1;
							}
						}
						if(blocks == 0 && counter == 1 && craftObs) {
							counter = -1;
							craftObs = false;
							isTalking = false;
							disable = false;
							talking.stop();
							//ActionObject block = new ActionObject("OBSIDIAN",-64,-64,16,16);
							//block.isTree = true;
							//o.getItems().add(block);
							diamondPick = true;
						}
						//System.out.println(blocks);
					}
				}
				if(!craftObs) {
					dialogue[0] = "Psh. Yeah. I am a crafting table.";
					dialogue[1] = "You have nothing to craft…";
				}
				if(craftObs) {
					dialogue[0] = "Press Y to craft Diamond Pickaxe, or Press N to leave...";
					dialogue[1] = "Crafted Diamond Pickaxe";
				}
					
		}
		if(objects.get(i).id == "PICKAXE") {
			ActionObject o = (ActionObject)objects.get(i);
			if(diamondPick)o.hasDiamondPick = true;
		}
		if(input.isKeyDown(Input.KEY_E) && startTime == 0 && pointer && !craftObs) {
			b = "";
			s = 0;
			counter++;
			isTalking = true;
			startTime = System.currentTimeMillis();
			if(counter >= dialogue.length) {
				isTalking = false;
				counter = -1;
				//Reset the block Counter!!
				disable = false;
				blocks = 0;
				talking.stop();
			}
		}
		
		if(input.isKeyDown(Input.KEY_Y) && startTime == 0 && pointer && craftObs && counter == 0) {
			b = "";
			s = 0;
			counter++;
			isTalking = true;
			startTime = System.currentTimeMillis();
		}
		
		if(input.isKeyDown(Input.KEY_N) && startTime == 0 && pointer && craftObs && counter == 0) {
			counter = -1;
			isTalking = false;
			craftObs = false;
			//Reset the block Counter!!
			disable = false;
			blocks = 0;
			talking.stop();
		}
		
	}
}

}
