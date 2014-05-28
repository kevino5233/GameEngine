package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import GameState.MenuState;
import Main.GamePanel;
import TileMap.TileMap;

public class Bat extends Enemy{

	private static final int IDLE = 0;
	private static final int FLYING = 1;
	
	private int startX, startY,
				endX, endY;
	
	private int maxHealth, health;
	
	private double activateDistance;
	
	private boolean attacking;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {4, 4};
	
	public Bat(TileMap tm, double x, double y) {
		super(tm, x, y);
		
		startX = endX = (int)x;
		startY = endY = (int)y;
		
		width = 16;
		height = 16;
		
		active = false;
		
		terminalVelocity = 2;
		
		maxHealth = 3;
		
		spriteSheet = new BufferedImage[2][];
		
		try{
			BufferedImage temp = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Enemies/bat.png")
					);
			for (int j = 0; j < spriteSheet.length; j++){
				spriteSheet[j] = new BufferedImage[numFrames[j]];
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j][i] = temp.getSubimage(
											i * width,
											j * height,
											width,
											height
											);
				}
			}
		} catch (Exception e){
			System.out.println("Error loading the bat");
			e.printStackTrace();
		}
		
		activateDistance = 70;
		
		currentState = FLYING;
		animatino = new Animation();
		animatino.setFrames(spriteSheet[FLYING], 4, false);
		
	}
	
	public void getNextPosition(Player sp){
		
		if (sp.isAttacking() && sp.getHitbox().contains(x, y)){
			playSound("Player/sword_impact");
			takeDamage(3);
		}
		
		int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height),
				  					 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
				 					);
		if (code != -1){
			sp.takeDamage(3);
		}
		
		if (x == endX && y == endY){
			int deltaX = endX - startX;
			int deltaY = endY - startY;
			
			startX = endX;
			startY = endY;
			
			if (attacking){
				attacking = false;

				if (deltaY > 0) deltaY *= -1;
				
				endX = startX + deltaX;
				endY = startY + deltaY;
				
				dx = 1;
				dy = 1;
				
				if (deltaX < 0) dx = -1;
				if (deltaY < 0) dy = -1;
				if (endX < 0) endX = 0;
				if (endY < 0) endY = 0;
				
//				System.out.println("Retreating");
//				System.out.println(endX + " " + endY);
//				System.out.println(dx + " " + dy);
			} else {
				
				attacking = true;
				
				endX = sp.getX();
				endY = sp.getY();
				
				deltaX = endX - startX;
				deltaY = endY - startY;
				
				dx = 1;
				dy = 1;
				
				if (deltaX < 0) dx = -1;
				if (deltaY < 0) dy = -1;
				if (endX < 0) endX = 0;
				if (endY < 0) endY = 0;
				
//				System.out.println("Attacking");
//				System.out.println(deltaX + " " + deltaY);
//				System.out.println(startX + ", "  + startY + "-->" + endX + " " + endY);
//				System.out.println(dx + " " + dy);
			}
		} else {
			if ((dx > 0 && x >= endX) ||
				(dx < 0 && x <= endX)){
				dx = 0;
				x = endX;
			}
			if ((dy > 0 && y >= endY) ||
				(dy < 0 && y <= endY)){
				dy = 0;
				y = endY;
			}
		}
		
		x += dx;
		y += dy;
		
		if (x < 0) x = 0;
		if (y < 0) y = 0;
		
		tileMap.testTouch(this);
	}

	@Override
	public void spawn() {
		x = spawnX;
		y = spawnY;
		startX = endX = (int)x;
		startY = endY = (int)y;
		currentState = FLYING;
		health = maxHealth;
		active = true;
	}

	@Override
	public void update() {
		animatino.update();
	}

	@Override
	public void die() {
		active = false;
		tileMap.removeEnemy(this);
	}

	@Override
	public void init() {
		
	}
	
	@Override
	public void draw(Graphics2D g){
		super.draw(g);
	}

	@Override
	public void collide(Rectangle rec, int code) {
		switch(code){
		case -1 : break;
		case Sprite.UP : 
						y = rec.getY() - getHeight();
						if (y < 0){
							y = 0;
						}
						break;
		case Sprite.DOWN : 
							y = rec.getY() + rec.getHeight();
							break;
		case Sprite.RIGHT : 
							x = rec.getX() + rec.getWidth();
							break;
		case Sprite.LEFT : 
							x = rec.getX() - getWidth();
							if (x < 0) x = 0;
							break;
		}
	}
	
	public BufferedImage getFrame(){ return animatino.getImage(); }

}
