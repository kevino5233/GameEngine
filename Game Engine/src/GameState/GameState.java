package GameState;

public abstract class GameState {

	protected GameStateManager gsm;
	
	protected boolean paused;
	
	//initializes variables and stuff
	public abstract void init();
	
	//updates the level
	public abstract void update();
	
	//draws to some graphics2D object
	public abstract void draw(java.awt.Graphics2D g);
	
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}
