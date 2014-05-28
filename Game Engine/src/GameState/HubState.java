package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Player;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import Main.GamePanel;
import TileMap.TileMap;



public class HubState extends GameState{
	
	private TileMap tileMap;
	
	private Player player;
	
	private TextEventListener textEventListener;
	
	private BufferedImage bg;
	
	private Rectangle earth, wind, fire, air;
	
	private LevelMakerData lvmk;
	
	//something to keep track of the level ports/doors
	
	//potentially a SpawnManager for enemies
	
	public HubState(GameStateManager gsm){
		this.gsm = gsm;
		
		pauseMenu = new PauseState(this, gsm);
		pauseMenu.init();

		fire = new Rectangle(
				1 * TileMap.tileSize, 
				13 *TileMap.tileSize, 
				16, 
				16
				);
		
		earth = new Rectangle(
				5 * TileMap.tileSize,
				13 * TileMap.tileSize,
				16,
				16
				);
		
		try{
			//tileMap = new TileMap("hub");
			bg = ImageIO.read(
						this.getClass().getResourceAsStream("/Resources/Backgrounds/hub.png")
					);
			
			amulet = ImageIO.read(
						this.getClass().getResourceAsStream("/Resources/Sprites/Objects/Amulet/amulet.png")
					);
			firePendant = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/Amulet/fire.png")
			);
			airPendant = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/Amulet/air.png")
			);
			earthPendant = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/Amulet/earth.png")
			);
			waterPendant = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/Amulet/water.png")
			);
			
			lvmk = LevelMakerData.parse("hub");
			tileMap = new TileMap(lvmk.getTileMap(), lvmk.getTileTypes(), lvmk.getEnemyData());
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
		paused = false;
		
		if (!isPlaying()) playSound("theme");
		
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
		if (tileMap != null && player != null && !paused && !textEventListener.isPlaying()){
			if (!player.isDead()) player.update();
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
			if (paused){
				pauseMenu.draw(g);
			} else {
				if (tileMap != null && player != null){
					if (textEventListener != null && textEventListener.isPlaying()){
						textEventListener.draw(g);
					} else {
						
						g.drawImage(bg.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0),
								0, 
								0,
								null
								);
						player.draw(g);
						tileMap.draw(g);
					}
					
					g.setColor(Color.BLACK);
					g.setFont(new Font("Times New Roman", Font.BOLD, 10));
					g.drawString("Health", 60, 10);
					g.setColor(Color.RED);
					g.drawRect(75, 15, player.getMaxHealth() * 10, 10);
					g.fillRect(75, 15, player.getHealth() * 10, 10);

					g.drawImage(amulet, 0, 0, null);
					if (gsm.getFire()) g.drawImage(firePendant, 0, 0, null);
					if (gsm.getAir()) g.drawImage(airPendant, 0, 0, null);
					if (gsm.getWater()) g.drawImage(waterPendant, 0, 0, null);
					if (gsm.getEarth()) g.drawImage(earthPendant, 0, 0, null);
					
				}
			}
			if (player.isDead()){
				g.setFont(new Font("Times New Roman", Font.BOLD, 30));
				g.setColor(Color.RED);
				g.drawString("You are dead", 50, 50);
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
		if (k == KeyEvent.VK_SPACE){
			if(textEventListener.isPlaying()){
				textEventListener.keyPressed(k);
			} else if (fire.intersects(
					new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
					)){
				stopSound();
				gsm.setState(GameStateManager.FIRESTATE);
			} else if (earth.intersects(
					new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight())
					)){
				stopSound();
				gsm.setState(GameStateManager.EARTHSTATE);
			}
		}
		if (paused){
			pauseMenu.keyPressed(k);
		}
		if (k == KeyEvent.VK_ESCAPE){
			pauseMenu.init();
			paused = !paused;
		}
		player.keyPressed(k);
	}

	@Override
	public void keyReleased(int k) {
		if (paused){
			pauseMenu.keyReleased(k);
		}
		player.keyReleased(k);
	}
	
}
