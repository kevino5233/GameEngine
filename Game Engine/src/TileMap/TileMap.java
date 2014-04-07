package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import LevelMaker.LevelMakerData;
import Main.GamePanel;

public class TileMap {

	private final static int tileSize = 16;
	
	private int camX, camY;
	private int spawnX, spawnY;

	private int rowOffset, colOffset;
	private int numRowsToDraw, numColsToDraw;
	
	private int rows, cols;
	private int height, width;
	
	private int numTilesAcross;
	
	private BufferedImage tileMap;
	
//	spawn enemies 3 tiles before camera
//	despawn enemies 10 tiles after camera
//	enemy manager thing, with arraylist of enemies
//	private SpawnListener spawnListener;
//	
//	private Enemy[][] enemySpawns;
	
	private int[][] tileData;
	private Tile[][] tiles;
	
	public TileMap(String s){
		try{
			LevelMakerData temp = LevelMakerData.parse(new File("./Resources/Levels/" + s + ".lvmk"));
			tileMap = temp.getTileMap();
			
			tileData = temp.getTileTypes();
			
			rows = tileData.length;
			cols = tileData[0].length;
			
			tiles = new Tile[rows][cols];
			
			height = rows * tileSize;
			width = cols * tileSize;
			
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
			
			//File[] sheets = (new File("./Resources/Sprites/Enemies/" + s)).listFiles();
			
			/*
			 * 
			int[][] spawnData = temp.getSpawns();
			Enemy[] enemies = new Enemies[sheets.length];
			
			for (int x = 0; x < sheets.length; x++){
				//we need a way to differentiate the different enemies
				enemies[x] = new Enemy(sheets[x]);
			}
			
			for (int j = 0; j < spawnData.length; j++){
				for (int i = 0; i < spawnData[0].length; i++){
					if (spawnData[j][i] == -1){
						spawnX = i;
						spawnY = j;
					} else if (spawnData[j][i] == 0){
						enemySpawns[j][i] = null;
					} else {
						enemySpawns[j][i] = enemies[spawnData[j][i]];
					}
				}
			}
			spawnListener = new SpawnListener(enemySpawns);
			
			//somehow instantiate rowOffSet and colOffSet
			 * 
			 */
		} catch (Exception e){
			System.out.println("You dunn fucked up");
			e.printStackTrace();
		}
	}
	
	public int getTileSize(){ return tileSize; }
	public int getX(){ return camX; }
	public int getY(){ return camY; }
	public int getPlayerSpawnX(){ return spawnX; }
	public int getPlayerSpawnY(){ return spawnY; }
	public int getHeight(){ return height; }
	public int getWidth(){ return width; }
	public int getRows(){ return rows; }
	public int getCols(){ return cols; }
	public int getRowsAcross(){ return numRowsToDraw; }
	public int getColsAcross(){ return numColsToDraw; }
	
	public Tile getTile(int x, int y){ return tiles[y][x]; }
	//public Enemy getEnemy(int x, int y) return enemySpawns[y][x]; }
	
	public void draw(Graphics2D g){
	
		int numRowsToDraw = GamePanel.HEIGHT / tileSize;
		int numColsToDraw = GamePanel.WIDTH / tileSize;
		int rowOffset = camY / tileSize;
		int colOffset = camX / tileSize;
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
			if (row >= rows) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++){
				if (col >= cols) break;
				if (tileData[row][col] == 0) continue;
				
				g.drawImage(tiles[row][col].getImage().getScaledInstance(tileSize * GamePanel.SCALE, tileSize * GamePanel.SCALE, 0), 
						   (int)camX + col * tileSize * GamePanel.SCALE, 
						   (int)camY + row * tileSize * GamePanel.SCALE, 
						   null
						   );
			}
		}
	}
}
