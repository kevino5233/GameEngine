package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;
import TileMap.TileMap;

public class Fist extends Enemy{
	
	private static final int IDLE = 0;
	private static final int ATTACKING = 1;
	private static final int RETREATING = 2;
	
	private Animation animatino;
	
	private int framesRest, framesRested;
	
	private BufferedImage[][] spriteSheet;
	private int[] numFrames= {5, 5, 5};
	
	public Fist(TileMap tm, double x, double y){
		
		super (tm, x, y);
		
		width = 16;
		height = 32;
		
		currentState = IDLE;
		
		active = false;	
		
		animatino = new Animation();
		
		spriteSheet = new BufferedImage[3][5];
		
		framesRest = 60;
		framesRested = 60;
		
		try{
			BufferedImage temp = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Enemies/fist.png")
					);
			for (int j = 0; j < spriteSheet.length; j++){
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
			System.out.println("COuldn't find picture");
		} catch (Exception e){
			System.out.println("Some other error");
		}
	}
	
	@Override
	public void getNextPosition(Player sp) {
		
		if (currentState == IDLE){
			double x2 = sp.getX();
			double y2 = sp.getY();
			
			double tempX = (x2 - x) * (x2 - x);
			double tempY = (y2 - y) * (y2 - y);
			
			double distance = Math.sqrt(tempX + tempY);
			
			if (distance <= 3 * TileMap.tileSize){
				if (framesRest == framesRested){
					System.out.println("GG");
					currentState = ATTACKING;
					animatino.setFrames(spriteSheet[ATTACKING], 2, true);
				} else {
					framesRested++;
				}
				
			}
			
			
		} else if (currentState == ATTACKING){
			
			int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, 8 * animatino.getFrame()),
					 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
				);
			
			if (code != -1){
				sp.takeDamage(3);
			}
			
		} else if (currentState == RETREATING){
			
			int code = Sprite.isTouching(new Rectangle((int)x, (int)y, width, height - 8 * animatino.getFrame()),
					 new Rectangle(sp.getX(), sp.getY(), sp.getWidth(), sp.getHeight())
				);
			if (code != -1){
				sp.takeDamage(3);
			}
			
			framesRested = 0;
			
		}
		
	}

	@Override
	public void spawn() {
		active = true;
		animatino.setFrames(spriteSheet[IDLE], 2, true);
		System.out.println("Get ready to get rekt");
	}

	@Override
	public void update() {
		if (animatino.hasPlayedOnce()){
			switch(currentState){
			case ATTACKING :
				currentState = RETREATING;
				animatino.setFrames(spriteSheet[RETREATING], 2, true);
				break;
			case RETREATING :
				currentState = IDLE;
				animatino.setFrames(spriteSheet[IDLE], 2, true);
			}
		} else {
			animatino.update();
		}
	}

	@Override
	public void die() {
		active = false;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public void collide(Rectangle rec, int code) {
		
	}

}
