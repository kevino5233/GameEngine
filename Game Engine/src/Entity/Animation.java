package Entity;

import java.awt.image.BufferedImage;

public class Animation{

	private int pos;
	private int framesWaited;
	private int framesDelay;
	
	private boolean playedOnce, stopped;
	
	private BufferedImage[] frames;
	
	public Animation(){
		playedOnce = false;
		stopped = true;
		pos = 0;
	}
	
	public void setFrames(BufferedImage[] frames, int delay){
		pos = 0;
		this.frames = frames;
		framesWaited = 0;
		framesDelay = delay;
		stopped = false;
		playedOnce = false;
	}
	
	public void update(){
		if (!stopped && framesWaited++ == framesDelay){
			framesWaited = 0;
			pos++;
			if (pos == frames.length) playedOnce = true;
			pos %= frames.length;
		}
	}
	
	public void setFrame(int i){
		pos = i % frames.length;
	}
	
	public void stop(){ stopped = true; }
	
	public int getFrame(){ return pos; }
	public BufferedImage getImage(){ return frames[pos]; }
	public boolean hasPlayedOnce(){ return playedOnce; }
	public boolean stopped(){ return stopped; }
	
}
