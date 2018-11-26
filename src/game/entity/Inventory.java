package game.entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.main.Game;
import game.map.TileMap;

public class Inventory extends Entity {
	
	int x,y;

	boolean showInventory;
	long startTime;
	long elapsedSeconds;
	
	boolean removeBlock;
	/*
	 * I have no idea about the inventory. Might start from scratch and look online or figure it out
	 * later?
	 * 
	 * 1. Make a new "Item" or use "ActionObject"
	 * 2. make a new object and it to an array of that object
	 * 3. Then display the whole stack of objects. 
	 */
	private ArrayList<ActionObject> items = new ArrayList<>();
	int currSelection = 0;
	Image arrow;
	int waitTime = 2;
	
	int playerHealth;
	
	public Inventory() {
		id = "INV";
		x = Game.WIDTH/2-50;
		y = Game.HEIGHT/2-100;
		loadImage("res/spritesheet.png");
		arrow = image.getSubImage(16, 32, 16, 16);
	}
	
	@Override
	public void render(Graphics g, GameContainer gc) throws SlickException {
		if(showInventory) {
			g.setColor(Color.gray);
			g.fillRect(x, y, 100, 200);
			g.setColor(Color.black);
			for(int i = 0;i < getItems().size();i++) {
				g.drawString(getItems().get(i).id, x, y+15*(i));
			}
			//g.drawRect(x+80, y+currSelection*15,10,10);
			g.drawImage(arrow,x+80,y+currSelection*15,null);
		}
		g.setColor(Color.orange);
		g.drawString("Health: " + playerHealth, 10, 40);
	}

	@Override
	public void update(int delta, GameContainer gc, Input input, TileMap map, ArrayList<Entity> objects)
			throws SlickException {
		if(startTime != 0) {
			elapsedSeconds = (System.currentTimeMillis() - startTime)/100;
			if(elapsedSeconds > waitTime)startTime = 0;
		}
		//System.out.println(currSelection + " " + getItems().size());
		for(int j = 0;j < getItems().size();j++) {
			if(showInventory) {
				if(input.isKeyDown(Input.KEY_DOWN) && startTime == 0) {
					//System.out.println(getItems().size() + " curr" + currSelection);
					startTime = System.currentTimeMillis();
					if(currSelection >= getItems().size()-1)currSelection = 0;
					else currSelection += 1;
				}
				else if(input.isKeyDown(Input.KEY_UP) && startTime == 0) {
					startTime = System.currentTimeMillis();
					if(currSelection <= 0)currSelection = getItems().size()-1;
					else currSelection -= 1;
				}
			}
		}
		
		for(int i = 0; i < objects.size();i++) {
			if(input.isKeyDown(Input.KEY_E) && startTime == 0) {
				startTime = System.currentTimeMillis();
				if(!showInventory) {
					showInventory = true;
					break;
				}
				if(showInventory) {
					showInventory = false;
					break;
				}
			}
			if(objects.get(i).getId() == "PICKAXE") {
				ActionObject o = (ActionObject)objects.get(i);
				if(o.addBlock) {
					if(!o.isTree && !o.isDiamond) {
						ActionObject block = new ActionObject("BLOCK",-64,-64,16,16);
						getItems().add(block);
						o.addBlock = false;
					}
					if(o.isTree && !o.isDiamond) {
						ActionObject block = new ActionObject("OBSIDIAN",-64,-64,16,16);
						getItems().add(block);
						o.addBlock = false;
						o.isTree = false;
					}
					if(!o.isTree && o.isDiamond) {
						ActionObject block = new ActionObject("DIAMOND",-64,-64,16,16);
						getItems().add(block);
						o.addBlock = false;
						o.isDiamond = false;
					}
				}
				//If item pickaxe is selected then you can use it.
				for(int k = 0;k < items.size();k++) {
					if(k == currSelection && items.get(k).id == "PICKAXE") {
						o.isOnItem = true;
						break;
					} else {
						o.isOnItem = false;
					}
					if(k == currSelection && items.get(k).id == "HOE") {
						o.isOnTool = true;
						break;
					} else {
						o.isOnTool = false;
					}
				}
			}
			
			if(objects.get(i).getId() == "PLAYER") {
				Player o = (Player)objects.get(i);
				if(showInventory) {
					o.dx = 0;
					o.dy = 0;
				}
				if(o.health > 0) {
					playerHealth = o.health;
				} else {
					objects.remove(i);
				}
				for(int k = 0;k < items.size();k++) {
					//Turn Clear Tile into Blocked Tile
					if(k == currSelection && !showInventory) {
						if(items.get(k).id == "BLOCK" || items.get(k).id == "OBSIDIAN" || items.get(k).id == "DIAMOND") { 
						for(int a = 0;a < map.getWidth();a++) {
							for(int b = 0;b < map.getHeight();b++) {
								if(input.isKeyDown(Input.KEY_RIGHT) && map.getMap()[(o.getX()+o.getWidth()+31)/32][o.getY()/32] != TileMap.BLOCKED && !removeBlock ) {
									if(!(items.get(k).id == "OBSIDIAN") && !(items.get(k).id == "DIAMOND")) {
										map.getMap()[(o.getX()+o.getWidth()+31)/32][o.getY()/32] = TileMap.BLOCKED;
										removeBlock = true;
									}
									else if(items.get(k).id == "OBSIDIAN" && items.get(k).id != "DIAMOND") {
										map.getTMap()[(o.getX()+o.getWidth()+31)/32][o.getY()/32] = TileMap.OBSIDIAN;
										map.getMap()[(o.getX()+o.getWidth()+31)/32][o.getY()/32] = TileMap.BLOCKED;
										removeBlock = true;
									}
									else if(items.get(k).id != "OBSIDIAN" && items.get(k).id == "DIAMOND") {
										map.getDMap()[(o.getX()+o.getWidth()+31)/32][o.getY()/32] = TileMap.DIAMOND;
										map.getMap()[(o.getX()+o.getWidth()+31)/32][o.getY()/32] = TileMap.BLOCKED;
										removeBlock = true;
									}
								}
								
								if(input.isKeyDown(Input.KEY_LEFT) && map.getMap()[(o.getX()-31)/32][o.getY()/32] != TileMap.BLOCKED && !removeBlock ) {
									
									if(!(items.get(k).id == "OBSIDIAN") && !(items.get(k).id == "DIAMOND")) {
										map.getMap()[(o.getX()-31)/32][o.getY()/32] = TileMap.BLOCKED;
										removeBlock = true;
									} else if(items.get(k).id == "OBSIDIAN" && items.get(k).id != "DIAMOND") {
										map.getTMap()[(o.getX()-31)/32][o.getY()/32] = TileMap.OBSIDIAN;
										map.getMap()[(o.getX()-31)/32][o.getY()/32]= TileMap.BLOCKED;
										//Do this for the rest of them
										removeBlock = true;
									} else if(items.get(k).id != "OBSIDIAN" && items.get(k).id == "DIAMOND") {
										map.getDMap()[(o.getX()-31)/32][o.getY()/32] = TileMap.DIAMOND;
										map.getMap()[(o.getX()-31)/32][o.getY()/32]= TileMap.BLOCKED;
										//Do this for the rest of them
										removeBlock = true;
									}
								}
								
								if(input.isKeyDown(Input.KEY_DOWN) && map.getMap()[o.getX()/32][(o.getY()+o.getHeight()+31)/32] != TileMap.BLOCKED  && !removeBlock ) {
									if(!(items.get(k).id == "OBSIDIAN") && !(items.get(k).id == "DIAMOND")) {
										map.getMap()[o.getX()/32][(o.getY()+o.getHeight()+31)/32] = TileMap.BLOCKED;
										removeBlock = true;
									}
									else if(items.get(k).id == "OBSIDIAN" && items.get(k).id != "DIAMOND") {
										map.getTMap()[o.getX()/32][(o.getY()+o.getHeight()+31)/32] = TileMap.OBSIDIAN;
										map.getMap()[o.getX()/32][(o.getY()+o.getHeight()+31)/32] = TileMap.BLOCKED;
										removeBlock = true;
									}
									else if(items.get(k).id != "OBSIDIAN" && items.get(k).id == "DIAMOND") {
										map.getDMap()[o.getX()/32][(o.getY()+o.getHeight()+31)/32] = TileMap.DIAMOND;
										map.getMap()[o.getX()/32][(o.getY()+o.getHeight()+31)/32] = TileMap.BLOCKED;
										removeBlock = true;
									}
								}
								
								if(input.isKeyDown(Input.KEY_UP) && map.getMap()[o.getX()/32][(o.getY()-31)/32] != TileMap.BLOCKED  && !removeBlock ) {
									if(!(items.get(k).id == "OBSIDIAN") && !(items.get(k).id == "DIAMOND")) {
										map.getMap()[o.getX()/32][(o.getY()-31)/32] = TileMap.BLOCKED;
										removeBlock = true;
										
									} else if(items.get(k).id == "OBSIDIAN" && items.get(k).id != "DIAMOND") {
										map.getTMap()[o.getX()/32][(o.getY()-31)/32] = TileMap.OBSIDIAN;
										map.getMap()[o.getX()/32][(o.getY()-31)/32] = TileMap.BLOCKED;
										removeBlock = true;
										System.out.println("TEST");
										//Idk if this does anything
										//Sometimes when crafting it comes out as a BLOCK with no mineTime?
										//Only doing it while placing it up?? not placing down right or left??
									} else if(items.get(k).id != "OBSIDIAN" && items.get(k).id == "DIAMOND") {
										map.getDMap()[o.getX()/32][(o.getY()-31)/32] = TileMap.DIAMOND;
										map.getMap()[o.getX()/32][(o.getY()-31)/32] = TileMap.BLOCKED;
										removeBlock = true;
										//return;
									}
								}
							}
						 }
					   }
					}
					if(removeBlock && startTime == 0 && !showInventory && k == currSelection) {
						removeBlock = false;
						if(items.get(k).id == "BLOCK" || items.get(k).id == "OBSIDIAN" || items.get(k).id == "DIAMOND")items.remove(k);
						if(items.size() > 1)currSelection = items.size()-1;
						else currSelection = 0;
						startTime = System.currentTimeMillis();
					}
				}
			}
		}
	}

	public ArrayList<ActionObject> getItems() {
		return items;
	}

	public void setItems(ArrayList<ActionObject> items) {
		this.items = items;
	}

}
