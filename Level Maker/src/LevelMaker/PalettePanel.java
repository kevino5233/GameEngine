package LevelMaker;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class PalettePanel extends JPanel implements ActionListener{
	
	private int tiles_horizontally_across;
	private int tiles_vertically_across;
	
	private int x, y;
	
	private JButton[][] tiles;
	
	private MapView parent;
	
	public PalettePanel(MapView parent){
		requestFocusInWindow();
		setPreferredSize(new Dimension(100, 100));
		this.parent = parent;
	}
	
	public void setMap(BufferedImage tile_map, int resolution){
		
		removeAll();
		
		int count = 0;
		
		tiles_horizontally_across = tile_map.getWidth() / resolution;
		tiles_vertically_across = tile_map.getHeight() / resolution;
		tiles = new JButton[tiles_vertically_across][tiles_horizontally_across];
		
		setLayout(new GridLayout(tiles_vertically_across, tiles_horizontally_across, 10, 10));
		
		resolution *= 4;
		
		setPreferredSize(new Dimension(
			tiles_horizontally_across * resolution + (tiles_horizontally_across	+ 1) * 10,
			tiles_vertically_across * resolution + (tiles_vertically_across + 1) * 10
		));
		
		BufferedImage new_tile_map = new BufferedImage(resolution * tiles_horizontally_across, resolution * tiles_vertically_across, BufferedImage.TYPE_INT_RGB);
		new_tile_map.getGraphics().drawImage(
				tile_map.getScaledInstance(resolution * tiles_horizontally_across, resolution * tiles_vertically_across, Image.SCALE_DEFAULT), 
				0, 
				0, 
				null
			);
		
		for (int j = 0; j < tiles_vertically_across; j++){
			for (int i = 0; i < tiles_horizontally_across; i++){
				BufferedImage temp = new_tile_map.getSubimage(i * resolution, j * resolution, resolution, resolution);
				tiles[j][i] = new JButton("" + count++, new ImageIcon(temp));
				tiles[j][i].addActionListener(this);
				this.add(tiles[j][i]);
			}
		}
		
	}

	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton temp_button = (JButton)ae.getSource();
		ImageIcon temp_icon = (ImageIcon)temp_button.getIcon();
		parent.addTile((BufferedImage)temp_icon.getImage(), Integer.parseInt(temp_button.getText()), x, y);
	}
	
	
}
