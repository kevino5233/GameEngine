package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Shark extends Enemy{

	private int framesUntilRespawn, framesToRespawn,
				framesUntilJump, framesToJump;
	
	private int currentState;
	
	private static final int SPAWN = 0;
	private static final int WIGGLE = 1;
	private static final int JUMP = 2;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames = {6, 10, 14};
	
	public Shark(TileMap tm) {
		super(tm);
		
		active = false;
		
		height = width = 32;
		
		framesToJump = 90;
		framesToRespawn = 90;
		
		terminalVelocity = 2;
		
		spriteSheet = new BufferedImage[3][];
		
		animatino = new Animation();
		
		try{
			BufferedImage temp = ImageIO.read(
						getClass().getResourceAsStream("/Resources/Sprites/Enemies/shark.png")
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
			System.out.println("Only found dolphins");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("RIP Sharks");
			e.printStackTrace();
		}
		
	}

	@Override
	public void spawn() {
		active = true;
		
		currentState = SPAWN;
		animatino.setFrames(spriteSheet[SPAWN], 3, true);
		
		framesUntilJump = 0;
		framesUntilRespawn = 0;
		
		x = GamePanel.WIDTH / 2 + tileMap.getX();
		y = GamePanel.HEIGHT / 2 + tileMap.getY() + 20;
		
	}

	@Override
	public void getNextPosition(Player sp) {
		if (currentState != SPAWN){
			if (currentState == JUMP){
				if (animatino.hasPlayedOnce()){
					y += dy;
				}
				int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height),
	  					 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
	 					);
				if (code != -1){
					sp.takeDamage(3);
				}

			} else {
				int deltaX = getX() - sp.getX();
				int deltaY = getY() - sp.getY();
				
				if (deltaX != 0){
					if (deltaX < 0){
						right = true;
						dx = terminalVelocity;
					} else {
						right = false;
						dx = -terminalVelocity;
					}
				} else {
					dx = 0;
				}
				
				if (deltaY != 0){
					if (deltaY < 0){
						right = true;
						dy = terminalVelocity;
					} else {
						right = false;
						dy = -terminalVelocity;
					}
				} else {
					dy = 0;
				}
				
				x += dx;
				y += dy;
				
				if (y < GamePanel.HEIGHT / 2){
					y = GamePanel.HEIGHT / 2;
				}
			}
		}
	}

	@Override
	public void update() {
		switch (currentState){
		case SPAWN : 
			if (animatino.hasPlayedOnce()){
				currentState = WIGGLE;
				animatino.setFrames(spriteSheet[WIGGLE], 3, false);
			}
			break;
		case WIGGLE :
			if (framesUntilJump++ == framesToJump){
				framesUntilJump = 0;
				currentState = JUMP;
				animatino.setFrames(spriteSheet[JUMP], 3, true);
			}
			break;
		case JUMP :
			if (animatino.hasPlayedOnce()){
				dy = 10;
				if (getY() >= GamePanel.HEIGHT && framesUntilRespawn++ == framesToRespawn){
					
					framesUntilRespawn = 0;
					currentState = SPAWN;
					animatino.setFrames(spriteSheet[SPAWN], 3, true);
					
					x = GamePanel.WIDTH / 2 + tileMap.getX();
					y = GamePanel.HEIGHT / 2 + tileMap.getY();
					
				}
			}
		}
		animatino.update();
	}
	
	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void collide(Rectangle rec, int code) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isFalling(){ return dy > 0; }

}
