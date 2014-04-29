package GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Player;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import TileMap.TileMap;

public class HubState extends GameState{
	
	private TileMap tileMap;
	
	private Player player;
	
	private LevelMakerData lvmk;
	
	private TextEventListener textEventListener;
	
	//something to keep track of the level ports/doors
	
	//potentially a SpawnManager for enemies
	
	public HubState(GameStateManager gsm){
		this.gsm = gsm;
		try{
			//tileMap = new TileMap("hub");
			BufferedImage bg = ImageIO.read(new File("./Resources/Backgrounds/hub.png"));
			lvmk = LevelMakerData.parse(new File("./Resources/Levels/hub.lvmk"));
			tileMap = new TileMap(bg, lvmk.getTileMap(), lvmk.getTileTypes(), lvmk.getEnemyData());
			player = new Player(tileMap, gsm.getFire(), gsm.getAir(), gsm.getDifficulty());
			textEventListener = new TextEventListener();
		} catch (IOException e){
			//couldn't find the level
		} catch (Exception e){
			//some other error
		}
	}

	@Override
	public void init() {
		try{
			player.spawn();
			ArrayList<String> textEvents = lvmk.getEvents();
			for (String s : textEvents){
				textEventListener.add(s);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (tileMap != null && player != null){
			player.update();
			System.out.println(tileMap.getPlayerSpawnX() +", " + tileMap.getPlayerSpawnY());
			tileMap.center(player.getX(), player.getY());
			tileMap.update(player);
		}
		
	}

	@Override
	public void draw(Graphics2D g) {

		try{
//			g.drawImage(bg.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0), 
//					0, 
//					0, 
//					null
//					);

			tileMap.draw(g);
			player.draw(g);
		} catch (NullPointerException e){
			System.out.println("Shit be null");
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(int k) {
		player.keyPressed(k);
	}

	@Override
	public void keyReleased(int k) {
		player.keyReleased(k);
	}
	
}
