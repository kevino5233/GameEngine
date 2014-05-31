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

public class PauseState implements ActionListener{

	private BufferedImage background;
	
	private GameStateManager gsm;
	
	private GameState gs;
	
	private int pos, dpos;
	private int x, y, width, height;
	
	private String[] options;
	
	private Color textColor, selectedColor;
	
	private Font textFont, infoFont;
	
	private boolean displayControls;
	
	private Timer timer;
	
	public PauseState(GameState parent, GameStateManager gsm){
		
		gs = parent;
		
		this.gsm = gsm;
		
		textColor = Color.RED;
		selectedColor = Color.WHITE;
		
		timer = new Timer(500, this);
		
		textFont = new Font("Times New Roman", Font.PLAIN, 30);
		infoFont = new Font("Times New Roman", Font.PLAIN, 10);
		
		try{
			background = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Sprites/Objects/menu.png")
					);
			
		} catch (IOException e){
			System.out.println("Couldn't find file");
		}
	}
	
	
	public void init() {
		// TODO Auto-generated method stub
		pos = 0;
		dpos = 0;
		
		width = 120;
		height= 120;
		
		options = new String[3];
		options[0] = "Resume";
		options[1] = "Help";
		options[2] = "Quit";
		
		timer.setInitialDelay(0);
		
		x = (GamePanel.WIDTH - width) / 2 - 25;
		y = (GamePanel.HEIGHT - height) / 2- 25;
	}

	
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setFont(textFont);
		g.drawImage(background, x * GamePanel.SCALE, y * GamePanel.SCALE, null);
		if (options[0].equals("Yes")){
			g.setColor(textColor);
			g.drawString("Really quit?", this.x * GamePanel.SCALE + 100, this.y * GamePanel.SCALE + 75);
			g.setFont(infoFont);
			g.drawString("Progress in this level will NOT be saved", this.x * GamePanel.SCALE + 100, this.y * GamePanel.SCALE + 175);
		}
		for (int x = 0; x < options.length; x++){
			g.setFont(textFont);
			if (x == pos){
				g.setColor(selectedColor);
			} else {
				g.setColor(textColor);
			}
			g.drawString(options[x], 
						 this.x * GamePanel.SCALE + 100, 
						 this.y * GamePanel.SCALE + 100 + 40 * x
						 );
		}
		if (displayControls){
			int drawX = 250;
			int drawY = 200;
			int yDiff = 20;
					
			g.setFont(new Font("Times New Roman", Font.PLAIN, 15));
			g.setColor(Color.BLACK);
			
			g.drawString("Attack - Z", drawX, drawY);
			g.drawString("Move - Arrow Keys", drawX, drawY + yDiff);
			g.drawString("Jump - Up Arrow Key", drawX, drawY + yDiff * 2);
			g.drawString("Action Button - Space", drawX, drawY + yDiff * 3);
			g.drawString("Pause - Escape", drawX, drawY + yDiff * 4);
		}
	}

	
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		if (k == KeyEvent.VK_UP){
			dpos = -1;
			timer.start();
		} else if (k == KeyEvent.VK_DOWN){
			dpos = 1;
			timer.start();
		} else if (k == KeyEvent.VK_SPACE || k == KeyEvent.VK_ENTER){
			switch(options[pos]){
			case "Resume" : 
				gs.unpause(); 
				break;
			case "Help" : 
				options = new String[1];
				options[0] = "Back";
				displayControls = true;
				pos = 0;
				break; //open the thing
			case "Yes" :
				GameState.stopSound();
				gsm.setState(GameStateManager.MENUSTATE);
				break;
			case "No" :
			case "Back" :
				options = new String[3];
				options[0] = "Resume";
				options[1] = "Help";
				options[2] = "Quit";
				displayControls = false;
				pos = 0;
				break;
			case "Quit" : 
				options = new String[2];
				options[0] = "Yes";
				options[1] = "No";
				pos = 0;
			}
		} else if (k == KeyEvent.VK_ESCAPE){
			gs.unpause();
		}
	}

	
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		pos += dpos;
		if (pos == options.length){
			pos = options.length - 1;
		} else if (pos < 0){
			pos = 0;
		}
	}

}
