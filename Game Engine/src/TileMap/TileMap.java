package TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.*;
import Event.TextEventListener;
import LevelMaker.LevelMakerData;
import Main.GamePanel;

public class TileMap {

	public final static int tileSize = 16;
	
	private double camX, camY;
	private int spawnX, spawnY;

	private int numRowsToDraw, numColsToDraw;
	
	private int rows, cols;
	private int height, width;
	
	private int numTilesAcross;
	
	private BufferedImage tileMap, background, frame;
	
//	spawn enemies 3 tiles before camera
//	despawn enemies 10 tiles after camera
//	enemy manager thing, with arraylist of enemies
//	private SpawnListener spawnListener;
//	
//	private Enemy[][] enemySpawns;
	
	private ArrayList<Enemy> liveEnemies;
	
	private int[][] tileData;
	private Tile[][] tiles;
	
	private Enemy[][] enemyData;
	
	public TileMap(BufferedImage bg, BufferedImage tm, int[][] td, String[][] ed){
		tileMap = tm;
		
		tileData = td;
		
		rows = tileData.length;
		cols = tileData[0].length;
		
		tiles = new Tile[rows][cols];
		
		numTilesAcross = tileMap.getWidth() / tileSize;
		
		int w = numTilesAcross;
		for (int j = 0; j < tileData.length; j++){
			for (int i = 0; i < tileData[0].length; i++){
				int type = tileData[j][i];
				tiles[j][i] = new Tile(tileMap.getSubimage(type % numTilesAcross * tileSize, 
														   type / numTilesAcross * tileSize, 
														   tileSize, 
														   tileSize), 
														   type / w);
			}
		}
		
		height = rows * tileSize;
		width = cols * tileSize;
		
		enemyData = new Enemy[ed.length][ed[0].length];
		for (int j = 0; j < ed.length; j++){
			for (int i = 0; i < ed[0].length; i++){
				switch(ed[j][i]){
				case "None" : 
					break;
				case "Player" :
					spawnY = j;
					spawnX = i;
					break;
				case "Bat" : 
					enemyData[j][i] = new Bat(this, i * tileSize, j * tileSize); 
					break;
				case "Goat" :
					enemyData[j][i] = new Goat(this, i * tileSize, j * tileSize);
					break;
				case "Spike" :
					enemyData[j][i] = new Spike(this, i * tileSize, j * tileSize);
					break;
				}
			}
		}
		
		liveEnemies = new ArrayList<>();

		background = bg;
		
		frame = new BufferedImage(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, BufferedImage.TYPE_INT_RGB);
		
	}
	
	public int getTileSize(){ return tileSize; }
	public int getX(){ return (int)camX; }
	public int getY(){ return (int)camY; }
	public int getPlayerSpawnX(){ return spawnX * tileSize; }
	public int getPlayerSpawnY(){ return spawnY * tileSize; }
	public int getHeight(){ return height; }
	public int getWidth(){ return width; }
	public int getRows(){ return rows; }
	public int getCols(){ return cols; }
	public int getRowsAcross(){ return numRowsToDraw; }
	public int getColsAcross(){ return numColsToDraw; }
	
	public Tile getTile(int x, int y){ return tiles[y][x]; }
	//public Enemy getEnemy(int x, int y) return enemySpawns[y][x]; }
	
	public void testTouch(Sprite sp){
		
		//IT FUCKING WORKS. THANK GOD.
		//JK CAN'T LAND BETWEEN PLATFORMS GGGGGG
		
		int x = sp.getX() / tileSize;
		int x2 = (sp.getX() + sp.getWidth()) / tileSize;
		int y = sp.getY() / tileSize;
		int y2 = (sp.getY() + sp.getHeight()) / tileSize;

		int code = -1;
		
		Rectangle rec = new Rectangle(0, 0, 0, 0);
		
		if (getTile(x, y).getType() == Tile.BLOCKING){
        	int temp = Sprite.isTouching(
        			new Rectangle(x * tileSize, y*tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	if (temp > code) {
        		code = temp;
        		rec = new Rectangle(x * tileSize, y*tileSize, tileSize, tileSize);
        	}
        }
		
        if (x2 < getCols() && getTile(x2, y).getType() == Tile.BLOCKING){
        	int temp = Sprite.isTouching(
        			new Rectangle(x2 * tileSize, y * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	if (temp > code) {
        		code = temp;
        		rec = new Rectangle(x2 * tileSize, y * tileSize, tileSize, tileSize);
        	}
        }
        
        if (y2 < getRows() && getTile(x, y2).getType() == Tile.BLOCKING){
        	int temp = Sprite.isTouching(
        			new Rectangle(x * tileSize, y2 * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	if (temp > code) {
        		code = temp;
        		rec = new Rectangle(x * tileSize, y2 * tileSize, tileSize, tileSize);
        	}
        }

        if (x2 < getCols() && y2 < getRows() && getTile(x2, y2).getType() == Tile.BLOCKING){
        	int temp = Sprite.isTouching(
        			new Rectangle(x2 * tileSize, y2 * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(),sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	if (temp > code) {
        		code = temp;
        		rec = new Rectangle(x2 * tileSize, y2 * tileSize, tileSize, tileSize);
        	}
        }
        
        sp.collide(rec, code);
	}
	
	public void center(double x, double y){
		camX = x - GamePanel.WIDTH / 2;
		camY = y - GamePanel.HEIGHT / 2;
		
		if (camX < 0) camX = 0;
		if (camX > width - GamePanel.WIDTH) camX = width - GamePanel.WIDTH;
		if (camY < 0) camY = 0;
		if (camY > height - GamePanel.HEIGHT) camY = height - GamePanel.HEIGHT;
		
		
	}
	
	public void update(Player player){
		
//		textEventListener.playMessage(player.getX(), player.getY());
//		
//		if (textEventListener.isPlaying()){
//			return;
//		}
		
		center(player.getX(), player.getY());
		
		int numColsToDraw = GamePanel.WIDTH / tileSize + 1;
		int numRowsToDraw = GamePanel.HEIGHT / tileSize + 1;
		int colOffset = (int)camX / tileSize;
		int rowOffset = (int)camY / tileSize;
		
		BufferedImage cam = new BufferedImage(numColsToDraw * tileSize * GamePanel.SCALE, 
											  numRowsToDraw * tileSize * GamePanel.SCALE, 
											  BufferedImage.TYPE_INT_RGB
											 );
		
		Graphics2D camGraphics = (Graphics2D)cam.getGraphics();
		
		int draw_x = (int)camX - colOffset * tileSize;
		int draw_y = (int)camY - rowOffset * tileSize;
		
		draw_x *= GamePanel.SCALE;
		draw_y *= GamePanel.SCALE;
		
		camGraphics.drawImage(background.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0), 
							  draw_x, 
							  draw_y, 
							  null
							 );
		
		for (int j = 0; j < numRowsToDraw; j++){
			int row = rowOffset + j;
			if (row >= rows) break;
			for (int i = 0; i < numColsToDraw; i++){
				int col = colOffset + i;
				if (col >= cols) break;
				
				if (enemyData[row][col] != null){
					Enemy e = enemyData[row][col];
					if (!e.isActive()){
						//add in bounds thing
						e.spawn();
						liveEnemies.add(e);
					}
				}
				
				if (tileData[row][col] == 0) continue;
				
				camGraphics.drawImage(tiles[row][col].getImage().getScaledInstance(tileSize * GamePanel.SCALE, tileSize * GamePanel.SCALE, 0),
									  i * tileSize * GamePanel.SCALE,
									  j * tileSize * GamePanel.SCALE,
									  null
									 );
				
			}
		}
		
		frame.getGraphics().drawImage(cam.getSubimage(draw_x, draw_y, GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE),
									  0,
									  0,
									  null
									 );
		
		//keep working on this
		
		for (int i = 0; i < liveEnemies.size(); i++){
			Enemy e = liveEnemies.get(i);
			if (e == null) continue;
			//fix the ranges to allow for one tile outlier?
			if (//!e.isActive() ||
				e.getX() + e.getWidth() < camX ||
				e.getX() > camX + GamePanel.WIDTH || 
				e.getY() + e.getHeight() < camY || 
				e.getY() > camY + GamePanel.HEIGHT){
				System.out.println("Enemy dying");
				e.die();
				liveEnemies.remove(i);
			} else {
				e.getNextPosition(player);
				e.update();
			}
		}
	}
	
	public void draw(Graphics2D g){
		
		g.drawImage(frame,
					0, 
					0, 
					null
					);
		
		for (Enemy e : liveEnemies){
			e.draw(g);
		}
		
//		if (textEventListener.isPlaying()){
//			textEventListener.draw(g);
//		}
		
	}
}
