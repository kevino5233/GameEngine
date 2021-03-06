package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import GameState.GameStateManager;

public class GamePanel extends JPanel implements Runnable, KeyListener{

	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	private Thread thread;
	private boolean running;
	public static int frames;
	public static long timeElapsed;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private GameStateManager gsm;
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		image = new BufferedImage(WIDTH * SCALE, HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D)image.getGraphics();
		requestFocus();
		running = true;
		gsm = new GameStateManager();
		addKeyListener(this);
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run(){
		try{
			while(running){
				long start = System.currentTimeMillis();
				gsm.update();
				gsm.draw(g);
				drawToScreen();
				
				
				long delay = 33 - (System.currentTimeMillis() - start);
				if (delay < 0) delay = 0;
				Thread.sleep(delay);
				
				long finish = System.currentTimeMillis();
				
				frames++;
				timeElapsed += finish - start;
			}
		} catch (Exception e){
			System.out.println("AW SHIT SON");
			e.printStackTrace();
		}
	}
	
	public static double getFPS(){
		return 1.0 * frames / timeElapsed * 1000;
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0,
				WIDTH * SCALE, HEIGHT * SCALE,
				null);
		g2.dispose();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e.getKeyCode());
	}
	
}
