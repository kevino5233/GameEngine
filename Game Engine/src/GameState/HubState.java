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
	
	private TextEventListener textEventListener;
	
	private LevelMakerData lvmk;
	
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
			System.out.println("Couldn't find something");
		} catch (Exception e){
			System.out.println("Some other error");
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		player.spawn();
		ArrayList<String> textEvents = lvmk.getEvents();
		for (String s : textEvents){
			textEventListener.add(s);
		}
	}

	@Override
	public void update() {
		if (textEventListener != null){
			if (!textEventListener.isPlaying()){
				textEventListener.playMessage(
						player.getX() / TileMap.tileSize, 
						player.getY() / TileMap.tileSize
						);
			} else {
				textEventListener.update();
			}	
		}
		if (tileMap != null && player != null && !textEventListener.isPlaying()){
			player.update();
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
			if (tileMap != null && player != null && textEventListener != null){
				tileMap.draw(g);
				player.draw(g);
				if (textEventListener.isPlaying()){
					textEventListener.draw(g);
				}
			}
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
		textEventListener.keyPressed(k);
	}

	@Override
	public void keyReleased(int k) {
		player.keyReleased(k);
	}
	
}
