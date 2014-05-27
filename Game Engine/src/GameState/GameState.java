package GameState;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;

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

public abstract class GameState {

	protected GameStateManager gsm;
	
	protected boolean paused;
	
	protected PauseState pauseMenu;
	
	protected BufferedImage amulet, earthPendant, firePendant, waterPendant, airPendant;
	
	private static Thread sound;
	private static Clip clip;
	
	//initializes variables and stuff
	public abstract void init();
	
	//updates the level
	public abstract void update();
	
	//draws to some graphics2D object
	public abstract void draw(java.awt.Graphics2D g);
	
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
	public void pause(){ paused = true; }
	public void unpause(){ paused = false; }
	
	public static synchronized void playSound(final String string) {
		  sound = new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		    	  InputStream audioSrc = getClass().getResourceAsStream("/Resources/Sound/Music/" + string + ".wav");
		    	  BufferedInputStream inputStream = new BufferedInputStream(audioSrc);
		    	  AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

		    	  AudioListener listener = new AudioListener();

		    	  clip = AudioSystem.getClip();
		    	  //clip.addLineListener(listener);
		    	  clip.open(audioInputStream);
		    	  clip.loop(Clip.LOOP_CONTINUOUSLY);

		    	  clip.start();

		      } catch (Exception e) {
		    	  e.printStackTrace();
		      }
		    }
		  });
		  sound.start();
	}
	
	public static synchronized boolean isPlaying(){
		if (clip != null){
			return clip.isActive();
		}
		return false;
	}
	
	public static synchronized void stopSound(){
  	  if (clip != null){
  		  clip.stop();
    	  clip.close();
  	  }
	}
	
}
