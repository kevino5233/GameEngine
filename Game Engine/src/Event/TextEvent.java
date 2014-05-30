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
	
	private int pos;
	
	private boolean done;
	
	public TextEvent(String s, String m){
		message = m;
		display = "";
		speaker = s;
		pos = 0;
		done = false;
		
		String word = "";
		String d = "";
		int words = 0;
		
		for (char c : message.toCharArray()){
			word += c;
			
			if (c == ' '){
				words++;
				if ((word + d).length() >= 50){
					display += d + "|" + word;
					words = 0;
					d = "";
				} else if (words >= 12){
					display += d + word + "|";
					words = 0;
					d = "";
				} else {
					d += word;
				}
				word = "";
			}
		}
		display += d + word;
		
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
			pos++;
			done = pos == message.length();
		}
	}
	
	public void draw(Graphics2D g){
		
		g.drawImage(box, 50, 10, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 10));
		g.drawString(speaker, 100, 40);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		
		int drawX = 110;
		int drawY = 50;
		
		String[] dat = display.split("[|]");
		
		String d = "";
		int x = 0;
		
		for (String s : dat){
			for (char c : s.toCharArray()){
				x++;
				d += c;
				if (x == pos) break;
			}
			g.drawString(d, drawX, drawY);
			drawY += 10;
			if (x == pos) break;
			else d = "";
		}
		
		if (done) {
			g.drawString("Press space to continue", GamePanel.WIDTH * GamePanel.SCALE - 250, 95);
		}
		
	}
	
	public void finish(){
		pos = display.length();
		done = true;
	}
	
	public void reset(){
		pos = 0;
		done = false;
	}
	
	public String toString(){
		return speaker + "|" + message;
	}
}
