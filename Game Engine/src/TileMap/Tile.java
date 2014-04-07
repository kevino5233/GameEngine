package TileMap;

import java.awt.image.BufferedImage;

public class Tile {

	public final static int NONBLOCKING = 0;
	public final static int BLOCKING = 1;
	
	private BufferedImage image;
	
	private int type;
	
	public Tile(BufferedImage image, int type){
		this.image = image;
		this.type = type;
	}
	
	public int getType(){ return type; }
	public BufferedImage getImage(){ return image; }
	
}
