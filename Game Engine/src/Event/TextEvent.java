package Event;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TextEvent {

	private BufferedImage box;
	
	private String message,
				   display, 
				   speaker;
	
	private boolean done;
	
	public TextEvent(String s, String m){
		message = m;
		display = "";
		speaker = s;
		done = false;
		
		try{
			box = ImageIO.read(new File("./Resources/Sprites/Objects/Textbox.png"));
		} catch (IOException e){
			System.out.println("Couldn't find the file");
		}
		
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
		
		g.drawImage(box, 50, 10, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString(speaker, 100, 40);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		String d = display;
		
		int drawX = 110;
		int drawY = 50;
		
		while(d.length() > 50){
			g.drawString(d.substring(0, 50), drawX, drawY);
			d = d.substring(50);
			drawY += 10;
		}
		g.drawString(d, drawX, drawY);
		if (done) {
			g.drawString("Press space to continue", GamePanel.WIDTH * GamePanel.SCALE - 250, 95);
		}
		
	}
	
	public void finish(){
		display = message;
		done = true;
	}
	
	public void reset(){
		display = "";
		done = false;
	}
	
	public String toString(){
		return speaker + "|" + message;
	}
}
