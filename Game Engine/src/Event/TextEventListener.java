package Event;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;

import TileMap.TileMap;

public class TextEventListener {

	private HashMap<String, TextEvent> textEventMap;
	
	private String key;
	
	private TextEvent eventPlaying;
	
	private boolean isEventPlaying;
	
	public TextEventListener(){
		
		textEventMap = new HashMap<>();
		
	}
	
	public void add(String s){
		
		String[] dat = s.split("[|]");
		if (dat.length < 4) return;
		String coord = dat[0] + ", " + dat[1];
		
		String speaker = dat[2];
		String message = dat[3];
		TextEvent temp = new TextEvent(speaker, message);
		
		textEventMap.put(coord, temp);
		
	}
	
	public void playMessage(int x, int y){
		
		String coord = x + ", " + y;
		TextEvent temp = textEventMap.get(coord);
		if (temp != null){
			eventPlaying = temp;
			isEventPlaying = true;
			key = coord;
		} else {
			eventPlaying = null;
			isEventPlaying = false;
		}
	}
	
	public void playMessage(TextEvent event){
		
		eventPlaying = event;
		isEventPlaying = true;
		
	}
	
	public boolean isPlaying(){ return isEventPlaying; }
	public boolean isDone(){
		if (isEventPlaying && eventPlaying != null){
			return eventPlaying.isDone();
		}
		return false;
	}
	
	public void update(){
		if (isEventPlaying){
			eventPlaying.update(); 
		}
	}
	
	public void draw(Graphics2D g){ 
		if (isEventPlaying){
			eventPlaying.draw(g);
		}
	}
	
	public void keyPressed(int keyCode){
		if (keyCode == KeyEvent.VK_SPACE && eventPlaying != null){
			if (eventPlaying.isDone()){
				textEventMap.remove(key);
				isEventPlaying = false;
			} else {
				eventPlaying.finish();
			}
		}
	}
	
	public void flush(){
		//remove all events
		eventPlaying = null;
		isEventPlaying = false;
	}
	
}