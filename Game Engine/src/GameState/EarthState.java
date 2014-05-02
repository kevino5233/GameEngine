package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
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
	
	private Goat goat1, goat2;
	
	private TextEvent earthGodThanks;
	
	//something to keep track of the level ports/doors
	
	//potentially a SpawnManager for enemies
	
	public EarthState(GameStateManager gsm){
		this.gsm = gsm;
		
		pauseMenu = new PauseState(this, gsm);
		pauseMenu.init();
		
		earthGodThanks = new TextEvent("Earth God", "You got the haunted goats! Thank goodness!");
		
		try{
			BufferedImage bg = ImageIO.read(new File("./Resources/Backgrounds/cave.png"));
			lvmk = LevelMakerData.parse(new File("./Resources/Levels/cave.lvmk"));
			tileMap = new TileMap(bg, lvmk.getTileMap(), lvmk.getTileTypes(), lvmk.getEnemyData());
			player = new Player(tileMap, gsm.getFire(), gsm.getAir(), gsm.getDifficulty());
			textEventListener = new TextEventListener();
			goat1 = new Goat(tileMap, 3 * tileMap.getTileSize(), 27 * tileMap.getTileSize());
			goat2 = new Goat(tileMap, 7 * tileMap.getTileSize(), 26 * tileMap.getTileSize());
			
		} catch (IOException e){
			System.out.println("Couldn't find something");
		} catch (Exception e){
			System.out.println("Some other error");
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		earthGodThanks.reset();
		goat1.spawn();
		goat2.spawn();
		player.spawn();
		ArrayList<String> textEvents = lvmk.getEvents();
		for (String s : textEvents){
			textEventListener.add(s);
		}
		playSound(new File("./Resources/Sound/Music/fire.wav"));
		paused = false;
	}

	@Override
	public void update() {
		
		if (textEventListener != null){
			
			if (!textEventListener.isPlaying()){
				
				if (goat1.isDead() && goat2.isDead()){
					textEventListener.playMessage(earthGodThanks);
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
			if (!goat1.isDead()){
				goat1.update();
				goat1.getNextPosition(player);
			}
			if (!goat2.isDead()){
				goat2.update();
				goat2.getNextPosition(player);
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
				if (textEventListener != null && textEventListener.isPlaying()){
					textEventListener.draw(g);
				} else {
					tileMap.draw(g);
					player.draw(g);
				}
			}
			if (!goat1.isDead() &&
				goat1.getX() + goat1.getWidth() > tileMap.getX() &&
				goat1.getX() < tileMap.getX() + GamePanel.WIDTH && 
				goat1.getY() + goat1.getHeight() > tileMap.getY() && 
				goat1.getY() < tileMap.getY() + GamePanel.HEIGHT){
					goat1.draw(g);
			}
			
			if (!goat2.isDead() &&
				goat2.getX() + goat2.getWidth() > tileMap.getX() &&
				goat2.getX() < tileMap.getX() + GamePanel.WIDTH && 
				goat2.getY() + goat2.getHeight() > tileMap.getY() && 
				goat2.getY() < tileMap.getY() + GamePanel.HEIGHT){
					goat2.draw(g);
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
			if (textEventListener.isPlaying()){
				textEventListener.keyPressed(k);
				if (goat1.isDead() && goat2.isDead()){
					stopSound();
					gsm.setState(GameStateManager.MENUSTATE);
				}
			}
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
