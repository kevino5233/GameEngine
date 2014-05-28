package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Player extends Sprite{
	
	private int framesInvincible, framesLeftInvincible;
	
	private int spawnX, spawnY;
	
	private static final double GRAVITY = 0.5;
	private double jumpStart;
	
	private boolean moving;
	
	private Animation swordamatino;
	
	BufferedImage[][] spriteSheet, swordSheet;
	
	private final int[] numFrames = {3, 8, 1, 4, 4, 4}, swordFrames = {4, 4, 4};
	
	private static int IDLE = 0;
	private static int WALKING = 1;
	private static int JUMPING = 2;
	private static int ATTACKING = 3;
	private static int JUMPATTACK = 4;
	//private static int BLOCKING = 5;
	private static int DYING = 5;
	//private static int FLINCHING = 5;
	//private static int EARTHMOVE = 6;
	//private static int AIRMOVE = 8;
	
	public Player(TileMap tm, boolean fire, boolean air, int difficulty){
		super(tm);
		
		spawnX = tm.getPlayerSpawnX();
		spawnY = tm.getPlayerSpawnY();
		
		width = 16;
		height = 32;
		
		maxHealth = 15 + difficulty * 2;
		
		framesInvincible = 30;
		framesLeftInvincible = 0;
		
		terminalVelocity = air ? 20 : 10;
		jumpStart = -terminalVelocity;
		
		right = true;
		
		spriteSheet = new BufferedImage[12][];
		swordSheet = new BufferedImage[6][];
		
		swordamatino = new Animation();
		
		try{
			BufferedImage temp = ImageIO.read(new File("./Resources/Sprites/Player/player" + (fire ? "-fire" : "") + ".png"));
			for (int j = 0; j < numFrames.length; j++){	
				spriteSheet[j * 2] = new BufferedImage[numFrames[j]];
				spriteSheet[j * 2 + 1] = new BufferedImage[numFrames[j]];
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j * 2][i] = temp.getSubimage(i * width, (j * 2) * height, width, height);
				}
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j * 2 + 1][i] = temp.getSubimage(i * width, (j * 2 + 1) * height, width, height);
				}
			}
			
			temp = ImageIO.read(new File("./Resources/Sprites/Player/sword.png"));
			for (int j = 0; j < swordFrames.length; j++){	
				swordSheet[j * 2] = new BufferedImage[swordFrames[j]];
				swordSheet[j * 2 + 1] = new BufferedImage[swordFrames[j]];
				for (int i = 0; i < swordFrames[j]; i++){
					swordSheet[j * 2][i] = temp.getSubimage(i * width, (j * 2) * height, width, height);
				}
				for (int i = 0; i < swordFrames[j]; i++){
					swordSheet[j * 2 + 1][i] = temp.getSubimage(i * width, (j * 2 + 1) * height, width, height);
				}
			}
			
		} catch (Exception e){
			System.out.println("You be fucked son");
			e.printStackTrace();
		}

		currentState = IDLE;
		moving = false;
		animatino = new Animation();
		
	}

	@Override
	public void init() {
		spawn();
		
	}
	
	private void getNextPosition(){
		
		//DUDUDUDUUUUN
		
		if (dy > terminalVelocity) {
			dy = terminalVelocity;
		}
		
		if (isAirborne()) {
			dy += GRAVITY;
		} 

		y += dy;
		x += dx;
		
		if (y < 0) {
			y = 0;
			snap();
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
	public void update(){
		if (super.isDead()){
			if (currentState != DYING){
				currentState = DYING;
				if (right){
					animatino.setFrames(spriteSheet[DYING * 2], 10, true);
					swordamatino.setFrames(swordSheet[(DYING - 3) * 2], 10, true);
				} else {
					animatino.setFrames(spriteSheet[DYING * 2 + 1], 10, true);
					swordamatino.setFrames(swordSheet[(DYING - 3) * 2 + 1], 10, true);
				}
			} else {
				animatino.update();
				swordamatino.update();
			}
		} else {
			getNextPosition();
			if (isAttacking()){
				if (animatino.hasPlayedOnce() == true){
					swordamatino.setFrame(swordFrames[currentState - 3] - 1);
					swordamatino.stop();
					if (isAirborne()){
						currentState = JUMPING;
						if (dx == 0){
							if (right){
								animatino.setFrames(spriteSheet[JUMPING * 2], 10, true);
							} else {
								animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10, true);
							}
						} else if (dx > 0){
							right = true;
							animatino.setFrames(spriteSheet[JUMPING * 2], 10, true);
						} else {
							right = false;
							animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10, true);
						}
					} else {
						currentState = IDLE;
						if (dx == 0){
							if (right){
								animatino.setFrames(spriteSheet[JUMPING * 2], 10, true);
							} else {
								animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10, true);
							}
						} else if (dx > 0){
							right = true;
							animatino.setFrames(spriteSheet[IDLE * 2], 10, false);
						} else {
							right = false;
							animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10, false);
						}
					}
				} else {
					animatino.update();
					swordamatino.update();
				}
				
			} else {
				animatino.update();	
			}
			if (framesLeftInvincible > 0) framesLeftInvincible--;
		}
		//animation stuff
	}
	
	public void draw(Graphics2D g){
		if (!(framesLeftInvincible % 10 >= 1 && framesLeftInvincible % 10 <= 4 || 
				framesLeftInvincible % 10 >= 6 && framesLeftInvincible % 10 <= 9)){
			BufferedImage temp = animatino.getImage();
			int draw_x = this.getX() - tileMap.getX();
			int draw_y = this.getY() - tileMap.getY();
			if (x > GamePanel.WIDTH / 2 && x + width < tileMap.getWidth() - GamePanel.WIDTH){
				draw_x = GamePanel.WIDTH / 2;
			}
			if (y > GamePanel.HEIGHT / 2 && y + height < tileMap.getHeight() - GamePanel.HEIGHT){
				draw_y = GamePanel.HEIGHT / 2;
			}
			g.drawImage(temp.getScaledInstance(getWidth() * GamePanel.SCALE, getHeight() * GamePanel.SCALE, 0), 
					draw_x * GamePanel.SCALE + tileMap.getDrawX(), 
					draw_y * GamePanel.SCALE + tileMap.getDrawY(), 
					null
					);
			if (currentState - 3 >= 0){
				temp = swordamatino.getImage();
				if (right){
					g.drawImage(temp.getScaledInstance(getWidth() * GamePanel.SCALE, getHeight() * GamePanel.SCALE, 0), 
							(draw_x + width) * GamePanel.SCALE + tileMap.getDrawX(), 
							draw_y * GamePanel.SCALE + tileMap.getDrawY(), 
							null
							);
				} else {
					g.drawImage(temp.getScaledInstance(getWidth() * GamePanel.SCALE, getHeight() * GamePanel.SCALE, 0), 
							(draw_x - width) * GamePanel.SCALE + tileMap.getDrawX(), 
							draw_y * GamePanel.SCALE + tileMap.getDrawY(), 
							null
							);
				}
			}
		}
		
	}
	
	private void turnLeft(){
		dx = -2;
		if(!moving){
			moving = true;
		}
		if (currentState != JUMPATTACK){ 
			right = false;
			if (currentState == JUMPING){
				animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10, true);
			} else if (currentState != WALKING){
				currentState = WALKING;
				animatino.setFrames(spriteSheet[WALKING * 2 + 1], 2, false);
			}
		}
		

	}
	
	private void turnRight(){
		dx = 2;
		if(!moving){
			moving = true;
		}
		if (currentState != JUMPATTACK){ 
			right = true;
			if (currentState == JUMPING){
				animatino.setFrames(spriteSheet[JUMPING * 2], 10, true);
			} else if (currentState != WALKING){
				currentState = WALKING;
				animatino.setFrames(spriteSheet[WALKING * 2], 2, false);
			}
		}
		
	}
	
	private void jump(){
		if (!isAttacking()){
			if (currentState != JUMPING){
				currentState = JUMPING;
			}
			if (right){
				animatino.setFrames(spriteSheet[JUMPING * 2], 10, true);
			} else {
				animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10, true);
			}
		}
	}
	
	private void land(){
		if (currentState == JUMPING){
			dy = 0;
		}
		if (moving && currentState != WALKING){
			currentState = WALKING;
			if (right){
				animatino.setFrames(spriteSheet[WALKING * 2], 2, false);
			} else {
				animatino.setFrames(spriteSheet[WALKING * 2 + 1], 2, false);
			}
		} else if (!moving && currentState != IDLE && currentState != ATTACKING){
			currentState = IDLE;
			if (right){
				animatino.setFrames(spriteSheet[IDLE * 2], 10, false);
			} else {
				animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10, false);
			}
		}
	}
	
	private void snap(){
		dy *= -1;
		dy /= 3;
	}

	@Override
	public void spawn() {
		x = spawnX;
		y = spawnY;
		currentState = IDLE;
		health = maxHealth;
		right = true;
		animatino.setFrames(spriteSheet[IDLE * 2], 10, false);
		framesLeftInvincible= 0;
		dx = 0;
		System.out.println("Done");
	}
	
	@Override
	public void takeDamage(int damage){
    	if (/*currentState != BLOCKING &&*/ framesLeftInvincible == 0){
    		playSound("Player/grunt");
    		framesLeftInvincible = framesInvincible;
    		super.takeDamage(damage);
    	}
	}
	
	@Override
	public boolean isDead(){
		
		return super.isDead() && 
				currentState == DYING &&
				animatino.hasPlayedOnce();
		
	}
	
	@Override
	public void die(){
		//stuff stuff game over blah blah animation death
		framesLeftInvincible = 0;
		if (currentState != DYING){
			currentState = DYING;
			if (right){
				animatino.setFrames(spriteSheet[DYING * 2], 10, true);
				swordamatino.setFrames(swordSheet[(DYING - 3) * 2], 10, true);
			} else {
				animatino.setFrames(spriteSheet[DYING * 2 + 1], 10, true);
				swordamatino.setFrames(swordSheet[(DYING - 3) * 2 + 1], 10, true);
			}
		}
	}
	
	@Override
	public Rectangle getHitbox(){
		if (isAttacking()){
			if (right){
				return new Rectangle(getX() + width, getY(), width, height);
			} else {
				return new Rectangle(getX() - width, getY(), width, height);
			}
		}
		return new Rectangle(0, 0, 0, 0);
	}

	public void keyPressed(int k){
		if (!super.isDead()){
			switch(k){
			case KeyEvent.VK_UP : 
				if (currentState != JUMPING){
					dy = jumpStart;
					jump();
				}
				break;
			case KeyEvent.VK_DOWN : 
				if (currentState != JUMPING){
//					currentState = BLOCKING;
//					dy = 0;
//					dx = 0;
//					animatino.setFrames(spriteSheet[BLOCKING], 10);
				}

				break;
			case KeyEvent.VK_LEFT : 
				turnLeft();
				break;
			case KeyEvent.VK_RIGHT : 
				turnRight();
				break;
			case KeyEvent.VK_Z :
				if (isAirborne()){
					currentState = JUMPATTACK;
					if (right){
						animatino.setFrames(spriteSheet[JUMPATTACK * 2], 2, true);
						swordamatino.setFrames(swordSheet[(JUMPATTACK - 3) * 2], 2, true);
					} else {
						animatino.setFrames(spriteSheet[JUMPATTACK * 2 + 1], 2, true);
						swordamatino.setFrames(swordSheet[(JUMPATTACK - 3) * 2 + 1], 2, true);
					}
				} else {
					currentState = ATTACKING;
					if (right){
						animatino.setFrames(spriteSheet[ATTACKING * 2], 2, false);
						swordamatino.setFrames(swordSheet[(ATTACKING - 3) * 2], 2, false);
					} else {
						animatino.setFrames(spriteSheet[ATTACKING * 2 + 1], 2, false);
						swordamatino.setFrames(swordSheet[(ATTACKING - 3) * 2 + 1], 2, false);
					}
				}
				break;
			}
		}
	}
	
	public void keyReleased(int k){
		if (!super.isDead()){
			switch(k){
			case KeyEvent.VK_UP :
				dy /= 3;
				break;
			case KeyEvent.VK_DOWN : 
				if (currentState != JUMPING){
					if (right){
						animatino.setFrames(spriteSheet[IDLE * 2], 10, true);
					} else {
						animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10, true);
					}/*or other ariel states*/
					currentState = IDLE;
				}
				break;
			case KeyEvent.VK_LEFT : 
				moving = false;
				if (currentState != JUMPING && currentState != JUMPATTACK) {
					currentState = IDLE;
					if (right){
						animatino.setFrames(spriteSheet[IDLE * 2], 10, true);
					} else {
						animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10, true);
					}
				}
				
				dx = 0;
				break;
			case KeyEvent.VK_RIGHT : 
				moving = false;
				if (currentState != JUMPING && currentState != JUMPATTACK){
					currentState = IDLE;
					if (right){
						animatino.setFrames(spriteSheet[IDLE * 2], 10, false);
					} else {
						animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10, false);
					}
				}
				
				dx = 0;
				break;
			}
		}
	}
	
	public int getX(){ return (int)x; }
	public int getY(){ return (int)y; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public int getCurrentHealth(){ return health; }
	public int getMaxHealth(){ return maxHealth; }
	public BufferedImage getCurrentFrame(){ return animatino.getImage(); }
	public boolean isFacingRight(){ return right; }
	public boolean isAttacking(){ return currentState == ATTACKING || currentState == JUMPATTACK; }
	public boolean isAirborne(){ return currentState == JUMPING || currentState == JUMPATTACK; }
	
	@Override
	public void collide(Rectangle rec, int code) {
		switch(code){
		case -1 : 
			if (currentState != JUMPATTACK) jump(); 
			break;
		case Sprite.UP : 
						y = rec.getY() - getHeight();
						if (!isAscending()){
							land(); 
						}
						if (y < 0){
							y = 0;
							snap();
						}
						break;
		case Sprite.DOWN : 
							y = rec.getY() + rec.getHeight();
							if (dy < 0) {
								dy *= -1;
								dy /= 3.;
							}
							break;
		case Sprite.RIGHT : 
							x = rec.getX() + rec.getWidth();
							break;
		case Sprite.LEFT : 
							x = rec.getX() - getWidth();
							if (x < 0) x = 0;
							break;
		}
//		System.out.println(rec);
//		System.out.println("Tile :: " + rec.getX() + " " + rec.getY());
//		System.out.println("Player :: " + x + " " + y);
//		System.out.println();
	}

	@Override
	public String toString() {
		return "aNIMATINO";
	}

	
	
}
