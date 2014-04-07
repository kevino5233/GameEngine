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
import javax.sound.sampled.Clip;
import javax.swing.Timer;

import Main.GamePanel;

public class MenuState extends GameState implements ActionListener{

	BufferedImage bg;
	
	private int pos = 0;
	private int dpos;
	private String[] options = { "Start", "Help", "Quit" };
	
	private Color titleColor;
	private Font titleFont;
	
	private Timer timer;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		
		
	}
	
	@Override
	public void init() {
		try{
			bg = ImageIO.read(new File("./Resources/Backgrounds/background.png"));
			
			titleColor = new Color(0, 64, 128);
			
			titleFont = new Font("Comic Sans MS", Font.PLAIN, 28);
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("you done fucked up");
		}
		
		timer = new Timer(500, this);
		timer.setInitialDelay(0);
		
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		g.drawImage(bg.getScaledInstance(GamePanel.WIDTH * GamePanel.SCALE, GamePanel.HEIGHT * GamePanel.SCALE, 0), 
					0, 
					0, 
					null
					);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Cats in the orange tree", 100, 100);
		
		for (int i = 0; i < options.length; i++){
			if (i == pos){
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 200, 100 + i * 15);
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
			switch(pos){
			case 0 : gsm.setState(1); break;
			case 1 : break; //open the thing
			case 2 : System.exit(0);
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
