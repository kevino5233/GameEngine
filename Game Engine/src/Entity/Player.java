package Entity;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Player extends Sprite{
	
	private int health, maxHealth;
	
	private int framesInvincible;
	
	private static final double GRAVITY = 1.0;
	private double jumpStart, stopJumpSpeed;
	
	private boolean jumping;
	
	BufferedImage[][] spriteSheet;
	
	private final int[] numFrames = {2, 8, 1, 2};
	
	private static int IDLE = 0;
	private static int WALKING = 1;
	private static int JUMPING = 2;
	private static int ATTACKING = 3;
	private static int BLOCKING = 4;
	private static int FLINCHING = 5;
	private static int EARTHMOVE = 6;
	/*might not exist */private static int AIRMOVE = 7;
	
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
		
		spriteSheet = new BufferedImage[7][];
		
		try{
			BufferedImage temp = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/player" + (fire ? "-fire" : "") + ".png"));
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
		animatino.setFrames(spriteSheet[IDLE]);
		
	}
	
	public void getNextPosition(){
		
		double xtemp = x;
		double ytemp = y;
		
		xtemp += dx;
		
		dy -= GRAVITY;
		if (dy > terminalVelocity) dy = terminalVelocity;
		ytemp += dy * .1;
		
		if (dy < 0 && !jumping) dy += stopJumpSpeed;
        int x2 = (int) (xtemp + width) / tileSize;
        int y2 = (int) (ytemp + height) / tileSize;
       	if (tm.getTile(xtemp / tileSize, ytemp / tileSize).getType == Tile.BLOCKING){
       		topLeft = true;
       	}
       	if (tm.getTile(x2, ytemp / tileSize).getType == Tile.BLOCKING){
       		topRight = true;
       	}
       	if (tm.getTile(xtemp, y2).getType == Tile.BLOCKING){
       		bottomLeft = true;
       	}
       	if (tm.getTile(x2, y2).getTYpe == Tile.BLOCKING){
       		bottomRight = true;
       	}
       	if (dy < 0 && (topLeft || topRight)){
       		dy = 0;
       		ytemp = (ytemp / tileSize) * tileSize + tileSize;
       	} else if (dy > 0 && (bottomLeft || bottomRight)){
       		dy = 0;
       		currentState = IDLE;
       		ytemp = (ytemp / tileSize) * tileSize;
       	}
       	if (dx < 0 && (topLeft || bottomLeft)){
       		xtemp = (xtemp / tileSize) * tileSize;
       	} else if (dy > 0 && (topRight || bottomRight)){
       		xtemp = (xtemp / tileSize) * tileSize + tileSize;
       	}
       	x = xtemp;
       	y = ytemp;
	}
	
	public void update(){
		getNextPosition();
		
		//animation stuff
	}
	
	public void draw(Graphics2D g){
		//draw on the stuff, make sure you account for map position
	}


	public void keyPressed(int k){
		switch(k){
		case KeyEvent.VK_UP : currentState = JUMPING;
							  dy = jumpStart;
							  jumping = true;
							  break;
		case KeyEvent.VK_DOWN : if (currentState != JUMPING) currentState = BLOCKING;
							    dy = 0;
							    dx = 0;
							    break;
		case KeyEvent.VK_LEFT : if(currentState == IDLE) currentState = WALKING;
								right = false;
								dx = -1;
								break;
		case KeyEvent.VK_RIGHT : if(currentState == IDLE) currentState = WALKING;
								 right = true;
								 dx = 1;
								 break;
		}
	}
	
	public void keyReleased(int k){
		switch(k){
		case KeyEvent.VK_UP : jumping = false;
							  break;
		case KeyEvent.VK_DOWN : currentState = IDLE;
							    break;
		case KeyEvent.VK_LEFT : currentState = IDLE;
								dx = 0;
								break;
		case KeyEvent.VK_RIGHT : currentState = IDLE;
								 dx = 0;
								 break;
		}
	}
	
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
