package Entity;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Player extends Sprite{
	
	private int health, maxHealth;
	
	private int framesInvincible;
	
	private static final double GRAVITY = 1.0;
	private double jumpStart, stopJumpSpeed;
	
	private boolean jumping;
	
	BufferedImage[][] spriteSheet;
	
	private final int[] numFrames = {3, 2, 1, 2};
	
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
		
		x = tm.getPlayerSpawnX();
		y = tm.getPlayerSpawnY();
		
		width = 16;
		height = 32;
		
		maxHealth = 15 + difficulty * 2;
		health = maxHealth;
		
		framesInvincible = 0;
		
		terminalVelocity = air ? 40 : 30;
		jumpStart = -terminalVelocity;
		stopJumpSpeed = .3;
		
		right = true;
		
		spriteSheet = new BufferedImage[2][];
		
		try{
			BufferedImage temp = ImageIO.read(new File("Resources/Sprites/Player/player" + (fire ? "-fire" : "") + ".png"));
			for (int j = 0; j < spriteSheet.length; j++){	
				spriteSheet[j] = new BufferedImage[numFrames[j]];
				for (int i = 0; i < numFrames[j]; i++){
					spriteSheet[j][i] = temp.getSubimage(i * width, j * height, width, height);
				}
			}
		} catch (Exception e){
			System.out.println("You be fucked son");
			e.printStackTrace();
		}

		currentState = IDLE;
		animatino = new Animation();
		animatino.setFrames(spriteSheet[IDLE], 10);
		
	}
	
	public void getNextPosition(){
		
		double xtemp = x;
		double ytemp = y;
		
		
		dy += GRAVITY;
		if (dy > terminalVelocity) dy = terminalVelocity;
		
		if (dy < 0 && !jumping) dy += stopJumpSpeed;
		int x1 = (int) (xtemp) / tileSize;
		int y1 = (int) (ytemp) / tileSize;
        int x2 = (int) (xtemp + width) / tileSize;
        int y2 = (int) (ytemp + height) / tileSize;
//    	if (tileMap.getTile(x1 / tileSize, y1 / tileSize).getType() == Tile.BLOCKING){
//       		topLeft = true;
//       	}
//       	if (tileMap.getTile((int)x2, y1 / tileSize).getType() == Tile.BLOCKING){
//       		topRight = true;
//       	}
//       	if (tileMap.getTile(x1, (int)y2).getType() == Tile.BLOCKING){
//       		bottomLeft = true;
//       	}
//       	if (tileMap.getTile(x2, y2).getType() == Tile.BLOCKING){
//       		bottomRight = true;
//       	}
        
        //checks tiles for collision
        topLeft = tileMap.getTile(x1, y1).getType() == Tile.BLOCKING;
        topRight = tileMap.getTile(x2, y1).getType() == Tile.BLOCKING;
        bottomLeft = tileMap.getTile(x1, y2).getType() == Tile.BLOCKING;
        bottomRight = tileMap.getTile(x2, y2).getType() == Tile.BLOCKING;

        System.out.println(x1 + ", " +y1 + " type " + tileMap.getTile(x1, y1).getType() + " at " + dy + "speed");
        
        //handles collision
       	if (/*dy < 0 && */(topLeft || topRight)){
       		dy = 0;
       		ytemp = (ytemp / tileSize) * tileSize + tileSize;
       	} else if (/*dy > 0 && */(bottomLeft || bottomRight)){
       		dy = 0;
       		ytemp = (ytemp / tileSize) * tileSize;
       	}
       	if (/*dx < 0 && */(topLeft || bottomLeft)){
       		xtemp = (xtemp / tileSize) * tileSize;
       	} else if (/*dx > 0 && */(topRight || bottomRight)){
       		xtemp = (xtemp / tileSize) * tileSize + tileSize;
       	}

		ytemp += dy * .1;
		xtemp += dx;
       	
       	x = xtemp;
       	y = ytemp;
       	
//       	System.out.println("Position " + x + " " + y);
//		System.out.println("Velocity " + dx + " " + dy);
//		System.out.println();
	}
	
	public void update(){
		getNextPosition();
		animatino.update();
		//animation stuff
	}
	
	public void draw(Graphics2D g){
		BufferedImage temp = animatino.getImage();
		int halfWidth = GamePanel.WIDTH;
		g.drawImage(temp, (int)x * GamePanel.SCALE, (int)y * GamePanel.SCALE, null);
		//draw on the stuff, make sure you account for map position
	}


	public void keyPressed(int k){
		switch(k){
		case KeyEvent.VK_UP : 
			if (currentState != FALLING){
				currentState = JUMPING;
				dy = jumpStart;
				jumping = true;
				animatino.setFrames(spriteSheet[JUMPING], 10);
			}

			break;
		case KeyEvent.VK_DOWN : 
			if (currentState != JUMPING){
				currentState = BLOCKING;
				dy = 0;
				dx = 0;
				animatino.setFrames(spriteSheet[BLOCKING], 10);
			}

			break;
		case KeyEvent.VK_LEFT : 
			if(currentState == IDLE){
				currentState = WALKING;
				animatino.setFrames(spriteSheet[WALKING], 10);
			}
			right = false;
			dx = -1;
			break;
		case KeyEvent.VK_RIGHT : 
			if(currentState == IDLE){
				currentState = WALKING;
				animatino.setFrames(spriteSheet[WALKING], 10);
			}
			right = true;
			dx = 1;

			break;
		}
	}
	
	public void keyReleased(int k){
		switch(k){
		case KeyEvent.VK_UP : 
			jumping = false;
			if (currentState == FALLING) break;
			animatino.setFrames(spriteSheet[IDLE], 10);
			break;
		case KeyEvent.VK_DOWN : 
			currentState = IDLE;
			animatino.setFrames(spriteSheet[IDLE], 10);
			break;
		case KeyEvent.VK_LEFT : 
			currentState = IDLE;
			animatino.setFrames(spriteSheet[IDLE], 10);
			dx = 0;
			break;
		case KeyEvent.VK_RIGHT : 
			currentState = IDLE;
			animatino.setFrames(spriteSheet[IDLE], 20);
			dx = 0;
			break;
		}
	}
	
	public void takeDamage(int damage){
		if (currentState != BLOCKING){
			health -= damage;
			if (health < 0){
				die();
			}
		}
	}
	
	private void die(){
		//stuff stuff game over blah blah animation death
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
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide(Sprite sp, int code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return "aNIMATINO";
	}
	
}
