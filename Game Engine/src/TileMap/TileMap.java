package TileMap;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Bat;
import Entity.Enemy;
import Entity.Player;
import Entity.Sprite;
import LevelMaker.LevelMakerData;
import Main.GamePanel;

public class TileMap {

	private final static int tileSize = 16;
	
	private double camX, camY;
	private int spawnX, spawnY;

	private int rowOffset, colOffset;
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
	
	public TileMap(String s){
		try{
			
			spawnX = 1;
			spawnY = 4;
			
			LevelMakerData temp = LevelMakerData.parse(new File("./Resources/Levels/" + s + ".lvmk"));
			tileMap = temp.getTileMap();
			
			tileData = temp.getTileTypes();
			
			background = ImageIO.read(new File("./Resources/Backgrounds/" + s + ".png"));
			
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
			
			frame = new BufferedImage(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, BufferedImage.TYPE_INT_RGB);
			
			liveEnemies = new ArrayList<Enemy>();
			liveEnemies.add(new Bat(this, 5 * tileSize, 5 * tileSize));
			liveEnemies.add(new Bat(this, 4 * tileSize, 4 * tileSize));
			
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
		
		for (int i = 0; i < liveEnemies.size(); i++){
			Enemy e = liveEnemies.get(i);
			if (e == null) continue;
			//fix the ranges to allow for one tile outlier?
			if (e.getX() + e.getWidth() < camX ||
				e.getX() > camX + GamePanel.WIDTH || 
				e.getY() + e.getHeight() < camY || 
				e.getY() > camY + GamePanel.HEIGHT){
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
		
		
	}
}
