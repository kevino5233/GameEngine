package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Goat;
import Entity.Player;
import Event.TextEvent;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import Main.GamePanel;
import TileMap.TileMap;



public class EarthState extends GameState{
	
	private TileMap tileMap;
	
	private Player player;
	
	private TextEventListener textEventListener;
	
	private LevelMakerData lvmk;
	
	private Goat goat1, goat2, goat3, goat4;
	
	private BufferedImage earthGod, bg, frame;
	private int godX, godY, godWidth, godHeight;
	
	private TextEvent earthGodThanks;
	
	//something to keep track of the level ports/doors
	
	//potentially a SpawnManager for enemies
	
	public EarthState(GameStateManager gsm){

		super(gsm);
		
		earthGodThanks = new TextEvent("Earth God", "You got the haunted goats! Thank goodness!");
		
		int numColsToDraw = GamePanel.WIDTH / TileMap.tileSize;
		int numRowsToDraw = GamePanel.HEIGHT / TileMap.tileSize;
		
		frame = new BufferedImage(((numColsToDraw + 1) * TileMap.tileSize) * GamePanel.SCALE,
				((numRowsToDraw + 1) * TileMap.tileSize) * GamePanel.SCALE,
				BufferedImage.TYPE_INT_RGB
				);
		
		try{
			bg = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Backgrounds/cave.png")
					);
			earthGod = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/earthGod.png")
					);
			lvmk = LevelMakerData.parse("cave");
			
			BufferedImage tilemap = ImageIO.read(
						this.getClass().getResourceAsStream("/Resources/Tilemaps/earth.png")
					);
			
			tileMap = new TileMap(tilemap, lvmk.getTileTypes(), lvmk.getEnemyData());
			player = new Player(tileMap, gsm.getDifficulty());
			textEventListener = new TextEventListener();
			goat1 = new Goat(tileMap, 3 * tileMap.getTileSize(), 27 * tileMap.getTileSize());
			goat2 = new Goat(tileMap, 7 * tileMap.getTileSize(), 26 * tileMap.getTileSize());
			goat3 = new Goat(tileMap, 30 * tileMap.getTileSize(), 4 * tileMap.getTileSize());
			goat4 = new Goat(tileMap, 23 * tileMap.getTileSize(), 8 * tileMap.getTileSize());
			
		} catch (IOException e){
			System.out.println("Couldn't find something");
		} catch (Exception e){
			System.out.println("Some other error");
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		
		goat1.spawn();
		goat2.spawn();
		goat3.spawn();
		goat4.spawn();
		player.spawn();
		
		tileMap.center(player.getX(), player.getY());
		tileMap.init();
		
		earthGodThanks.reset();
		
		godX = 27 * TileMap.tileSize;
		godY = 14 * TileMap.tileSize;
		godWidth = earthGod.getWidth();
		godHeight = earthGod.getHeight();
		
		ArrayList<String> textEvents = lvmk.getEvents();
		for (String s : textEvents){
			textEventListener.add(s);
		}
		
		playSound("cave");
		paused = false;
	}

	@Override
	public void update() {
		
		if (textEventListener != null){
			
			if (!textEventListener.isPlaying()){
				
				if (goat1.isDead() && 
					goat2.isDead() &&
					goat3.isDead() &&
					goat4.isDead()){
					if (earthGodThanks.isDone()){
						stopSound();
						gsm.complete(GameStateManager.EARTHSTATE);
						gsm.setState(GameStateManager.HUBSTATE);
					} else {
						textEventListener.playMessage(earthGodThanks);
					}
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
			tileMap.center(player.getX(), player.getY());
			tileMap.update(player);
			if (!goat1.isDead()){
				goat1.getNextPosition(player);
				goat1.update();
			}
			if (!goat2.isDead()){
				goat2.getNextPosition(player);
				goat2.update();
			}
			if (!goat3.isDead()){
				goat3.getNextPosition(player);
				goat3.update();
			}
			if (!goat4.isDead()){
				goat4.getNextPosition(player);
				goat4.update();
			}
		}
		
	}

	@Override
	public void draw(Graphics2D g) {

		try{
			if (paused){
				pauseMenu.draw(g);
			} else {

				if (tileMap != null && player != null){

					Graphics2D frameGraphics = (Graphics2D)frame.getGraphics();

					frameGraphics.drawImage(bg.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0),
							tileMap.getDrawX(), 
							tileMap.getDrawY(),
							null
							);

					player.draw(frameGraphics);
					tileMap.draw(frameGraphics);

					if (!goat1.isDead() &&
							goat1.getX() + goat1.getWidth() > tileMap.getX() &&
							goat1.getX() < tileMap.getX() + GamePanel.WIDTH && 
							goat1.getY() + goat1.getHeight() > tileMap.getY() && 
							goat1.getY() < tileMap.getY() + GamePanel.HEIGHT){
						goat1.draw(frameGraphics);
					}

					if (!goat2.isDead() &&
							goat2.getX() + goat2.getWidth() > tileMap.getX() &&
							goat2.getX() < tileMap.getX() + GamePanel.WIDTH && 
							goat2.getY() + goat2.getHeight() > tileMap.getY() && 
							goat2.getY() < tileMap.getY() + GamePanel.HEIGHT){
						goat2.draw(frameGraphics);
					}
					
					if (!goat3.isDead() &&
							goat3.getX() + goat3.getWidth() > tileMap.getX() &&
							goat3.getX() < tileMap.getX() + GamePanel.WIDTH && 
							goat3.getY() + goat3.getHeight() > tileMap.getY() && 
							goat3.getY() < tileMap.getY() + GamePanel.HEIGHT){
						goat3.draw(frameGraphics);
					}
					
					if (!goat4.isDead() &&
							goat4.getX() + goat4.getWidth() > tileMap.getX() &&
							goat4.getX() < tileMap.getX() + GamePanel.WIDTH && 
							goat4.getY() + goat4.getHeight() > tileMap.getY() && 
							goat4.getY() < tileMap.getY() + GamePanel.HEIGHT){
						goat4.draw(frameGraphics);
					}
					
					g.drawImage(frame.getSubimage(tileMap.getDrawX(), tileMap.getDrawY(), GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE),
							0,
							0,
							null
							);


					if (godX + godWidth > tileMap.getX() &&
							godX < tileMap.getX() + GamePanel.WIDTH && 
							godY + godHeight > tileMap.getY() && 
							godY < tileMap.getY() + GamePanel.HEIGHT){
						g.drawImage(
								earthGod.getScaledInstance
								(godWidth * GamePanel.SCALE, godHeight * GamePanel.SCALE, 0),
								(godX - tileMap.getX()) * GamePanel.SCALE,
								(godY - tileMap.getY()) * GamePanel.SCALE,
								null
								);
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

					if (textEventListener != null && textEventListener.isPlaying()){
						textEventListener.draw(g);
					}

				}


			}
			if (player.isDead()){
				g.setFont(new Font("Times New Roman", Font.BOLD, 30));
				g.setColor(Color.RED);
				g.drawString("You are dead", 50, 50);
				g.drawString("Press space to retry", 50, 100);
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

		if (paused){
			pauseMenu.keyPressed(k);
		} else if (k == KeyEvent.VK_SPACE){
			if (textEventListener.isPlaying()){
				textEventListener.keyPressed(k);
				
			}
			if (player.isDead()){
				stopSound();
				init();
			}
		} else if (k == KeyEvent.VK_ESCAPE){
			pauseMenu.init();
			paused = !paused;
		} else {
			player.keyPressed(k);
		}
	}

	@Override
	public void keyReleased(int k) {
		if (paused){
			pauseMenu.keyReleased(k);
		} else {
			player.keyReleased(k);
		}
	}
	
}
