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
			System.out.println("Initing...");
			tileMap = new TileMap("hub");
			player = new Player(tileMap, gsm.getFire(), gsm.getAir(), gsm.getDifficulty());
			bg = ImageIO.read(new File("./Resources/Backgrounds/hub.png"));
			System.out.println("Done");
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (player != null) player.update();
	}

	@Override
	public void draw(Graphics2D g) {

		try{
			g.drawImage(bg.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0), 
					0, 
					0, 
					null
					);
			
			tileMap.draw(g);
			player.draw(g);
		} catch (NullPointerException e){
			System.out.println("Shit be null");
		} catch (Exception e){
			e.printStackTrace();
		}
		/*
		 * drawing the player
		 * //math and stuff to draw the player
		int x = player.getX();
		int y = player.getY();
		//tileMap.center(x, y);
		int camX = tileMap.getX();
		int camY = tileMap.getY();
		
		int drawX = GamePanel.WIDTH / 2;
		int drawY = GamePanel.HEIGHT / 2;
		
		int maxCamX = tileMap.getCols() - tileMap.getColsAcross();
		int maxCamY = tileMap.getRows() - tileMap.getRowsAcross();
		
		if (camX == 0){
			drawX = x;
		} else if (camX == maxCamX){
			drawX = x - (tileMap.getCols() - tileMap.getColsAcross());
		}
		
		if (camY == 0){
			drawY = y;
		} else if (camY == maxCamY){
			drawY = y - (tileMap.getRows() - tileMap.getRowsAcross());
		}
		
		g.drawImage(player.getCurrentFrame(), drawX, drawY, null);
		 */
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
