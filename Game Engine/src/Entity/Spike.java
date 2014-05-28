package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Spike extends Enemy{
	
	private BufferedImage sprite;

	public Spike(TileMap tm, double x, double y){
		super(tm, x, y);
		
		active = false;
		
		height = 16;
		width = 16;
		
		try{
			sprite = ImageIO.read(new File("./Resources/Sprites/Enemies/Spike.png"));
		} catch (IOException e){
			System.out.println("COuldn't find picture");
		} catch (Exception e){
			System.out.println("Some other error");
		}
		
	}
	
	@Override
	public void getNextPosition(Player sp) {
		int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height),
					 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
				);
		if (code != -1){
			sp.takeDamage(3);
		}
	}

	@Override
	public void spawn() {
		active = true;
	}

	@Override
	public void update() {
		
	}

	@Override
	public void die() {
		active = false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
			g.drawImage(sprite.getScaledInstance(width * GamePanel.SCALE, height * GamePanel.SCALE, 0),
					(getX() - tileMap.getX()) * GamePanel.SCALE + tileMap.getDrawX(),
					(getY() - tileMap.getY()) * GamePanel.SCALE + tileMap.getDrawY(),
					null
				   );
	}

	@Override
	public void collide(Rectangle rec, int code) {
		// TODO Auto-generated method stub
		
	}

}
