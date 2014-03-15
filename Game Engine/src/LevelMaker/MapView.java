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

	private BufferedImage za_bokusu;
	
	private static final int SIDE = 600;
	
	private static int res_num = 2;
	private static final int[] tiles_across = {10, 20, 40, 60, 100};
	
	private JFrame palette_frame;
	private PalettePanel palette_panel;
	
	private BufferedImage tile_map;
	
	private BufferedImage[][] tiles;
	private int[][] level_data;
	
	private int cam_x, cam_y;
	
	public MapView(){
		setFocusable(true);
		setPreferredSize(new Dimension(SIDE, SIDE));
		tiles = new BufferedImage[tiles_across[res_num]][tiles_across[res_num]];
		level_data = new int[tiles_across[res_num]][tiles_across[res_num]];
		cam_x = 0;
		cam_y = 0;
		addMouseListener(this);
		repaint();
		palette_panel = new PalettePanel(this);
		palette_frame = new JFrame("Select Tile");
		palette_frame.setContentPane(palette_panel);
	    palette_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		palette_frame.setResizable(false);
		try {
			za_bokusu = ImageIO.read(new File("./Resources/ZA_BOX.png"));
		} catch (IOException e) {
			System.out.println("Could not load grid");
		}
	}
	
	public void addTile(BufferedImage tile, int tile_num, int x, int y){

		palette_frame.setVisible(false);
		
		int res = SIDE / tiles_across[res_num];
		
		x = (x + cam_x) / res;
		y = (y + cam_x) / res;
		
		tiles[y][x] = tile;
		level_data[y][x] = tile_num;
		
		repaint();
		
	}
	
	private BufferedImage currentFrame(){
		int res = SIDE / tiles_across[res_num];
		BufferedImage level_view = new BufferedImage(SIDE, SIDE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) level_view.getGraphics();
		for (int j = 0; j < tiles_across[res_num] && j < level_data.length; j++){
			for (int i = 0; i < tiles_across[res_num] && i < level_data[0].length; i++){
				
				if (level_data[j + cam_y][i + cam_x] != 0){
					System.out.println(level_data[j + cam_y][i + cam_x]);
					Image temp = tiles[j + cam_y][i + cam_x].getScaledInstance(res, res, Image.SCALE_DEFAULT);
					g.drawImage(temp, i * res, j * res, null);
				} else {
					g.drawImage(
							za_bokusu.getScaledInstance(res, res, Image.SCALE_DEFAULT), 
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
		palette_panel.setMap(tile_map, 16);
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
		res_num++;
		if (res_num >= tiles_across.length) res_num = tiles_across.length - 1;
		repaint();
	}
	
	public void zoomIn(){
		res_num--;
		if (res_num < 0) res_num = 0;
		repaint();
	}
	
	public void moveRight(){
		if (++cam_x + tiles_across[res_num] >= level_data[0].length) --cam_x;
		repaint();
	}
	
	public void moveLeft(){
		if (--cam_x < 0) cam_x = 0;
		repaint();
	}
	
	public void moveUp(){
		if (--cam_y < 0) cam_y = 0;
		repaint();
	}
	
	public void moveDown(){
		if (++cam_y + tiles_across[res_num] >= level_data.length) --cam_y;
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		requestFocusInWindow();
		palette_panel.setCoordinates(e.getX(), e.getY());
		palette_frame.pack();
		palette_frame.setVisible(true);
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
