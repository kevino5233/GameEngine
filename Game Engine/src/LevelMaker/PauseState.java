package LevelMaker;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import GameState.GameState;
import GameState.GameStateManager;

public class PauseState extends GameState implements ActionListener{
	
	BufferedImage bg;
	
	private int pos, dpos;
	
	private String[] options = {"Resume", "Controls", "Quit"};
	
	private Timer timer;
	
	public PauseState(GameStateManager gsm){
		this.gsm = gsm;
		
		timer = new Timer(500, this);
		
		//get the pause image
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
		pos = 0;
		dpos = 0;
		
		timer.setInitialDelay(0);
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
