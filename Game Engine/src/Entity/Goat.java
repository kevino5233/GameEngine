package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Goat extends Enemy{

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	
	private int startX, endX;
	
	private int maxHealth, health;
	
	private double activateDistance;
	
	private boolean attacking;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {1, 4};
	
	public Goat(TileMap tm, double x, double y) {
		super(tm, x, y);
		
		startX = endX = (int)x;
		
		width = 32;
		height = 16;
		
		right = true;
		
		active = false;
		
		spriteSheet = new BufferedImage[4][];

		try{
			BufferedImage temp = ImageIO.read(new File("Resources/Sprites/Enemies/goat.png"));
			for (int j = 0; j < spriteSheet.length / 2; j++){	
				spriteSheet[j * 2] = new BufferedImage[numFrames[j]];
				spriteSheet[j * 2 + 1] = new BufferedImage[numFrames[j]];
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j * 2][i] = temp.getSubimage(i * width, (j * 2) * height, width, height);
				}
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j * 2 + 1][i] = temp.getSubimage(i * width, (j * 2 + 1) * height, width, height);
				}
			}
		} catch (IOException e){
			System.out.println("Couldn't find the spritesheet");
		} catch (Exception e){
			System.out.println("Some error happened");
			e.printStackTrace();
		}

		currentState = IDLE;
		
		animatino = new Animation();
		
		animatino.setFrames(spriteSheet[IDLE], 10);
		
	}

	@Override
	public void getNextPosition(Sprite sp) {
		int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height),
									 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
									);
		if (code != -1){
			sp.takeDamage(3);
		}
		if (x == endX){
			int deltaX = endX - startX;

			startX = endX;

			if (attacking){
				
				attacking = false;

				endX = startX + deltaX;

				dx = 1;
				dy = 1;

				if (deltaX < 0) dx = -1;
				if (endX < 0) endX = 0;

				//System.out.println("Retreating");
				//System.out.println(endX + " " + endY);
				//System.out.println(dx + " " + dy);
			} else {

				attacking = true;

				endX = sp.getX();

				deltaX = endX - startX;

				dx = 1;
				dy = 1;

				if (deltaX < 0) dx = -1;
				if (endX < 0) endX = 0;

				//System.out.println("Attacking");
				//System.out.println(deltaX + " " + deltaY);
				//System.out.println(startX + ", "  + startY + "-->" + endX + " " + endY);
				//System.out.println(dx + " " + dy);
			}
		} else {
			if ((dx > 0 && x >= endX) ||
				(dx < 0 && x <= endX)){
				dx = 0;
				x = endX;
			}
		}

		x += dx;

		if (x < 0) x = 0;

		tileMap.testTouch(this);
		
	}

	@Override
	public void spawn() {
		// TODO Auto-generated method stub
		x = spawnX;
		y = spawnY;
		active = true;
		currentState = IDLE;
		animatino.setFrames(spriteSheet[IDLE], 10);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		right = dx > 0;
		if (Math.abs(dx) > 0){
			if (currentState != WALKING){
				currentState = WALKING;
				if (right){
					animatino.setFrames(spriteSheet[WALKING * 2], 10);
				} else {
					animatino.setFrames(spriteSheet[WALKING * 2 + 1], 10);
				}
			}
		} else {
			if (currentState != IDLE){
				currentState = IDLE;
				if (right){
					animatino.setFrames(spriteSheet[IDLE * 2], 10);
				} else {
					animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10);
				}
			}
		}
		animatino.update();
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub
		active = false;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.drawImage(animatino.getImage().getScaledInstance(width * GamePanel.SCALE, height * GamePanel.SCALE, 0),
				(getX() - tileMap.getX()) * GamePanel.SCALE,
				(getY() - tileMap.getY()) * GamePanel.SCALE,
				null
			   );
	}

	@Override
	public void collide(Rectangle rec, int code) {
		// TODO Auto-generated method stub
		switch(code){
		case -1 : 
			dx *= -1;
			break;
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

}
