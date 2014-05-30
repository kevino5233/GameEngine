package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Fukushima;
import Entity.Player;
import Event.TextEvent;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import Main.GamePanel;
import TileMap.TileMap;

public class AirState extends GameState{
	
	private boolean getRadiated;

	private TileMap tileMap;
	
	private Player player;
	
	private Fukushima nuclearDisaster;
	
	private TextEvent airGodThanks;
	private TextEventListener textEventListener;
	
	private BufferedImage frame, bg;
	
	private LevelMakerData lvmk;
	
	public AirState(GameStateManager gsm) {
		super(gsm);
		
		int numColsToDraw = GamePanel.WIDTH / TileMap.tileSize;
		int numRowsToDraw = GamePanel.HEIGHT / TileMap.tileSize;
		
		frame = new BufferedImage(((numColsToDraw + 1) * TileMap.tileSize) * GamePanel.SCALE,
				((numRowsToDraw + 1) * TileMap.tileSize) * GamePanel.SCALE,
				BufferedImage.TYPE_INT_RGB
				);
		
		airGodThanks = new TextEvent(
				"Air God",
				"Thanks for the cleaning up all that toxic waste up here! I leave you with my blessing"
				);
		
		try{
			bg = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Backgrounds/air.png")
					);
			lvmk = LevelMakerData.parse("air");
			
			BufferedImage tilemap = ImageIO.read(
						this.getClass().getResourceAsStream("/Resources/Tilemaps/air.png")
					);
			
			tileMap = new TileMap(tilemap, lvmk.getTileTypes(), lvmk.getEnemyData());
			player = new Player(tileMap, gsm.getFire(), gsm.getAir(), gsm.getDifficulty());
			nuclearDisaster = new Fukushima(tileMap, 0, 0);
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
		nuclearDisaster.spawn();
		
		tileMap.center(player.getX(), player.getY());
		tileMap.init();
		
		getRadiated = false;
		
		ArrayList<String> textEvents = lvmk.getEvents();
		for (String s : textEvents){
			textEventListener.add(s);
		}
		
		playSound("air");
		paused = false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (textEventListener != null){
			
			if (!textEventListener.isPlaying()){
				
				if (nuclearDisaster.isDead()){
					if (airGodThanks.isDone()){
						gsm.complete(GameStateManager.AIRSTATE);
						gsm.setState(GameStateManager.HUBSTATE);
					} else {
						textEventListener.playMessage(
								player.getX() / TileMap.tileSize, 
								player.getY() / TileMap.tileSize
								);
					}
				}
				
				
			} else {
				textEventListener.update();
			}	
		}
		if (tileMap != null && player != null && !paused && !textEventListener.isPlaying()){
			if (!player.isDead()) player.update();
			if (getRadiated && !nuclearDisaster.isDead()) {
				nuclearDisaster.getNextPosition(player);
				nuclearDisaster.update();
			} else if (player.getX() <= GamePanel.HEIGHT){
				getRadiated = true;
			}
			tileMap.center(player.getX(), player.getY());
			tileMap.update(player);
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
					nuclearDisaster.draw(frameGraphics);

					g.drawImage(frame.getSubimage(tileMap.getDrawX(), tileMap.getDrawY(), GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE),
							0,
							0,
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
