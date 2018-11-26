package game.main;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import game.entity.ActionObject;
import game.entity.CraftingTable;
import game.entity.Creeper;
import game.entity.Entity;
import game.entity.Inventory;
import game.entity.NPC;
import game.entity.Player;
import game.map.TileMap;

public class Game extends BasicGame {
	
	Player player;
	TileMap map;
	ActionObject action;
	ActionObject action2;
	Inventory inv;
	NPC npc;
	Creeper creeper;
	CraftingTable ct;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	ArrayList<Entity> objects;
	
	public Game(String title) {
		super(title);
	}
	
	public static void main(String args[]) throws SlickException {
		AppGameContainer game = new AppGameContainer(new Game("Game"));
		game.setDisplayMode(WIDTH,HEIGHT,false);
		game.setTargetFrameRate(60);
		game.setVSync(true);
		game.start();
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.drawRect(WIDTH/2,0,1,HEIGHT);
		g.drawRect(0,HEIGHT/2,WIDTH,1);
		g.scale(4,4);
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).getId() == "PLAYER") {
				Player o = (Player)objects.get(i);
				g.translate((-o.getX()+(Game.WIDTH/8)-o.getWidth()/2), -o.getY()+((Game.HEIGHT/8)-o.getHeight()/2));
			}
		}
		map.render(g, gc);
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).getId() == "PLAYER")player.render(g, gc);
			if(objects.get(i).getId() == "PICKAXE")action.render(g, gc);
			if(objects.get(i).getId() == "CREEPER")creeper.render(g, gc);
			if(objects.get(i).getId() == "NOTCH")npc.render(g, gc);
			if(objects.get(i).getId() == "CT")ct.render(g, gc);
		}
		g.resetTransform();
		inv.render(g, gc);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		objects = new ArrayList<>();
		inv = new Inventory();
		objects.add(inv);
		creeper = new Creeper(400,100,32,32);
		objects.add(creeper);
		ct = new CraftingTable(96,96,32,32,"CT","/ct.txt",2);
		objects.add(ct);
		npc = new NPC(100,32,32,32,"NOTCH","/notch.txt",4);
		objects.add(npc);
		player = new Player(50,50,32,32);
		objects.add(player);
		action = new ActionObject("PICKAXE",-64,-64,16,16);
		objects.add(action);
		action2 = new ActionObject("HOE",-64,-64,16,16);
		objects.add(action2);
		//Add the pickaxe to inventory
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).getId() == "INV") {
				Inventory o = (Inventory)objects.get(i);
				o.getItems().add(action);
				o.getItems().add(action2);
			}
		}
		map = new TileMap();
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		map.update(delta, gc, input);
		for(int i = 0;i < objects.size();i++) {
			if(objects.get(i).getId() == "PLAYER")player.update(delta, gc, input, map, objects);
			if(objects.get(i).getId() == "PICKAXE")action.update(delta, gc, input, map, objects);
			if(objects.get(i).getId() == "NOTCH")npc.update(delta, gc, input, map, objects);
			if(objects.get(i).getId() == "CT")ct.update(delta, gc, input, map, objects);
			if(objects.get(i).getId() == "INV")inv.update(delta, gc, input, map, objects);
			if(objects.get(i).getId() == "CREEPER")creeper.update(delta, gc, input, map, objects);
		}
		if(input.isKeyDown(Input.KEY_ESCAPE)) {
			System.exit(0);
		}
	}
	
	

}
