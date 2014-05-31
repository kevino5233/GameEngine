package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

import TileMap.TileMap;

class AudioListener implements LineListener{

	private boolean done = false;
	
	@Override
	public void update(LineEvent event) {
		Type eventType = event.getType();
		if (eventType == Type.STOP || eventType == Type.CLOSE){
			done = true;
			notifyAll();
		}
	}
	
	public synchronized void waitUntilDone() throws InterruptedException {
		while(!done){
			wait();
		}
	}
	
}

public abstract class Sprite{
	
	//check over stuff
	
	protected TileMap tileMap;
	protected int tileSize;
	
	protected double xmap, ymap;
	
	protected double x, y, dx, dy;
	
	protected boolean topLeft, topRight, bottomLeft, bottomRight;
	
	protected double terminalVelocity;
	 
	protected Animation animatino;
	protected int currentState;
	protected boolean right;
	
	protected int width, height;
	protected int health, maxHealth;
	
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;

    public int getX(){ return (int)x; }
    public int getY(){ return (int)y; }
    public int getHealth(){ return health; }
    public int getMaxHealth(){ return maxHealth; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height;} 
	public boolean isAscending(){ return dy < 0; }
	public boolean isDead(){ return health <= 0; }
	public Rectangle getHitbox(){ return new Rectangle(width, height, getX(), getY()); }
	
    public void takeDamage(int damage){
    	health -= damage;
		if (health <= 0){
			die();
		}
	}
	
    public abstract void spawn();
    public abstract void update();
    public abstract void die();
    public abstract void init();
    public abstract void draw(Graphics2D g);
    
    public abstract void collide(Rectangle rec, int code);
    
    public Sprite(TileMap tm){
    	tileMap = tm;
    	tileSize = tm.getTileSize();
    }
    
    public static int isTouching(Rectangle a, Rectangle b){
    	
        double x = a.getX();
        double x2 = x + a.getWidth();
        double y = a.getY();
        double y2 = y + a.getHeight();
        
        double i = b.getX();
        double i2 = i + b.getWidth();
        double j = b.getY();
        double j2 = j + b.getHeight();
        
        double i_x = Math.abs(i - x);
		double x_i2 = Math.abs(x - i2);
		double i_x2 = Math.abs(i - x2);
		double x2_i2 = Math.abs(x2 - i2);
        
        double j_y = Math.abs(j - y);
		double y_j2 = Math.abs(y - j2);
		double j_y2 = Math.abs(j - y2);
		double y2_j2 = Math.abs(y2 - j2);
		
        if (y <= j2 && y > j && ((i >= x && i2 <= x2) || (i2 > x && i < x) || (i < x2 && i2 > x2)) && (j_y > y_j2 && i_x <= x_i2 && i_x2 >= x2_i2)){
        	return UP;
		} else if (j < y2 && j2 > y2 && ((i >= x && i2 <= x2) || (i2 > x && i < x) || (i < x2 && i2 > x2)) && ( j_y2 < y2_j2)){
			return DOWN;
		} else if (i2 > x && i < x && ((j >= y && j2 <= y2) || (j2 > y && j < y) || (j < y2 && j2 > y2)) && ( i_x > x_i2)) {
			return LEFT;
		} else if (i > x && i < x2 && ((j >= y && j2 <= y2) || (j2 > y && j < y) || (j < y2 && j2 > y2)) && ( (i_x2 < x2_i2))){
			return RIGHT;
		}
        return -1;
    }
    
    //fix later, make more efficient
    
    public static int isTouchingVertically(Rectangle a, Rectangle b){
    	double x = a.getX();
        double x2 = x + a.getWidth();
        double y = a.getY();
        double y2 = y + a.getHeight();
        
        double i = b.getX();
        double i2 = i + b.getWidth();
        double j = b.getY();
        double j2 = j + b.getHeight();
        
        double i_x = Math.abs(i - x);
		double x_i2 = Math.abs(x - i2);
		double i_x2 = Math.abs(i - x2);
		double x2_i2 = Math.abs(x2 - i2);
        
        double j_y = Math.abs(j - y);
		double y_j2 = Math.abs(y - j2);
		double j_y2 = Math.abs(j - y2);
		double y2_j2 = Math.abs(y2 - j2);
		
		if (y <= j2 && y > j && ((i >= x && i2 <= x2) || (i2 > x && i < x) || (i < x2 && i2 > x2)) && (j_y > y_j2 && i_x <= x_i2 && i_x2 >= x2_i2)){
        	return UP;
		} else if (j < y2 && j2 > y2 && ((i >= x && i2 <= x2) || (i2 > x && i < x) || (i < x2 && i2 > x2)) && ( j_y2 < y2_j2)){
			return DOWN;
		} 
        return -1;
    }
    
    public static int isTouchingHorizontally(Rectangle a, Rectangle b){
    	
    	double x = a.getX();
        double x2 = x + a.getWidth();
        double y = a.getY();
        double y2 = y + a.getHeight();
        
        double i = b.getX();
        double i2 = i + b.getWidth();
        double j = b.getY();
        double j2 = j + b.getHeight();
        
        double i_x = Math.abs(i - x);
		double x_i2 = Math.abs(x - i2);
		double i_x2 = Math.abs(i - x2);
		double x2_i2 = Math.abs(x2 - i2);
        
        double j_y = Math.abs(j - y);
		double y_j2 = Math.abs(y - j2);
		double j_y2 = Math.abs(j - y2);
		double y2_j2 = Math.abs(y2 - j2);
    	
    	if (i2 > x && i < x && ((j >= y && j2 <= y2) || (j2 > y && j < y) || (j < y2 && j2 > y2)) && ( i_x > x_i2)) {
			return LEFT;
		} else if (i > x && i < x2 && ((j >= y && j2 <= y2) || (j2 > y && j < y) || (j < y2 && j2 > y2)) && ( (i_x2 < x2_i2))){
			return RIGHT;
		}
        return -1;
       
    }
    
    public static synchronized void playSound(final String string) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		    	  InputStream audioSrc = getClass().getResourceAsStream("/Resources/Sound/" + string + ".wav");
		    	  BufferedInputStream inputStream = new BufferedInputStream(audioSrc);
		    	  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

		    	  AudioListener listener = new AudioListener();

		    	  Clip clip = AudioSystem.getClip();
		    	  clip.addLineListener(listener);
		    	  clip.open(audioInputStream);
		    	  FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		    	  gainControl.setValue(-20.0f);		    	  
		    	  clip.start();

		    	  listener.waitUntilDone();	
		    	  	
		    	  clip.stop();
		    	  clip.close();
		    	  
		      } catch (Exception e) {
		    	  e.printStackTrace();
		      }
		    }
		  }).start();
    }
}


