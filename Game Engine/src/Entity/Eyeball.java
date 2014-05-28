package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import TileMap.TileMap;

public class Eyeball extends Enemy{
	
	private int numFramesFreeze, numFramesFrozen;
	
	private int currentState;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {4, 4};
	
	private static int IDLE = 0;
	private static int MOVING = 1;
	
	public Eyeball(TileMap tm, double x, double y) {
		super(tm);
		
		spawnX = x;
		spawnY = y;
		
		height = 16;
		width = 16;
		
		numFramesFreeze = numFramesFrozen = 60;
		
		terminalVelocity = 1;
		
		maxHealth = 3;
		
		spriteSheet = new BufferedImage[4][];
		
		animatino = new Animation();
		
		try{
			BufferedImage temp = ImageIO.read(
						getClass().getResourceAsStream("/Resources/Sprites/Enemies/eyeball.png")
					);
			for (int j = 0; j < numFrames.length; j++){
				spriteSheet[j * 2] = new BufferedImage[numFrames[j]];
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j * 2][i] = temp.getSubimage(
											i * width,
											j * height,
											width,
											height
											);
				}
				spriteSheet[j * 2 + 1] = new BufferedImage[numFrames[j]];
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j * 2 + 1][i] = temp.getSubimage(
											i * width,
											j * height,
											width,
											height
											);
				}
			}
		} catch (IOException e){
			System.out.println("Problem finding the file Eyeball");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Some fucking error Eyeball");
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getNextPosition(Player sp) {
		
		if (sp.isAttacking() && sp.getHitbox().contains(x, y)){
			playSound("Player/sword_impact");
			takeDamage(3);
		}
		
		int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height),
				  					 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
				 					);
		
		
		if (code != -1){
			sp.takeDamage(3);
			currentState = IDLE;
			dy = dx = 0;
			if (right){
				animatino.setFrames(spriteSheet[IDLE * 2 + 1], 3, false);
			} else {
				animatino.setFrames(spriteSheet[IDLE * 2], 3, false);
			}
			numFramesFrozen = 0;
		}
		
		if (numFramesFreeze == numFramesFrozen){
			
			
			int deltaX = getX() - sp.getX();
			int deltaY = getY() - sp.getY();
			
			if (deltaX != 0){
				if (deltaX < 0){
					right = false;
					dx = terminalVelocity;
				} else {
					right = true;
					dx = -terminalVelocity;
				}
			} else {
				dx = 0;
			}
			
			if (deltaY != 0){
				if (deltaY < 0){
					right = false;
					dy = terminalVelocity;
				} else {
					right = true;
					dy = -terminalVelocity;
				}
			} else {
				dy = 0;
			}
			
			x += dx;
			y += dy;
			
		} else {
			numFramesFrozen++;
		}
		
	}

	@Override
	public void spawn() {
		
		x = spawnX;
		y = spawnY;
		
		currentState = IDLE;
		
		if (right){
			animatino.setFrames(spriteSheet[IDLE * 2 + 1], 3, false);
		} else {
			animatino.setFrames(spriteSheet[IDLE * 2], 3, false);
		}
		
		right = true;
		active = true;
		
	}

	@Override
	public void update() {
		if (dy != 0 || dx != 0){
			currentState = MOVING;
			animatino.setFrames(
					spriteSheet[MOVING * 2 + (right ? 1 : 0)]
					, 3
					, false
					);
		}
		animatino.update();
	}
	
	@Override
	public void draw(Graphics2D g){
		super.draw(g);
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
