package Event;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Main.GamePanel;

public class TextEvent {

	private String message,
				   display, 
				   speaker;
	
	private boolean done;
	
	public TextEvent(String m, String s){
		message = m;
		display = "";
		speaker = s;
		done = false;
	}
	
	public String getMessage(){ return message; }
	public String getSpeaker(){ return speaker; }
	public boolean isDone(){ return done; }
	
	public void update(){
		if (!done){
			display += message.charAt(display.length());
			done = message.length() == display.length();
		}
	}
	
	public void draw(Graphics2D g){
		
		g.setColor(new Color(0x66ccff));
		g.fillRect(50, 10, GamePanel.WIDTH * GamePanel.SCALE - 100, 50);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString(speaker, 55, 20);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.drawString(display, 60, 30);
		if (done) {
			g.drawString("Press space to continue", GamePanel.WIDTH * GamePanel.SCALE - 175, 50);
		}
		
	}
	
	public void finish(){
		display = message;
		done = true;
	}
	
	public void reset(){
		display = "";
	}
	
	public String toString(){
		return speaker + "|" + message;
	}
}
