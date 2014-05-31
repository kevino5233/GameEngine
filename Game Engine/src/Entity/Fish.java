package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import GameState.WaterState;
import Main.GamePanel;
import TileMap.TileMap;

public class Fish extends Enemy{
	
	private int framesLeftInvincible, framesInvincible, 
				framesUntilSpawn, framesToSpawn,
				framesUntilPrefire, framesToPrefire,
				framesUntilFire, framesToFire;
	
	private BufferedImage cannonball;
	
	private int cannonballY;
	
	private int currentState;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {1, 10, 1, 4, 4};
	
	private static final int PUDDLE = 0;
	private static final int SPAWN = 1;
	private static final int IDLE = 2;
	private static final int PREFIRE = 3;
	private static final int POSTFIRE = 4;

	public Fish(TileMap tm, double x, double y) {
		super(tm, x, y);
		
		height = 64;
		width = 32;
		
		framesToSpawn = 30;
		framesToPrefire = 60;
		framesToFire = 90;
		framesInvincible = 20;
		
		maxHealth = 8;
		
		terminalVelocity = 20;
		
		spriteSheet = new BufferedImage[5][];
		
		animatino = new Animation();
		
		try{
			
			cannonball = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Enemies/cannonball.png")
					);
			
			BufferedImage temp = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Enemies/fish.png")
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
		
		currentState = PUDDLE;
		animatino.setFrames(spriteSheet[PUDDLE], 0, true);
		
		health = maxHealth;
		
		x = spawnX;
		y = spawnY;

		framesUntilSpawn = 0;
		framesUntilPrefire = 60;
		framesUntilFire = 0;
		framesLeftInvincible = 0;
	}

	@Override
	public void getNextPosition(Player sp) {
		if (framesLeftInvincible-- <= 0 &&
				sp.isAttacking() &&
				sp.getHitbox().intersects(new Rectangle(getX(), getY(), 46, 76))
				){
			//adjust later
			playSound("Player/sword_impact");
			framesLeftInvincible = framesInvincible;
			takeDamage(Player.getDamage());
		}
		
		if (dy != 0){
			if (dy < 0 && cannonballY + cannonball.getHeight() < 0){
				dy =  terminalVelocity;
			} else if (dy > 0 && cannonballY >= GamePanel.HEIGHT / 2){
				dy = 0;
				WaterState.damageBoat(2);
			}
			cannonballY += dy;
		}
	}

	@Override
	public void update() {
		if (health < 0){
			if (currentState != PUDDLE){
				currentState = PUDDLE;
				animatino.setFrames(spriteSheet[PUDDLE], 1, true);
			}
		} else {
			if (currentState == PUDDLE){
				if (framesUntilSpawn++ == framesToSpawn){
					currentState = SPAWN;
					animatino.setFrames(spriteSheet[SPAWN], 2, true);
				}
			
			} else if (currentState == SPAWN){
				if (animatino.hasPlayedOnce()){
					currentState = IDLE;
					animatino.setFrames(spriteSheet[IDLE], 1, true);
				}
			} else if (currentState == IDLE){
				if (framesUntilPrefire++ == framesToPrefire){
					framesUntilPrefire = 0;
					currentState = PREFIRE;
					animatino.setFrames(spriteSheet[PREFIRE], 2, true);
				}
			} else if (currentState == PREFIRE){
				if (animatino.hasPlayedOnce() && framesUntilFire++ == framesToFire){
					framesUntilFire = 0;
					dy = -terminalVelocity;
					cannonballY = getY();
					currentState = POSTFIRE;
					animatino.setFrames(spriteSheet[POSTFIRE], 2, true);
				}
				
			} else if (currentState == POSTFIRE){
				if (animatino.hasPlayedOnce()){
					currentState = IDLE;
					animatino.setFrames(spriteSheet[IDLE], 1, true);
				}
			}
		}
		animatino.update();
	}
	
	@Override
	public void draw(Graphics2D g){
		super.draw(g);
		if (dy != 0){
			if (dy < 0){
				g.drawImage(
						 cannonball.getScaledInstance(
									cannonball.getHeight() * GamePanel.SCALE, 
									cannonball.getWidth() * GamePanel.SCALE,
									0
								),
							(getX() + 8 - tileMap.getX()) * GamePanel.SCALE + tileMap.getDrawX(),
							(cannonballY - tileMap.getY()) * GamePanel.SCALE + tileMap.getDrawY(),
							null
							);
			} else if (dy > 0){
				g.drawImage(
						cannonball,
						GamePanel.WIDTH / 2 * GamePanel.SCALE + tileMap.getDrawX(),
						(cannonballY - tileMap.getY()) * GamePanel.SCALE + tileMap.getDrawY(),
						null
						);
			}
		}
	}

	@Override
	public void die() {
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide(Rectangle rec, int code) {
		// TODO Auto-generated method stub
		
	}

	
	
}
