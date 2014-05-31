package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Player;
import Entity.Shark;
import Event.TextEvent;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import Main.GamePanel;
import TileMap.TileMap;

public class WaterState extends GameState{
	
	private static int boatHealth, boatMaxHealth;
	
	private int framesFreeze, framesFrozen, 
				mapX, bgX,
				mapDx, bgDx;
	
	private Player player;
	
	private Shark jaws;
	
	private Animation animatino;
	
	private TileMap tileMap;
	
	private TextEvent waterLevelStart,
					waterGodThanks;
	
	private TextEventListener textEventListener;
	
	private LevelMakerData lvmk;
	
	private BufferedImage frame, waterGod;
	
	public WaterState(GameStateManager gsm){
		
		super(gsm);

		int numColsToDraw = GamePanel.WIDTH / TileMap.tileSize;
		int numRowsToDraw = GamePanel.HEIGHT / TileMap.tileSize;
		
		frame = new BufferedImage(
				(numColsToDraw + 1) * TileMap.tileSize * GamePanel.SCALE,
				(numRowsToDraw + 1) * TileMap.tileSize * GamePanel.SCALE,
				BufferedImage.TYPE_INT_RGB
				);

		framesFreeze = framesFrozen = 10;

		boatMaxHealth = 14;
		
		waterLevelStart = new TextEvent(
				"NPC",
				"Here's the objects that the water god wanted. "
				+ "You'll have to protect it from monsters on the way to her throne. "
				+ "Kill the fish before they can shoot off their cannons. Good luck!"
				);
		
		waterGodThanks = new TextEvent(
				"Water God", 
				"Thank you for delivering these precious objects to me!"
				+ " I shall give you my blessing. Go on and save our land oh brave one!"
				);
		
		try{
			
			int boatSide = 32;
			
			BufferedImage boat = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/boat.png")
					);
			
			BufferedImage[] boatSheet = new BufferedImage[5]; 
			
			for (int i = 0; i < 5; i++){
				boatSheet[i] = boat.getSubimage(
						i * boatSide,
						0,
						boatSide,
						boatSide
						);
			}
			
			animatino = new Animation();
			animatino.setFrames(boatSheet, 15, false);
			
			bg = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Backgrounds/water.png")
					);
			
			lvmk = LevelMakerData.parse("water");
			tileMap = new TileMap(
					ImageIO.read(
							this.getClass().getResourceAsStream("/Resources/TileMaps/water.png")
							),
					lvmk.getTileTypes(), 
					lvmk.getEnemyData()
					);
			player = new Player(tileMap, gsm.getDifficulty());
			jaws = new Shark(tileMap);
			textEventListener = new TextEventListener();
			
		} catch (IOException e){
			System.out.println("Couldn't find something WaterState");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("SOme other error WATER");
			e.printStackTrace();
		}
		
	}
	
	public void init(){
		
		mapX = bgX = 0;
		mapDx = 1;
		bgDx = 32;
		
		boatHealth = boatMaxHealth;
		
		tileMap.init();
		tileMap.center(0, 0);
		
		player.spawn();
		
		waterLevelStart.reset();
		waterGodThanks.reset();
		
		ArrayList<String> textEvents = lvmk.getEvents();
		for (String s : textEvents){
			textEventListener.add(s);
		}
		
		playSound("water");
		paused = false;
		
	}
		
	//updates the level
	public void update(){
		if (textEventListener != null){
			if (!textEventListener.isPlaying()){
				if (tileMap.getX() == tileMap.getWidth() - GamePanel.WIDTH){
					if (waterGodThanks.isDone()){
						stopSound();
						gsm.complete(GameStateManager.WATERSTATE);
						gsm.setState(GameStateManager.HUBSTATE);
					} else {
						textEventListener.playMessage(waterGodThanks);
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
			mapX += mapDx;
			bgX--;
			if (mapX == 2){
				textEventListener.playMessage(waterLevelStart);
			}
			if (bgX < 0){
				bgX += bg.getWidth();
			}
			if (framesFrozen == framesFreeze){
				bgX += bgDx;
				bgX %= bg.getWidth();
				framesFrozen = 0;
			} else {
				framesFrozen++;
			}
			animatino.update();
			if (!jaws.isActive()){
				if (mapX >= GamePanel.WIDTH){
					jaws.spawn();
				}
			} else {
				jaws.getNextPosition(player);
				jaws.update();
			}
			if (!player.isDead()) player.update();
			tileMap.center(mapX, 0);
			tileMap.update(player);
		}
	}
		
	//draws to some graphics2D object
	public void draw(java.awt.Graphics2D g){
		try{
			if (paused){
				pauseMenu.draw(g);
			} else {
				if (textEventListener != null && textEventListener.isPlaying()){
					textEventListener.draw(g);
				} else if (tileMap != null && player != null){
					
					Graphics2D frameGraphics = (Graphics2D)frame.getGraphics();
					
					BufferedImage bg1 = bg.getSubimage(0, 0, bg.getWidth() - bgX, bg.getHeight());

					frameGraphics.drawImage(bg1.getScaledInstance(bg1.getWidth()* GamePanel.SCALE, bg1.getHeight() * GamePanel.SCALE, 0),
							tileMap.getDrawX() + bgX * GamePanel.SCALE, 
							tileMap.getDrawY(),
							null
							);
					
					if (bgX != 0){
						BufferedImage bg2 = bg.getSubimage(bg.getWidth() - bgX, 0, bgX, bg.getHeight());

						frameGraphics.drawImage(bg2.getScaledInstance(bg2.getWidth()* GamePanel.SCALE, bg2.getHeight() * GamePanel.SCALE, 0),
								tileMap.getDrawX(), 
								tileMap.getDrawY(),
								null
								);
						
					}
					frameGraphics.drawImage(
							animatino.getImage().getScaledInstance(64, 64, 0),
							(GamePanel.WIDTH / 2 - 16) * GamePanel.SCALE + tileMap.getDrawX(),
							(GamePanel.HEIGHT / 2 + 16 + (animatino.getFrame() % 2 == 0 ? 2 : 0)) * GamePanel.SCALE + tileMap.getDrawY(),
							null
							);
					if (!jaws.isActive()){
						player.draw(frameGraphics);
						tileMap.draw(frameGraphics);
					} else {
						if (jaws.isFalling()){
							player.draw(frameGraphics);
							tileMap.draw(frameGraphics);
							jaws.draw(frameGraphics);
						} else {
							jaws.draw(frameGraphics);
							player.draw(frameGraphics);
							tileMap.draw(frameGraphics);
						}
					}
					
					g.drawImage(frame.getSubimage(tileMap.getDrawX(), tileMap.getDrawY(), GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE),
							0,
							0,
							null
							);
					
					g.setColor(Color.BLACK);
					g.setFont(new Font("Times New Roman", Font.BOLD, 10));
					g.drawString("Health", 60, 10);
					g.setColor(Color.RED);
					g.drawRect(75, 15, player.getMaxHealth() * 10, 10);
					g.fillRect(75, 15, player.getHealth() * 10, 10);
					
					g.setColor(Color.BLACK);
					g.setFont(new Font("Times New Roman", Font.BOLD, 10));
					g.drawString("Boat Health", (GamePanel.WIDTH - 10) * GamePanel.SCALE, 10);
					g.setColor(Color.BLUE);
					g.drawRect((GamePanel.WIDTH - 100) * GamePanel.SCALE, 15, boatMaxHealth * 10, 10);
					g.fillRect((GamePanel.WIDTH - 100) * GamePanel.SCALE, 15, boatHealth * 10, 10);

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
					
					if (boatHealth < 0){
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
		
	public void keyPressed(int k){
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
	
	public void keyReleased(int k){
		if (paused){
			pauseMenu.keyReleased(k);
		}
		player.keyReleased(k);
	}

	public static void damageBoat(int damage){
		boatHealth -= damage;
		if (boatHealth <= 0){
			//do something
		}
	}
	
}
