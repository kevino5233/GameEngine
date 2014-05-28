package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Goat extends Enemy{

	private static final int IDLE = 0;
	private static final int WALKING = 1;

	private int framesInvincible, framesLeftInvincible;
	
	private long numFramesToFreeze, numFramesUntilFreeze, numFramesFreeze, numFramesFrozen;
	
	private int x1, x2;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {1, 4};
	
	public Goat(TileMap tm, double x, double y) {
		super(tm, x, y);
		
		int startX = (int)x / tm.getTileSize();
		int startY = (int)y / tm.getTileSize();
		
		int tempX = startX;
		
		do{
			tempX--;
			System.out.println(tempX);
		}while(tileMap.getTile(tempX, startY).getType() != Tile.BLOCKING &&
			   tileMap.getTile(tempX, startY + 1).getType() == Tile.BLOCKING);
		
		x1 = tempX * tileSize;
		
		tempX = startX;
		
		do{
			tempX++;
		}while(tileMap.getTile(tempX, startY).getType() != Tile.BLOCKING &&
			   tileMap.getTile(tempX, startY + 1).getType() == Tile.BLOCKING);
		
		x2 = (tempX - 1) * tileSize - width;
		
		width = 32;
		height = 16;
		
		active = false;

		dx = 1;
		
		right = true;
		
		maxHealth = 10;
		
		framesInvincible = 20;
		
		spriteSheet = new BufferedImage[4][];

		try{
			BufferedImage temp = ImageIO.read(new File("./Resources/Sprites/Enemies/goat.png"));
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

		animatino = new Animation();
		
		currentState = WALKING;
		animatino.setFrames(spriteSheet[WALKING * 2 + 1], 3, false);
	}

	@Override
	public void getNextPosition(Player sp) {
		
		if (framesLeftInvincible-- <= 0 && 
			sp.isAttacking() && 
			sp.getHitbox().intersects(new Rectangle(getX(), getY(), width, height))
		   ){
			playSound("Player/sword_impact");
			framesLeftInvincible = framesInvincible;
			takeDamage(3);
		}
		
		int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height),
									 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
									);
		if (code != -1){
			sp.takeDamage(3);
		}
		
		if (dx > 0 && x >= x2){
			x = x2;
			dx = -1;
			right = false;
			animatino.setFrames(spriteSheet[WALKING * 2], 3, false);
		} else if (dx < 0 && x <= x1){
			x = x1;
			dx = 1;
			right = true;
			animatino.setFrames(spriteSheet[WALKING * 2 + 1], 3, false);
		}
		
		x += dx;
		
		
	}
	
	public void resetFreezeFrames(){
		numFramesToFreeze = System.currentTimeMillis() % 600;
		numFramesUntilFreeze = 0;
		numFramesFreeze = System.currentTimeMillis() % 300;
		numFramesFrozen = 0;
		currentState = WALKING;
		if (right){
			dx = 1;
			animatino.setFrames(spriteSheet[WALKING * 2 + 1], 3, false);
		} else {
			dx = -1;
			animatino.setFrames(spriteSheet[WALKING * 2], 3, false);
		}
	}

	@Override
	public void spawn() {
		// TODO Auto-generated method stub
		resetFreezeFrames();
		x = spawnX;
		y = spawnY;
		health = maxHealth;
		active = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (numFramesUntilFreeze++ >= numFramesToFreeze){
			if (currentState != IDLE){
				currentState = IDLE;
				dx = 0;
				if (right){
					animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10, true);
				} else {
					animatino.setFrames(spriteSheet[IDLE * 2], 10, true);
				}
			} else if (numFramesFrozen++ >= numFramesFreeze){
				resetFreezeFrames();
			}
		}
		animatino.update();
	}

	@Override
	public void die() {
		active = false;
		tileMap.removeEnemy(this);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		super.draw(g);
	}

	@Override
	public void collide(Rectangle rec, int code) {
		
	}
	
	public String toString(){
		return "Goat don't give a fuck";
	}

}