package LevelMaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import Event.TextEvent;

public class MapView extends JComponent implements MouseListener{

	private BufferedImage zaBokusu;
	
	private static final int SIDE = 600;
	
	private static int resNum = 2;
	private static final int[] tilesAcross = {10, 20, 40, 60, 100};
	
	private InsertFrame insertFrame;
	
	private BufferedImage tileMap;
	
	private String[][] enemies;
	private BufferedImage[][] tiles;
	private int[][] levelData;
	private TextEvent[][] textEvents;
	
	private int camX, camY;
	
	private boolean displayEnemies;
	
	public MapView(){
		setFocusable(true);
		setPreferredSize(new Dimension(SIDE, SIDE));
		
		tiles = new BufferedImage[tilesAcross[resNum]][tilesAcross[resNum]];
		levelData = new int[tilesAcross[resNum]][tilesAcross[resNum]];
		enemies = new String[tilesAcross[resNum]][tilesAcross[resNum]];
		textEvents = new TextEvent[tilesAcross[resNum]][tilesAcross[resNum]];
		
		camX = 0;
		camY = 0;
		
		try {
			zaBokusu = ImageIO.read(new File("./Resources/ZA_BOX.png"));
		} catch (IOException e) {
			System.out.println("Could not load grid");
		}

		displayEnemies = false;
		
		insertFrame = new InsertFrame(this);
		insertFrame.setResizable(false);
		
		addMouseListener(this);
		
		repaint();
		
	}
	
	public void addEnemy(String name, int x, int y){
		
		int res = SIDE / tilesAcross[resNum];

		x = (x + camX) / res;
		y = (y + camY) / res;

		enemies[y][x] = name;
		
		repaint();
		
	}
	
	public void addTile(BufferedImage tile, int tile_num, int x, int y){

		int res = SIDE / tilesAcross[resNum];
		
		x = (x + camX) / res;
		y = (y + camY) / res;
		
		tiles[y][x] = tile;
		levelData[y][x] = tile_num;
		
		repaint();
		
	}
	
	public void addTextEvent(int x, int y, String speaker, String message){
		int res = SIDE / tilesAcross[resNum];
		
		x = (x + camX) / res;
		y = (y + camY) / res;
		
		textEvents[y][x] = new TextEvent(message, speaker);
	}
	
	private BufferedImage currentFrame(){
		BufferedImage levelView = new BufferedImage(SIDE, SIDE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) levelView.getGraphics();
		int res = SIDE / tilesAcross[resNum];
		for (int j = 0; j < tilesAcross[resNum] && j < levelData.length; j++){
			for (int i = 0; i < tilesAcross[resNum] && i < levelData[0].length; i++){
				
				if (levelData[j + camY][i + camX] != 0){
					Image temp = tiles[j + camY][i + camX].getScaledInstance(res, res, Image.SCALE_DEFAULT);
					g.drawImage(temp, i * res, j * res, null);
				} else {
					g.drawImage(
							zaBokusu.getScaledInstance(res, res, Image.SCALE_DEFAULT), 
							i * res, 
							j * res, 
							null
					);
				}
			}
		}
		if (displayEnemies){
			//set the alpha so the tiles will draw slightly transparent
			g.setColor(Color.BLACK);
			for (int j = 0; j < tilesAcross[resNum] && j < levelData.length; j++){
				for (int i = 0; i < tilesAcross[resNum] && i < levelData[0].length; i++){
					
					String enemy = enemies[j + camY][i + camX];
					
					if (enemy == null){
						enemy = "None";
					}
					
					g.drawString(enemy, i * res, j * res + res / 2);
					
				}
			}
		}
		return levelView;
	}

	public void paint(Graphics g){
		super.paintComponent(g);
		g = (Graphics2D)g;
		g.drawImage(currentFrame(), 0, 0, null);
	}
	
	public String getLevelData(){
		return LevelMakerData.getSaveableData(tileMap, levelData, enemies, textEvents);
	}
	
	public TextEvent getTextEvent(int x, int y){
		int res = SIDE / tilesAcross[resNum];
		
		x = (x + camX) / res;
		y = (y + camX) / res;
		
		return textEvents[y][x];
	}
	
	public void load(LevelMakerData lvmk){
		tileMap = lvmk.getTileMap();
		levelData = lvmk.getTileTypes();
		enemies = lvmk.getEnemyData();
		textEvents = new TextEvent[enemies.length][enemies[0].length];
		ArrayList<String> textEventList = lvmk.getEvents();
		insertFrame.setMap(tileMap, 16);
		tiles = new BufferedImage[levelData.length][levelData[0].length];
		int tiles_vertically_across = tileMap.getHeight() / 16;
		int tiles_horizontally_across = tileMap.getWidth() / 16;
		for (int j = 0; j < levelData.length; j++){
			for (int i = 0; i < levelData[0].length; i++){
				int tile_type = levelData[j][i];
				tiles[j][i] = tileMap.getSubimage(
						tile_type % tiles_horizontally_across * 16,
						tile_type / tiles_vertically_across * 16,
						16,
						16
				);
			}
		}
		for (String s : textEventList){
			String[] dat = s.split("[|]");
			
			int x = Integer.parseInt(dat[0]);
			int y = Integer.parseInt(dat[1]);
			
			String speaker = dat[3];
			String message = dat[2];
			TextEvent temp = new TextEvent(message, speaker);
			
			textEvents[y][x] = temp;
			
		}
		repaint();
	}
	
	public void zoomOut(){
		resNum++;
		if (resNum >= tilesAcross.length) resNum = tilesAcross.length - 1;
		repaint();
	}
	
	public void zoomIn(){
		resNum--;
		if (resNum < 0) resNum = 0;
		repaint();
	}
	
	public void moveRight(){
		if (++camX + tilesAcross[resNum] >= levelData[0].length) --camX;
		repaint();
	}
	
	public void moveLeft(){
		if (--camX < 0) camX = 0;
		repaint();
	}
	
	public void moveUp(){
		if (--camY < 0) camY = 0;
		repaint();
	}
	
	public void moveDown(){
		if (++camY + tilesAcross[resNum] >= levelData.length) --camY;
		repaint();
	}
	
	public void toggleEnemyDisplay(){
		displayEnemies = !displayEnemies;
		repaint();
	}

	public BufferedImage getTilemap(){ return tileMap; }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		insertFrame.setCoordinates(e.getX(), e.getY());
		insertFrame.pack();
		insertFrame.setVisible(true);
//		int numClicks = e.getClickCount();
//		if (e.getButton() == MouseEvent.BUTTON1){
//			requestFocusInWindow();
//			if (numClicks == 2){
//				System.out.println("Double clickuuu");
//			} else if (numClicks == 1){
//				palettePanel.setCoordinates(e.getX(), e.getY());
//				paletteFrame.pack();
//				paletteFrame.setResizable(false);
//				paletteFrame.setVisible(true);
//			}
//		} else {
//			requestFocusInWindow();
//			enemyPanel.setCoordinates(e.getX(), e.getY());
//			enemyFrame.pack();
//			enemyFrame.setResizable(false);
//			enemyFrame.setVisible(true);
//		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
