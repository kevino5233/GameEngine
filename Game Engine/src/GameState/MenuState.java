package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

import Main.GamePanel;

public class MenuState extends GameState implements ActionListener{

	private int pos;
	private int dpos;
	private String[] options;
	
	private Font creditsFont, menuFont;
	
	private Timer timer;
	
	private boolean displayCredits;
	private boolean displayControls;
	
	private JLabel usernameLabel, passwordLabel, infoLabel, invalidUserLabel;
	
	private JTextField usernameTextField;
	
	private JPasswordField passwordTextField;
	
	private JButton submitButton;
	
	private JFrame passwordFrame;
	
	public MenuState(GameStateManager gsm){

		super(gsm);
		
		menuFont = new Font("Times New Roman", Font.PLAIN, 30);
		creditsFont = new Font("Times New Roman", Font.PLAIN, 15);
		
		timer = new Timer(500, this);
		
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		invalidUserLabel = new JLabel("");
		infoLabel = new JLabel("Your username will automatically be added if it hasn't yet");
		
		usernameTextField = new JTextField(30);
		passwordTextField = new JPasswordField(30);
		
		submitButton = new JButton("Sign in");
		submitButton.addActionListener(this);
		
		passwordFrame = new JFrame("Sign in");
		
		JPanel panel = new JPanel();
		
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		panel.setLayout(layout);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(infoLabel)
							.addComponent(usernameLabel)
							.addComponent(passwordLabel)
							.addComponent(invalidUserLabel)
							.addComponent(submitButton)
							)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(usernameTextField)
							.addComponent(passwordTextField)
							)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addComponent(infoLabel)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(usernameTextField)
							.addComponent(usernameLabel)
							)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(passwordTextField)
							.addComponent(passwordLabel)
							)
					.addComponent(invalidUserLabel)
					.addComponent(submitButton)
				);
		
		passwordFrame.setContentPane(panel);
		
		try{
			
			bg = ImageIO.read(
					this.getClass().getResourceAsStream("/Resources/Backgrounds/Background.png")
					);
					
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("you done fucked up");
		}
		
	}
	
	@Override
	public void init() {

		options = new String[1];
		options[0] = "Press space to start";
		
		pos = 0;
		dpos = 0;
		
		gsm.setUser("", "");
		
		usernameTextField.setText("");
		passwordTextField.setText("");
		
		displayCredits = false;
		displayControls = false;
		
		timer.setInitialDelay(0);
		
		playSound("theme");
		
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
		} else if (k == KeyEvent.VK_SPACE || k == KeyEvent.VK_ENTER){
			switch(options[pos]){
			case "Press space to start" :
				options = new String[3];
				options[0] = "Play";
				options[1] = "Credits";
				options[2] = "Quit";
				pos = 0;
				dpos = 0;
				break;
			case "Play" :
				passwordFrame.pack();
				passwordFrame.setVisible(true);
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
				gsm.setState(GameStateManager.HUBSTATE);
				break;
			}
		}
		
	}

	@Override
	public void keyReleased(int k) {
		timer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == submitButton){
			
			gsm.setUser(usernameTextField.getText(), String.valueOf(passwordTextField.getPassword()));

			usernameTextField.setText("");
			passwordTextField.setText("");
			
			try{
				
				gsm.reset();
				
				File saves = new File("Save Profiles");
				if (!saves.exists()){
					saves.mkdir();
				}
				File profile = new File( "Save Profiles/" + gsm.getUsername() + ".gg");
				if (!profile.exists()){
					profile.createNewFile();
				}
				
				Scanner in = new Scanner(profile);

				if (in.hasNext()){
					String tempUsername = in.next();
					String tempPassword = in.next();
					char[] levels = in.next().toCharArray();
					
					if (tempUsername.equals(gsm.getUsername()) && tempPassword.equals(gsm.getPassword())){
						if (levels[0] == '1') gsm.complete(GameStateManager.AIRSTATE);
						if (levels[1] == '1') gsm.complete(GameStateManager.FIRESTATE);
						if (levels[2] == '1') gsm.complete(GameStateManager.WATERSTATE);
						if (levels[3] == '1') gsm.complete(GameStateManager.EARTHSTATE);
						
						passwordFrame.setVisible(false);
						
						options = new String[2];
						options[0] = "Okay";
						options[1] = "Back";
						displayControls = true;
						pos = 0;
						
					} else {
						invalidUserLabel.setText("Invalid username or password");
					}
					
					in.close();
					
					return;

				}

				
				FileWriter fileWriter = new FileWriter(profile);
				BufferedWriter writer = new BufferedWriter(fileWriter);
				
				writer.append(gsm.getUsername() + " " + gsm.getPassword() + " 0000");
				
				writer.close();
				in.close();
				
				passwordFrame.setVisible(false);
				
				options = new String[2];
				options[0] = "Okay";
				options[1] = "Back";
				displayControls = true;
				pos = 0;
				
				return;
				
			} catch (FileNotFoundException e){
				System.out.println("Could not find file goddamnit MenuState");
				e.printStackTrace();
			} catch (IOException e){
				System.out.println("Could not make file or something");
				e.printStackTrace();
			} catch (Exception e){
				System.out.println("Some other error");
				e.printStackTrace();
			}
			
			
		} else {
			pos += dpos;
			if (pos == options.length){
				pos = options.length - 1;
			} else if (pos < 0){
				pos = 0;
			}
		}
	}
	
}
