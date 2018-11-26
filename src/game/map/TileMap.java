package game.map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.entity.Player;

public class TileMap {
	
	int width = 31, height = 10;
	
	int[][] map = new int[width][height];
	
	int blockSize = 32;
	
	public static final int BLOCKED = 1;
	public static final int CLEAR = 0;
	public static final int OBSIDIAN = 2;
	public static final int BEDROCK = 3;
	public static final int DIAMOND = 4;
	public static final int CAVE_GROUND = 5;
	
	Image image,ground,block,obsidian,bedrock,diamond,cave;
	
	int[][] tMap = new int[width][height];
	int[][] bMap = new int[width][height];
	int[][] dMap = new int[width][height];
	int tx,ty = 0;
	int t;
	int bx,by = 0;
	int b = 0;
	int dx,dy = 0;
	int d = 0;
	
	public TileMap() {
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/map.txt")))) {
			 String curLine = null;
			 int y = 0;
			 while((curLine = in.readLine()) != null) {
				System.out.println(curLine);
				for(int x = 0;x < width;x++) {
					if(curLine.substring(x,x+1).equals("1")) {
						//System.out.println("BLOCKED HERE");
						System.out.println(x + " " + y);
						map[x][y] = BLOCKED;
						
					}
					if(curLine.substring(x,x+1).equals("0")) {
						//System.out.println("CLEAR HERE");
						map[x][y] = CLEAR;
					}
					if(curLine.substring(x,x+1).equals("2")) {
						//System.out.println("BLOCKED HERE");
						//System.out.println(x + " " + y);
						map[x][y] = BLOCKED;
						tx = x;
						ty = y;
						tMap[tx][ty] = OBSIDIAN;
					}
					if(curLine.substring(x,x+1).equals("3")) {
						//System.out.println("BLOCKED HERE");
						//System.out.println(x + " " + y);
						map[x][y] = BLOCKED;
						bx = x;
						by = y;
						bMap[bx][by] = BEDROCK;
					}
					if(curLine.substring(x,x+1).equals("4")) {
						//System.out.println("BLOCKED HERE");
						//System.out.println(x + " " + y);
						map[x][y] = BLOCKED;
						dx = x;
						dy = y;
						dMap[dx][dy] = DIAMOND;
					}
					if(curLine.substring(x,x+1).equals("5")) {
						//System.out.println("CLEAR HERE");
						map[x][y] = CAVE_GROUND;
					}
					
					if(x+1 == width) {
						System.out.println("New Col");
						y += 1;
					}
				}
			 }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		loadImage("res/spritesheet.png");
		ground = image.getSubImage(32, 0, blockSize, blockSize);
		block = image.getSubImage(64, 0, blockSize, blockSize);
		obsidian = image.getSubImage(96, 0, blockSize, blockSize);
		bedrock = image.getSubImage(0, 64, blockSize, blockSize);
		diamond = image.getSubImage(64, 64, blockSize, blockSize);
		cave = image.getSubImage(96, 64, blockSize, blockSize);
	}

	public void render(Graphics g, GameContainer gc) throws SlickException {
		for(int x = 0;x < width;x++) {
			for(int y = 0;y < height;y++) {
				if(map[x][y] == BLOCKED) {
					if(bMap[x][y] == BEDROCK && tMap[x][y] != OBSIDIAN && dMap[x][y] != DIAMOND) {
						g.drawImage(bedrock,x*blockSize,y*blockSize,null);
					}
					else if(tMap[x][y] == OBSIDIAN && bMap[x][y] != BEDROCK && dMap[x][y] != DIAMOND) {
						g.drawImage(obsidian,x*blockSize,y*blockSize,null);
					}
					else if(dMap[x][y] == DIAMOND && tMap[x][y] != OBSIDIAN && bMap[x][y] != BEDROCK) {
						//g.setColor(Color.blue);
						//g.fillRect(x*blockSize, y*blockSize, blockSize, blockSize);
						g.drawImage(diamond,x*blockSize,y*blockSize,null);
					} else {
						g.setColor(Color.white);
						g.drawImage(block,x*blockSize,y*blockSize,null);
					}
				}
				else if(map[x][y] == CLEAR) {
					g.setColor(Color.white);
					g.drawImage(ground,x*blockSize,y*blockSize,null);
				}
				else if(map[x][y] == CAVE_GROUND) {
					//System.out.println("TEST");
					g.setColor(Color.white);
					g.drawImage(cave,x*blockSize,y*blockSize,null);
				}
			}
		}
	}

	public void update(int delta, GameContainer gc, Input input) throws SlickException {
		// TODO Auto-generated method stub
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

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}
	
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

	public int[][] getTMap() {
		return tMap;
	}

	public void setTMap(int[][] tMap) {
		this.tMap = tMap;
	}
	
	public int[][] getBMap() {
		return bMap;
	}

	public void setBMap(int[][] bMap) {
		this.bMap = bMap;
	}

	public int[][] getDMap() {
		return dMap;
	}

	public void setDMap(int[][] dMap) {
		this.dMap = dMap;
	}
	
	

}
