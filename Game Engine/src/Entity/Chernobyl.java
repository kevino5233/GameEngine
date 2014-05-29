package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Chernobyl extends Enemy{

	private ArrayList<Eyeball> spawnedEyeballs;
	
	private int currentState;

	private int framesInvincible, framesLeftInvincible,
				numFramesWiggle, numFramesWiggled,
				framesUntilSpawn, framesToSpawn;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {4, 2};
	
	private static final int SPAWN = 0;
	private static final int WIGGLE = 1;
	
	public Chernobyl(TileMap tm, double x, double y) {
		super(tm, x, y);
		
		spawnX = x;
		spawnY = y;
		
		height = 64;
		width = 64;
		
		numFramesWiggle = 56;
		framesToSpawn = 103;
		
		terminalVelocity = 1;
		
		maxHealth = 10;
		
		framesInvincible = 20;
		
		spriteSheet = new BufferedImage[2][];
		
		animatino = new Animation();
		
		spawnedEyeballs = new ArrayList<>();
		
		try{
			BufferedImage temp = ImageIO.read(
						getClass().getResourceAsStream("/Resources/Sprites/Enemies/chernobyl.png")
					);
			for (int j = 0; j < numFrames.length; j++){
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
		} catch (IOException e){
			System.out.println("Problem finding the Cherynobyl");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Not another Cherynobyl");
			e.printStackTrace();
		}
		
	}

	@Override
	public void spawn() {
		active = true;
		
		currentState = SPAWN;
		animatino.setFrames(spriteSheet[SPAWN], 3, true);
		
		spawnedEyeballs.clear();
		
		numFramesWiggled = numFramesWiggle;
		framesUntilSpawn = 0;
		
		health = maxHealth;
		
		x = spawnX;
		y = spawnY;
	}
	
	@Override
	public void getNextPosition(Player sp) {
		for (Eyeball e : spawnedEyeballs){
			e.getNextPosition(sp);
		}
		
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
		
		if (numFramesWiggled++ == numFramesWiggle){
			numFramesWiggled = 0;
			long seed = System.currentTimeMillis();
			if (seed % 2 == 0){
				dy = 1;
			} else {
				dy = -1;
			}
			if (seed % 3 == 1){
				dx = 1;
			} else {
				dx = -1;
			}
		}
		
		x += dx;
		y += dy;
		
		if (y < 0) {
			y = 0;
		} else if (y + height >= tileMap.getHeight()){
			y = tileMap.getHeight() - height;
		}
		if (x < 0) {
			x = 0;
		} else if (x + width >= tileMap.getWidth()){
			x = tileMap.getWidth() - width;
		}
		
		tileMap.testTouch(this);
	}

	@Override
	public void update() {
		if (currentState == SPAWN){
			if (animatino.hasPlayedOnce()){
				currentState = WIGGLE;
				animatino.setFrames(spriteSheet[WIGGLE], 2, false);
			}
		} else {
			if (framesUntilSpawn == framesToSpawn){
				spawnedEyeballs.add(new Eyeball(tileMap, getX(), getY()));
				spawnedEyeballs.get(spawnedEyeballs.size() - 1).spawn();
				framesUntilSpawn = 0;
			} else if (spawnedEyeballs.size() < 3){
				framesUntilSpawn++;
			}
		}
		for (int x = 0; x < spawnedEyeballs.size(); x++){
			Enemy e = spawnedEyeballs.get(x);
			if (!e.isActive()){
				e.die();
				spawnedEyeballs.remove(x);
			}
			e.update();
		}
		animatino.update();
	}
	
	@Override
	public void draw(Graphics2D g){
		super.draw(g);
		for (Eyeball e : spawnedEyeballs){
			e.draw(g);
		}
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

}
