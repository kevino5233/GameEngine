package Entity;

import java.awt.image.BufferedImage;

public class Animation{

	private int pos;
	private int framesWaited;
	private int framesDelay;
	private boolean playOnce, stopped;
	
	private BufferedImage[] frames;
	
	public Animation(){
		playOnce = false;
		stopped = true;
		pos = 0;
	}
	
	public void setFrames(BufferedImage[] frames, int delay, boolean po){
		pos = 0;
		this.frames = frames;
		framesWaited = 0;
		framesDelay = delay;
		stopped = false;
		playOnce = po;
	}
	
	public void update(){
		if (!stopped && framesWaited++ == framesDelay){
			framesWaited = 0;
			pos++;
			if (pos == frames.length) {
				if (playOnce){
					pos = frames.length - 1;
					stopped = true;
				} else {
					pos %= frames.length;
				}
			}
		}
	}
	
	public void setFrame(int i){
		pos = i % frames.length;
	}
	
	public void stop(){ stopped = true; }
	
	public int getFrame(){ return pos; }
	public BufferedImage getImage(){ return frames[pos]; }
	public boolean hasPlayedOnce(){ return pos + 1 == frames.length; }
	public boolean stopped(){ return stopped; }
	
}
