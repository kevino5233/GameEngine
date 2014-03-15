package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MenuState extends GameState{

	BufferedImage bg;
	
	private int currentChoice = 0;
	private String[] options = { "Start", "Help", "Quit" };
	
	private Color titleColor;
	private Font titleFont;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		
		try{
			bg = ImageIO.read(new File("./Resources/Backgrounds/background.png"));
			
			titleColor = new Color(0, 64, 128);
			
			titleFont = new Font("Comic Sans MS", Font.PLAIN, 28);
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("you done fucked up");
		}
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		g.drawImage(bg.getScaledInstance(480, 640, 0), 0, 0, null);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Cats in the orange tree", 100, 100);
		
		for (int i = 0; i < options.length; i++){
			if (i == currentChoice){
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 200, 100 + i * 15);
		}
		
	}

	@Override
	public void keyPressed(int k) {
		System.out.println("Doge");
		currentChoice++;
		currentChoice %= 3;
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}
	
}
