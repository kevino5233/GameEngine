package GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Entity.Player;
import Main.GamePanel;
import TileMap.TileMap;

public class HubState extends GameState{
	
	private TileMap tileMap;
	
	private Player player;
	
	//draw everything on to this image and then draw this image on to the main panel thing when it is called by the GSM
	//maybe have a background class to add more variety to backgrounds
	private BufferedImage bg;
	
	//something to keep track of the level ports/doors
	
	//potentially a SpawnManager for enemies
	
	public HubState(GameStateManager gsm){
		this.gsm = gsm;
	}

	@Override
	public void init() {
		try{
			tileMap = new TileMap("hub");
			player = new Player(tileMap, gsm.getFire(), gsm.getAir(), gsm.getDifficulty());
			player.spawn();
			bg = ImageIO.read(new File("./Resources/Backgrounds/hub.png"));
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (tileMap != null && player != null){
			player.update();
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
