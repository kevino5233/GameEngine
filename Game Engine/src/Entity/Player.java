package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Player extends Sprite{
	
	private int framesInvincible, framesLeftInvincible;
	
	private int spawnX, spawnY;
	
	private static final double GRAVITY = 0.5;
	private double jumpStart, stopJumpSpeed;
	
	private boolean moving;
	
	BufferedImage[][] spriteSheet;
	
	private final int[] numFrames = {3, 8, 1};
	
	private static int IDLE = 0;
	private static int WALKING = 1;
	private static int JUMPING = 2;
	private static int FALLING = 3;
	private static int ATTACKING = 4;
	private static int BLOCKING = 5;
	private static int FLINCHING = 6;
	private static int EARTHMOVE = 7;
	/*might not exist */private static int AIRMOVE = 8;
	
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
		stopJumpSpeed = .3;
		
		right = true;
		
		spriteSheet = new BufferedImage[6][];
		
		try{
			BufferedImage temp = ImageIO.read(new File("Resources/Sprites/Player/player" + (fire ? "-fire" : "") + ".png"));
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
		} catch (Exception e){
			System.out.println("You be fucked son");
			e.printStackTrace();
		}

		currentState = IDLE;
		moving = false;
		animatino = new Animation();
		animatino.setFrames(spriteSheet[IDLE], 10);
		
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
		getNextPosition();
		animatino.update();
		if (framesLeftInvincible > 0) framesLeftInvincible--;
		//animation stuff
	}
	
	public void draw(Graphics2D g){
		if (!(framesLeftInvincible % 10 >= 1 && framesLeftInvincible % 10 <= 6)){
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
					draw_x * GamePanel.SCALE, 
					draw_y * GamePanel.SCALE, 
					null
					);
		}
		//draw on the stuff, make sure you account for map position
	}
	
	private void turnLeft(){
		right = false;
		dx = -3;
		if(!moving){
			moving = true;
		}
		if (currentState != JUMPING && currentState != WALKING){
			currentState = WALKING;
			animatino.setFrames(spriteSheet[WALKING * 2 + 1], 3);
		} else if (currentState == JUMPING){
			animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10);
		}
	}
	
	private void turnRight(){
		right = true;
		dx = 3;
		if(!moving){
			moving = true;
		}
		if (currentState != JUMPING && currentState != WALKING){
			currentState = WALKING;
			animatino.setFrames(spriteSheet[WALKING * 2], 3);
		} else if (currentState == JUMPING){
			animatino.setFrames(spriteSheet[JUMPING * 2], 10);
		}
	}
	
	private void jump(){
		if (currentState != JUMPING){
			dy = jumpStart;
			currentState = JUMPING;
		}
		if (right){
			animatino.setFrames(spriteSheet[JUMPING * 2], 10);
		} else {
			animatino.setFrames(spriteSheet[JUMPING * 2 + 1], 10);
		}
	}
	
	private void land(){
		if (currentState == JUMPING){
			dy = 0;
		}
		if (moving){
			currentState = WALKING;
			if (right){
				animatino.setFrames(spriteSheet[WALKING * 2], 3);
			} else {
				animatino.setFrames(spriteSheet[WALKING * 2 + 1], 3);
			}
		} else {
			currentState = IDLE;
			if (right){
				animatino.setFrames(spriteSheet[IDLE * 2], 10);
			} else {
				animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10);
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
	}
	
	public void takeDamage(int damage){
    	if (currentState != BLOCKING && framesLeftInvincible == 0){
    		framesLeftInvincible = framesInvincible;
    		super.takeDamage(damage);
    	}
	}
	
	@Override
	public void die(){
		//stuff stuff game over blah blah animation death
		System.out.println("You be dead");
	}

	public void keyPressed(int k){
		switch(k){
		case KeyEvent.VK_UP : 
			jump();
			break;
		case KeyEvent.VK_DOWN : 
			if (currentState != JUMPING){
//				currentState = BLOCKING;
//				dy = 0;
//				dx = 0;
//				animatino.setFrames(spriteSheet[BLOCKING], 10);
			}

			break;
		case KeyEvent.VK_LEFT : 
			turnLeft();
			break;
		case KeyEvent.VK_RIGHT : 
			turnRight();
			break;
		}
	}
	
	public void keyReleased(int k){
		switch(k){
		case KeyEvent.VK_UP :
			dy /= 3;
			break;
		case KeyEvent.VK_DOWN : 
			if (currentState != JUMPING) /*or other ariel states*/currentState = IDLE;
			if (right){
				animatino.setFrames(spriteSheet[IDLE * 2], 10);
			} else {
				animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10);
			}
			break;
		case KeyEvent.VK_LEFT : 
			moving = false;
			if (currentState != JUMPING) {
				currentState = IDLE;
				if (right){
					animatino.setFrames(spriteSheet[IDLE * 2], 10);
				} else {
					animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10);
				}
			}
			
			dx = 0;
			break;
		case KeyEvent.VK_RIGHT : 
			moving = false;
			if (currentState != JUMPING){
				currentState = IDLE;
				if (right){
					animatino.setFrames(spriteSheet[IDLE * 2], 10);
				} else {
					animatino.setFrames(spriteSheet[IDLE * 2 + 1], 10);
				}
			}
			
			dx = 0;
			break;
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
	public boolean isAirborne(){ return currentState == JUMPING; }
	
	@Override
	public void collide(Rectangle rec, int code) {
		switch(code){
		case -1 : currentState = JUMPING; break;
		case Sprite.UP : 
						y = rec.getY() - getHeight();
						if (isAirborne()){
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
