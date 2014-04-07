package Entity;

import java.awt.image.BufferedImage;

public class Animation{

	private int pos;
	private int framesWaited;
	private int framesDelay;
	
	private boolean playedOnce;
	
	private BufferedImage[] frames;
	
	public Animation(){
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames, int delay){
		pos = 0;
		this.frames = frames;
		framesWaited = 0;
		framesDelay = delay;
		playedOnce = false;
	}
	
	public void update(){
		if (framesWaited++ == framesDelay){
			framesWaited = 0;
			pos++;
			if (pos == frames.length) playedOnce = true;
			pos %= frames.length;
		}
	}
	
	public void setFrame(int i){
		pos = i % frames.length;
	}
	
	public int getFrame(){ return pos; }
	public BufferedImage getImage(){ return frames[pos]; }
	public boolean hasPlayedOnce(){ return playedOnce; }
	
}
