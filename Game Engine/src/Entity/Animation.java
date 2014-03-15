package Entity;

import java.awt.image.BufferedImage;

public class Animation{

	private int pos;
	
	private boolean playedOnce;
	
	private BufferedImage[] frames;
	
	public Animation(){
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames){
		pos = 0;
		this.frames = frames;
		playedOnce = false;
	}
	
	public void update(){
		pos++;
		if (pos == frames.length) playedOnce = true;
		pos %= frames.length;
	}
	
	public void setFrame(int i){
		pos = i % frames.length;
	}
	
	public int getFrame(){ return pos; }
	public BufferedImage getImage(){ return frames[pos]; }
	public boolean hasPlayedOnce(){ return playedOnce; }
	
}
