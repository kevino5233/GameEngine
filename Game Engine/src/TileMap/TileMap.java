package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import LevelMaker.LevelMakerData;

public class TileMap {

	private final static int tileSize = 16;
	
	private int camX, camY;

	private int rowOffset, colOffset;
	private int numRowsToDraw, numColsToDraw;
	
	private int height, width;
	
	private int numTilesAcross;
	
	private BufferedImage tileMap;
	
	//spawn enemies 3 tiles before camera
	//despawn enemies 10 tiles after camera
	//enemy manager thing, with arraylist of enemies
	private SpawnListener spawnListener;
	
	private Enemy[][] enemySpawns;
	
	private int[][] tileData;
	private Tile[][] tiles;
	
	public TileMap(String s){
		LevelMakerData temp = LevelMakerData.parse(new File("./Resources/Levels/" + s + ".lvmk"));
		tileMap = temp.getTileMap();
		
		int[][] mapData = temp.getTileTypes();
		int[][] spawnData = temp.getSpawns();
		
		height = mapData.length;
		width = mapData[0].length;
		numTilesAcross = tileMap.getWidth() / tileSize;

		int w = tileMap.getWidth() / tileSize;
		for (int j = 0; j < mapData.length; j++){
			for (int i = 0; i < mapData[0].length; i++){
				int type = mapData[j][i];
				tiles[j][i] = new Tile(tileMap.getSubimage(type % w, type / w, tileSize, tileSize), type / w == 0);
			}
		}
		
		File[] sheets = (new File("./Resources/Sprites/Enemies/" + s)).listFiles();
		Enemy[] enemies = new Enemies[sheets.length];
		
		for (int x = 0; x < sheets.length; x++){
			//we need a way to differentiate the different enemies
			enemies[x] = new Enemy(sheets[x]);
		}
		
		for (int j = 0; j < spawnData.length; j++){
			for (int i = 0; i < spawnData[0].length; i++){
				if (spawnData[j][i] == -1){
					//player spawns here
				} else if (spawnData[j][i] == 0){
					enemySpawns[j][i] = null;
				} else {
					enemySpawns[j][i] = enemies[spawnData[j][i]];
				}
			}
		}
		spawnListener = new SpawnListener(enemySpawns);
		
		//somehow instantiate rowOffSet and colOffSet
	}
	
	public int getTileSize(){ return tileSize; }
	public int getX(){ return camX; }
	public int getY(){ return camY; }
	public int getRows(){ return height; }
	public int getCols(){ return width; }
	
	public Tile getTile(int x, int y){ return tiles[y][x]; }
	public Enemy getEnemy(int x, int y) return enemySpawns[y][x]; }
	
	public void draw(Graphics2D g){
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
			if (row >= height) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++){
				if (col >= width) break;
				if (tileData[row][col] == 0) continue;
				
				int rc = tileData[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(tiles[r][c].getImage(), (int)camX + col * tileSize, (int)camY + row * tileSize, null);
			}
		}
	}
}
