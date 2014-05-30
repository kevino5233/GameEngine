package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Fukushima extends Enemy{

	private ArrayList<Chernobyl> spawnedNuclearDisasters;
	
	private int framesInvincible, framesLeftInvincible,
				framesUntilFire, framesToFire,
				framesFired, framesFire, 
				framesUntilSpawn, framesToSpawn;
	
	private int currentState;
	
	private Animation laserAnimatino;
	private BufferedImage[] laserSpriteSheet;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {1, 4, 2};
	
	private static final int IDLE = 0;
	private static final int SHOOP = 1;
	private static final int DAWOOP = 2;
	
	public Fukushima(TileMap tm, double x, double y) {
		super(tm, x, y);

		height = 128;
		width = 320;

		framesFire = 60;
		framesToSpawn = 180;
		framesToFire = 90;
		framesInvincible = 20;
		
		maxHealth = 30;
		
		spriteSheet = new BufferedImage[3][];
		laserSpriteSheet = new BufferedImage[6];
		
		animatino = new Animation();
		laserAnimatino = new Animation();
		
		spawnedNuclearDisasters = new ArrayList<>();
		
		try{
			BufferedImage temp = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Enemies/fukushima.png")
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
			
			temp = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Enemies/laser.png")
					);
			for (int i = 0; i < 6; i++){
				laserSpriteSheet[i] = temp.getSubimage(
						i * 21,
						0,
						21,
						(i + 1) * 16
						);
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
		
		currentState = IDLE;
		animatino.setFrames(spriteSheet[IDLE], 3, true);
		
		spawnedNuclearDisasters.clear();
		
		health = maxHealth;
		
		x = spawnX;
		y = spawnY;
		
		framesFired = 0;
		framesUntilFire = 0;
		framesUntilSpawn = 0;
		framesLeftInvincible = 0;
	}

	@Override
	public void getNextPosition(Player sp) {
		for (Chernobyl c : spawnedNuclearDisasters){
			c.getNextPosition(sp);
		}
		if (currentState == DAWOOP){
			int code = Sprite.isTouching(
					new Rectangle(
							getX() + 150, 
							getY() + 80, 
							laserAnimatino.getImage().getWidth(), 
							laserAnimatino.getImage().getHeight()
							),
					new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
					);

			if (code != -1){
				sp.takeDamage(3);
			}
		}
		
		if (framesLeftInvincible-- <= 0 &&
				sp.isAttacking() &&
				sp.getHitbox().intersects(new Rectangle(getX() + 137, getY(), 46, 76))
				){
			playSound("Player/sword_impact");
			framesLeftInvincible = framesInvincible;
			takeDamage(3);
		}

		int code = Sprite.isTouching(new Rectangle(getX() + 137, getY(), 46, 76),
				new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
				);

		if (code != -1){
			sp.takeDamage(3);
		}
	}

	@Override
	public void update() {
		if (currentState == DAWOOP){
			laserAnimatino.update();
			if (framesFired++ == framesFire){
				framesFired = 0;
				currentState = IDLE;
				animatino.setFrames(spriteSheet[IDLE], 1, true);
			} else {
				//update laser animation
			}
		}else if (currentState == SHOOP){
			if (animatino.hasPlayedOnce()){
				currentState = DAWOOP;
				animatino.setFrames(spriteSheet[DAWOOP], 3, false);
				laserAnimatino.setFrames(laserSpriteSheet, 3, true);
				//set laser animation
			}
		}else if (currentState == IDLE){
			if (framesUntilFire++ == framesToFire){
				framesUntilFire = 0;
				currentState = SHOOP;
				//play charging sound
				animatino.setFrames(spriteSheet[SHOOP], 5, true);
			} else if (framesUntilSpawn++ == framesToSpawn){
				framesUntilSpawn = 0;
				if (spawnedNuclearDisasters.size() == 0){
					spawnedNuclearDisasters.add(new Chernobyl(tileMap, getX() + 240, getY() + 64));
					spawnedNuclearDisasters.get(0).spawn();
				} else if (spawnedNuclearDisasters.size() == 1){
					spawnedNuclearDisasters.add(new Chernobyl(tileMap, getX() + 80, getY() + 64));
					spawnedNuclearDisasters.get(1).spawn();
				}
			}
		} 
		
		for (int x = 0; x < spawnedNuclearDisasters.size(); x++){
			Enemy e = spawnedNuclearDisasters.get(x);
			if (!e.isActive()){
				e.die();
				spawnedNuclearDisasters.remove(x);
			}
			e.update();
		}
		animatino.update();
	}
	
	@Override
	public void draw(Graphics2D g){
		if (currentState == DAWOOP){
			BufferedImage laser = laserAnimatino.getImage();
			g.drawImage(
					laser.getScaledInstance(laser.getWidth() * GamePanel.SCALE, laser.getHeight() * GamePanel.SCALE, 0),
					(getX() + 150 - tileMap.getX()) * GamePanel.SCALE + tileMap.getDrawX(),
					(getY() + 80 - tileMap.getY()) * GamePanel.SCALE + tileMap.getDrawY(),
					null
					);
		}
		super.draw(g);
		for (Enemy e : spawnedNuclearDisasters){
			e.draw(g);
		}
	}

	@Override
	public void die() {
		//death animation? + corpse?
		active = false;
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
