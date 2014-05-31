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
	
	private int drawX, drawY;
	
	private int numTilesAcross;
	
	private BufferedImage tileMap;
	
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
	
	public TileMap(BufferedImage tm, int[][] td, String[][] ed){
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
		
		numColsToDraw = GamePanel.WIDTH / tileSize + 1;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 1;
		
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
				case "Fist" :
					enemyData[j][i] = new Fist(this, i * tileSize, j * tileSize);
					break;
				case "Eyeball" :
					enemyData[j][i] = new Eyeball(this, i * tileSize, j * tileSize);
					break;
				case "Chernobyl" :
					enemyData[j][i] = new Chernobyl(this, i * tileSize, j * tileSize);
					break;
				case "Fish" :
					enemyData[j][i] = new Fish(this, i * tileSize, j * tileSize);
					break;
				}
			}
		}
		liveEnemies = new ArrayList<>();

		
		//frame = new BufferedImage(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, BufferedImage.TYPE_INT_RGB);
		
	}
	
	public void init(){
		
		liveEnemies.clear();
		
		int colOffset = (int)camX / tileSize;
		int rowOffset = (int)camY / tileSize;
		
		for (int j = 0; j < numRowsToDraw; j++){
			int row = rowOffset + j;
			if (row >= rows) break;
			for (int i = 0; i < numColsToDraw; i++){
				int col = colOffset + i;
				if (col >= cols) break;
				if (enemyData[row][col] != null){
					Enemy e = enemyData[row][col];
					e.spawn();
					liveEnemies.add(e);
				}
			}
		}
		System.out.println(liveEnemies.size() + " enemies");
	}
	
	public int getTileSize(){ return tileSize; }
	public int getX(){ return (int)camX; }
	public int getY(){ return (int)camY; }
	public int getDrawX(){ return drawX; }
	public int getDrawY(){ return drawY; }
	public int getPlayerSpawnX(){ return spawnX * tileSize; }
	public int getPlayerSpawnY(){ return spawnY * tileSize; }
	public int getHeight(){ return height; }
	public int getWidth(){ return width; }
	public int getRows(){ return rows; }
	public int getCols(){ return cols; }
	public int getRowsAcross(){ return numRowsToDraw; }
	public int getColsAcross(){ return numColsToDraw; }
	
	public Tile getTile(int x, int y){ return tiles[y][x]; }
	
	public void removeEnemy(Enemy e){ liveEnemies.remove(e); }
		
	public void center(double x, double y){
		camX = x - GamePanel.WIDTH / 2;
		camY = y - GamePanel.HEIGHT / 2;
		
		if (camX < 0) camX = 0;
		if (camX > width - GamePanel.WIDTH) camX = width - GamePanel.WIDTH;
		if (camY < 0) camY = 0;
		if (camY > height - GamePanel.HEIGHT) camY = height - GamePanel.HEIGHT;
		
	}
	
	public void testTouch(Sprite sp){

		testVerticalCollision(sp);
		testHorizontalCollision(sp);
		
	}
	
	public void testVerticalCollision(Sprite sp){

		int x1 = sp.getX() / tileSize;
		int x2 = (sp.getX() + sp.getWidth()) / tileSize;
		int y1 = sp.getY() / tileSize;
		int y2 = (sp.getY() + sp.getHeight()) / tileSize;
		
		int code = -1;
		
		if (getTile(x1, y1).getType() == Tile.BLOCKING){
        	code = Sprite.isTouchingVertically(
        			new Rectangle(x1 * tileSize, y1*tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);

            if (code != -1) {
            	sp.collide(new Rectangle(x1 * tileSize, y1*tileSize, tileSize, tileSize), code);
            	return;
            }
        }
		
		if (x2 < getCols() && getTile(x2, y1).getType() == Tile.BLOCKING){
        	code = Sprite.isTouchingVertically(
        			new Rectangle(x2 * tileSize, y1 * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);

            if (code != -1) {
            	sp.collide(new Rectangle(x2 * tileSize, y1 * tileSize, tileSize, tileSize), code);
            	return;
            }
        }
        
        if (y2 < getRows() && getTile(x1, y2).getType() == Tile.BLOCKING){
        	code = Sprite.isTouchingVertically(
        			new Rectangle(x1 * tileSize, y2 * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);

        	if (code != -1) {
        		sp.collide(new Rectangle(x1 * tileSize, y2 * tileSize, tileSize, tileSize), code);
        		return;
        	}
        }

        if (x2 < getCols() && y2 < getRows() && getTile(x2, y2).getType() == Tile.BLOCKING){
        	code = Sprite.isTouchingVertically(
        			new Rectangle(x2 * tileSize, y2 * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(),sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	
        	if (code != -1) {
        		sp.collide(new Rectangle(x2 * tileSize, y2 * tileSize, tileSize, tileSize), code);
        		return;
        	}
        }
        
        if (code == -1){
        	sp.collide(null, -1);
        }
        
	}
	
	public void testHorizontalCollision(Sprite sp){

		int x1 = sp.getX() / tileSize;
		int x2 = (sp.getX() + sp.getWidth()) / tileSize;
		int y1 = sp.getY() / tileSize;
		
		if (getTile(x1, y1).getType() == Tile.BLOCKING){
        	int code = Sprite.isTouchingHorizontally(
        			new Rectangle(x1 * tileSize, y1*tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	if (code != -1) {
        		sp.collide(new Rectangle(x1 * tileSize, y1*tileSize, tileSize, tileSize), code);
        		return;
        	}
        }
		
		if (x2 < getCols() && getTile(x2, y1).getType() == Tile.BLOCKING){
        	int code = Sprite.isTouchingHorizontally(
        			new Rectangle(x2 * tileSize, y1 * tileSize, tileSize, tileSize),
        			new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
        			);
        	if (code != -1) {
            	sp.collide(new Rectangle(x2 * tileSize, y1*tileSize, tileSize, tileSize), code);
            	return;
            }
        }
		
		for (int y = sp.getY(); y < sp.getY() + sp.getHeight(); y += tileSize){
			int y2 = y / tileSize;
	        
	        if (y2 < getRows() && getTile(x1, y2).getType() == Tile.BLOCKING){
	        	int code = Sprite.isTouchingHorizontally(
	        			new Rectangle(x1 * tileSize, y2 * tileSize, tileSize, tileSize),
	        			new Rectangle(sp.getX(), y , sp.getWidth(), tileSize)
	        			);
	        	if (code != -1) {
	            	sp.collide(new Rectangle(x1 * tileSize, y2 * tileSize, tileSize, tileSize), code);
	            	return;
	            }
	        }

	        if (x2 < getCols() && y2 < getRows() && getTile(x2, y2).getType() == Tile.BLOCKING){
	        	int code = Sprite.isTouchingHorizontally(
	        			new Rectangle(x2 * tileSize, y2 * tileSize, tileSize, tileSize),
	        			new Rectangle(sp.getX(), y , sp.getWidth(), tileSize)
	        			);
	        	if (code != -1) {
	            	sp.collide(new Rectangle(x2 * tileSize, y2 * tileSize, tileSize, tileSize), code);
	            	return;
	            }        	
	        }
		}        
	}
	
	public void update(Player player){
		
		int numColsToDraw = GamePanel.WIDTH / tileSize + 1;
		int numRowsToDraw = GamePanel.HEIGHT / tileSize + 1;
		int colOffset = (int)camX / tileSize;
		int rowOffset = (int)camY / tileSize;
		
		drawX = (int)camX - colOffset * tileSize;
		drawY = (int)camY - rowOffset * tileSize;
		
		drawX *= GamePanel.SCALE;
		drawY *= GamePanel.SCALE;
		
		for (int x = colOffset - 1; x <= colOffset + numColsToDraw; x++){
			if (x < 0 || x >= cols) continue;
			if (rowOffset - 1 >= 0){
				if (enemyData[rowOffset - 1][x] != null){
					Enemy e = enemyData[rowOffset - 1][x];
					if (!e.isActive()){
						e.spawn();
						liveEnemies.add(e);
					}
				}
			}

			if (rowOffset + numRowsToDraw < rows){
				if (enemyData[rowOffset + numRowsToDraw][x] != null){
					Enemy e = enemyData[rowOffset + numRowsToDraw][x];
					if (!e.isActive()){
						e.spawn();
						liveEnemies.add(e);
					}
				}
			}
			
		}
		
		for (int y = rowOffset; y < rowOffset + numRowsToDraw; y++){
			if (y >= rows){
				break;
			}
			
			if (colOffset - 1 >=  0){
				if (enemyData[y][colOffset - 1] != null){
					Enemy e = enemyData[y][colOffset - 1];
					if (!e.isActive()){
						e.spawn();
						liveEnemies.add(e);
					}
				}
			}
			
			if (colOffset + numColsToDraw < cols){
				if (enemyData[y][colOffset + numColsToDraw] != null){
					Enemy e = enemyData[y][colOffset + numColsToDraw];
					if (!e.isActive()){
						e.spawn();
						liveEnemies.add(e);
					}
				}
			}
		}
		
		for (int i = 0; i < liveEnemies.size(); i++){
			Enemy e = liveEnemies.get(i);
			if (e == null) continue;
			//fix the ranges to allow for one tile outlier?
			if (	
					!e.isActive() ||
					e.getX() + e.getWidth() < camX - tileSize ||
					e.getX() > camX + GamePanel.WIDTH + tileSize || 
					e.getY() + e.getHeight() < camY - tileSize || 
					e.getY() > camY + GamePanel.HEIGHT + tileSize
				
				){
				e.die();
			} else {
				e.getNextPosition(player);
				e.update();
			}
		}
	}
	
	public void draw(Graphics2D g){
		
//		g.drawImage(frame,
//					0, 
//					0, 
//					null
//					);

		int colOffset = (int)camX / tileSize;
		int rowOffset = (int)camY / tileSize;
		
		for (int j = 0; j < numRowsToDraw; j++){
			int row = rowOffset + j;
			if (row >= rows) break;
			for (int i = 0; i < numColsToDraw; i++){
				int col = colOffset + i;
				if (col >= cols) break;
				if (tileData[row][col] == 0) continue;
				
				g.drawImage(tiles[row][col].getImage().getScaledInstance(tileSize * GamePanel.SCALE, tileSize * GamePanel.SCALE, 0),
									  i * tileSize * GamePanel.SCALE,
									  j * tileSize * GamePanel.SCALE,
									  null
									 );
				
			}
		}
		
		for (Enemy e : liveEnemies){
			e.draw(g);
		}
		
	}
}
