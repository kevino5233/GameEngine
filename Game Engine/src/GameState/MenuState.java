package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import Main.GamePanel;

public class MenuState extends GameState implements ActionListener{

	BufferedImage bg;
	
	private int pos;
	private int dpos;
	private String[] options;
	
	private Font creditsFont, menuFont;
	
	private Timer timer;
	
	private int nextLevel;
	
	private boolean displayCredits;
	private boolean displayControls;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;	
		
		menuFont = new Font("Times New Roman", Font.PLAIN, 30);
		creditsFont = new Font("Times New Roman", Font.PLAIN, 15);
		
		timer = new Timer(500, this);
		try{
			
			bg = ImageIO.read(new File("./Resources/Backgrounds/background.png"));
			
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("you done fucked up");
		}
		
	}
	
	@Override
	public void init() {

		options = new String[3];
		options[0] = "Play";
		options[1] = "Credits";
		options[2] = "Quit";
		
		pos = 0;
		dpos = 0;
		
		playSound(new File("./Resources/Sound/Music/hub.wav"));
		
		displayCredits = false;
		displayControls = false;
		
		timer.setInitialDelay(0);
		
		nextLevel = 0;
		
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setFont(menuFont);
		g.drawImage(bg.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0), 
					0, 
					0, 
					null
					);
		
		for (int i = 0; i < options.length; i++){
			if (i == pos){
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 200, 200 + i * 40);
		}
		
		if (displayCredits){
			
			int draw_x = 25;
			int draw_y = 210;
			int y_diff = 20;
			int tab = 10;
			
			g.setFont(creditsFont);
			g.setColor(Color.BLACK);
			
			g.drawString("Game Director", draw_x, draw_y);
			g.drawString("Chris Raff", draw_x + tab, draw_y + y_diff);
			
			g.drawString("Lead Programmer", draw_x,  draw_y + y_diff * 2);
			g.drawString("Kevin Sun", draw_x + tab,  draw_y + y_diff * 3);
			
			g.drawString("Artists", draw_x,  draw_y + y_diff * 4);
			g.drawString("Adam Davis, Garisson Grogan, Chris Raff, Saif Quraishi", draw_x + tab,  draw_y + y_diff * 5);
			
			g.drawString("Composer", draw_x,  draw_y + y_diff * 6);
			g.drawString("Aaron Peterson", draw_x + tab,  draw_y + y_diff * 7);
			
		}
		if (displayControls){
			
			int drawX = 25;
			int drawY = 210;
			int yDiff = 20;
					
			g.setFont(creditsFont);
			g.setColor(Color.BLACK);
			
			g.drawString("Attack - Z", drawX, drawY);
			g.drawString("Move - Arrow Keys", drawX, drawY + yDiff);
			g.drawString("Jump - Up Arrow Key", drawX, drawY + yDiff * 2);
			g.drawString("Action Button - Space", drawX, drawY + yDiff * 3);
			g.drawString("Pause - Escape", drawX, drawY + yDiff * 4);
			
		}
		
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_UP){
			dpos = -1;
			timer.start();
		} else if (k == KeyEvent.VK_DOWN){
			dpos = 1;
			timer.start();
		} else if (k == KeyEvent.VK_ENTER){
			switch(options[pos]){
			case "Play" : 
				options = new String[3];
				options[0] = "Earth Level";
				options[1] = "Fire Level";
				options[2] = "Back";
				displayCredits = false;
				displayControls = false;
				pos = 0;
				break;
			case "Credits" : 
				options = new String[1];
				options[0] = "Back";
				displayCredits = true;
				pos = 0;
				break; //open the thing
			case "Quit" : 
				System.exit(0);
			case "Back" :
				options = new String[3];
				options[0] = "Play";
				options[1] = "Credits";
				options[2] = "Quit";
				displayCredits = false;
				displayControls = false;
				pos = 0;
				break;
			case "Okay" :
				stopSound();
				gsm.setState(nextLevel);
				break;
			case "Fire level" :
//				nextLevel = GameStateManager.FIRESTATE;
//				options = new String[2];
//				options[0] = "Okay";
//				options[1] = "Back";
//				displayCredits = false;
//				displayControls = true;
//				pos = 0;
				break;
			case "Earth Level" :
				nextLevel = GameStateManager.EARTHSTATE;
				options = new String[2];
				options[0] = "Okay";
				options[1] = "Back";
				displayCredits = false;
				displayControls = true;
				pos = 0;
				break;
			}
		}
		
	}

	@Override
	public void keyReleased(int k) {
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		pos += dpos;
		if (pos == options.length){
			pos = options.length - 1;
		} else if (pos < 0){
			pos = 0;
		}
	}
	
}
