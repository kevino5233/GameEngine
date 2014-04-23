package LevelMaker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class MapView extends JComponent implements MouseListener{

	private BufferedImage zaBokusu;
	
	private static final int SIDE = 600;
	
	private static int resNum = 2;
	private static final int[] tilesAcross = {10, 20, 40, 60, 100};
	
	private JFrame paletteFrame;
	private TilePalettePanel palettePanel;
	
	private JFrame enemyFrame;
	private EnemyPalettePanel enemyPanel;
	
	private BufferedImage tile_map;
	
	private BufferedImage[][] tiles;
	private int[][] level_data;
	
	private int camX, camY;
	
	public MapView(){
		setFocusable(true);
		setPreferredSize(new Dimension(SIDE, SIDE));
		
		tiles = new BufferedImage[tilesAcross[resNum]][tilesAcross[resNum]];
		level_data = new int[tilesAcross[resNum]][tilesAcross[resNum]];
		
		camX = 0;
		camY = 0;
		
		addMouseListener(this);
		
		repaint();
		
		palettePanel = new TilePalettePanel(this);
		paletteFrame = new JFrame("Select Tile");
		paletteFrame.setContentPane(palettePanel);
	    
		enemyPanel = new EnemyPalettePanel(this);
		enemyFrame = new JFrame("Select Enemy");
		enemyFrame.setContentPane(palettePanel);
		
		paletteFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		paletteFrame.setResizable(false);
		try {
			zaBokusu = ImageIO.read(new File("./Resources/ZA_BOX.png"));
		} catch (IOException e) {
			System.out.println("Could not load grid");
		}
	}
	
	public void addTile(BufferedImage tile, int tile_num, int x, int y){

		paletteFrame.setVisible(false);
		
		int res = SIDE / tilesAcross[resNum];
		
		x = (x + camX) / res;
		y = (y + camX) / res;
		
		tiles[y][x] = tile;
		level_data[y][x] = tile_num;
		
		repaint();
		
	}
	
	private BufferedImage currentFrame(){
		int res = SIDE / tilesAcross[resNum];
		BufferedImage level_view = new BufferedImage(SIDE, SIDE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) level_view.getGraphics();
		for (int j = 0; j < tilesAcross[resNum] && j < level_data.length; j++){
			for (int i = 0; i < tilesAcross[resNum] && i < level_data[0].length; i++){
				
				if (level_data[j + camY][i + camX] != 0){
					System.out.println(level_data[j + camY][i + camX]);
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
		return level_view;
	}

	public void paint(Graphics g){
		super.paintComponent(g);
		g = (Graphics2D)g;
		g.drawImage(currentFrame(), 0, 0, null);
	}
	
	public String getLevelData(){
		return LevelMakerData.getSaveableData(tile_map, level_data);
	}
	
	public void load(LevelMakerData lvmk){
		tile_map = lvmk.getTileMap();
		level_data = lvmk.getTileTypes();
		tiles = new BufferedImage[level_data.length][level_data[0].length];
		palettePanel.setMap(tile_map, 16);
		int tiles_vertically_across = tile_map.getHeight() / 16;
		int tiles_horizontally_across = tile_map.getWidth() / 16;
		for (int j = 0; j < level_data.length; j++){
			for (int i = 0; i < level_data[0].length; i++){
				int tile_type = level_data[j][i];
				tiles[j][i] = tile_map.getSubimage(
						tile_type % tiles_horizontally_across * 16,
						tile_type / tiles_vertically_across * 16,
						16,
						16
				);
			}
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
		if (++camX + tilesAcross[resNum] >= level_data[0].length) --camX;
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
		if (++camY + tilesAcross[resNum] >= level_data.length) --camY;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1){
			requestFocusInWindow();
			palettePanel.setCoordinates(e.getX(), e.getY());
			paletteFrame.pack();
			paletteFrame.setVisible(true);
		} else {
			requestFocusInWindow();
			//enemyPanel.setCoordinates(e.getX(), e.getY());
			enemyFrame.pack();
			enemyFrame.setVisible(true);
		}
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
