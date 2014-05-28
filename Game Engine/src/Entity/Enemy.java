package Entity;

import java.awt.Graphics2D;

import Main.GamePanel;
import TileMap.TileMap;

public abstract class Enemy extends Sprite{
	
	protected boolean active;
	
	protected double spawnX, spawnY;

	public Enemy(TileMap tm) {
		super(tm);
		
	}
	
	public abstract void getNextPosition(Player sp);
	
	public Enemy(TileMap tm, double x, double y){
		this(tm);
		this.x = spawnX = x;
		this.y = spawnY = y;
	}
	
	public boolean isActive(){ return active; }
	
	public double distance(Sprite sp){
		int x2 = sp.getX();
		int y2 = sp.getX();
		
		double deltaX = x - x2;
		double deltaY = y - y2;
		
		deltaX *= deltaX;
		deltaY *= deltaY;
		
		return Math.sqrt(deltaX + deltaY);
		
	}
	
	public void draw(Graphics2D g){
		g.drawImage(animatino.getImage().getScaledInstance(width * GamePanel.SCALE, height * GamePanel.SCALE, 0),
				(getX() - tileMap.getX()) * GamePanel.SCALE + tileMap.getDrawX(),
				(getY() - tileMap.getY()) * GamePanel.SCALE + tileMap.getDrawY(),
				null
			   );
	}
}
