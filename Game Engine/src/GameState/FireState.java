package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Entity.Player;
import Event.TextEvent;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import Main.GamePanel;
import TileMap.TileMap;

public class FireState extends GameState{
	
	private ArrayList<Rectangle> coords;

	private TileMap tileMap;
	
	private Player player;
	
	private TextEventListener textEventListener;
	
	private LevelMakerData lvmk;
	
	private BufferedImage pot;
	
	private TextEvent fireGodThanks,
					  dropItLikeItsHot, 
					  weedItOut;
	
	//something to keep track of the level ports/doors
	
	//potentially a SpawnManager for enemies
	
	public FireState(GameStateManager gsm){
		this.gsm = gsm;
		
		pauseMenu = new PauseState(this, gsm);
		pauseMenu.init();
		
		fireGodThanks = new TextEvent(
				"Fire God", 
				"Man you're blazing it! Get back here with my plants and I'll reward you"
				);
		dropItLikeItsHot = new TextEvent(
					"Fire God",
					"There's one! Don't drop it, it's hot!"
				);
		weedItOut = new TextEvent(
				"Fire God",
				"You only need to weed out one more"
				);
		
		try{

			coords = new ArrayList<>();
			pot = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/pot.png")
					);
			BufferedImage bg = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Backgrounds/fire.png")
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
			lvmk = LevelMakerData.parse("fire");
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
		
		tileMap.init();
		
		coords.add(new Rectangle(17 * TileMap.tileSize, 11 * TileMap.tileSize, TileMap.tileSize, TileMap.tileSize));
		coords.add(new Rectangle(8 * TileMap.tileSize, 24 * TileMap.tileSize, TileMap.tileSize, TileMap.tileSize));
		coords.add(new Rectangle(58 * TileMap.tileSize, 25 * TileMap.tileSize, TileMap.tileSize, TileMap.tileSize));
		
		fireGodThanks.reset();
		dropItLikeItsHot.reset();
		weedItOut.reset();
		
		player.spawn();
		ArrayList<String> textEvents = lvmk.getEvents();
		for (String s : textEvents){
			textEventListener.add(s);
		}
		playSound("fire");
		paused = false;
		
		System.out.println("Done");
	}

	@Override
	public void update() {
		if (textEventListener != null){
			if (!textEventListener.isPlaying()){
				
				if (fireGodThanks.isDone()){
					stopSound();
					gsm.complete(GameStateManager.FIRESTATE);
					gsm.setState(GameStateManager.HUBSTATE);
					
				} else {
					textEventListener.playMessage(
							player.getX() / TileMap.tileSize, 
							player.getY() / TileMap.tileSize
							);
				 	
				}
				
				
			} else {
				textEventListener.update();
			}	
		}
		if (tileMap != null && player != null && !paused && !textEventListener.isPlaying()){
			if (!player.isDead()) player.update();
			tileMap.update(player);
		}
		for (int i = 0; i < coords.size(); i++){
			Rectangle rec = coords.get(i);
			if (rec.intersects(new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight()))){
				coords.remove(i);
				switch (coords.size()){
				case 0 :
					textEventListener.playMessage(fireGodThanks);
					break;
				case 1 : 
					textEventListener.playMessage(weedItOut);
					break; 
				case 2 : 
					textEventListener.playMessage(dropItLikeItsHot);
					break;
				}
			}
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
					tileMap.draw(g);
					player.draw(g);
					
					for (Rectangle rec : coords){
						if (rec.getX() + rec.getWidth() > tileMap.getX() &&
								rec.getX() < tileMap.getX() + GamePanel.WIDTH && 
								rec.getY() + rec.getHeight() > tileMap.getY() && 
								rec.getY() < tileMap.getY() + GamePanel.HEIGHT){
							g.drawImage(pot.getScaledInstance((int)rec.getWidth() * GamePanel.SCALE, (int)rec.getHeight() * GamePanel.SCALE, 0),
									(int)(rec.getX() - tileMap.getX()) * GamePanel.SCALE,
									(int)(rec.getY() - tileMap.getY()) * GamePanel.SCALE,
									null
								   );
								}
					}
					if (textEventListener != null && textEventListener.isPlaying()){
						textEventListener.draw(g);
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
					
					if (player.isDead()){
						g.setFont(new Font("Times New Roman", Font.BOLD, 30));
						g.setColor(Color.RED);
						g.drawString("You are dead", 50, 50);
						g.drawString("Press space to retry", 50, 100);
					}
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
		if (k == KeyEvent.VK_SPACE && textEventListener.isPlaying()){
			textEventListener.keyPressed(k);
		}
		if (paused){
			pauseMenu.keyPressed(k);
		}
		if (k == KeyEvent.VK_ESCAPE){
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
