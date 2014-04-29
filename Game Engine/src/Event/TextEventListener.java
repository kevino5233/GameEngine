package Event;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class TextEventListener {

	private HashMap<String, TextEvent> textEventMap;
	
	private TextEvent eventPlaying;
	
	private boolean isEventPlaying;
	
	public TextEventListener(){
		
		textEventMap = new HashMap<>();
		
	}
	
	public void add(String s){
		
		String[] dat = s.split("|");
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
		} else {
			eventPlaying = null;
			isEventPlaying = false;
		}
	}
	
	public boolean isPlaying(){ return isEventPlaying; }
	
	public void updateMessage(){
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
				isEventPlaying = false;
				textEventMap.remove(eventPlaying);
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