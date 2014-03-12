package GameState;

public class GameStateManager {

	private static GameState[] gameStates = new GameState[2];
	
	private int currentState;
	
	private static int difficulty = 0;
	
	private static final int MENUSTATE = 0;
	private static final int HUBSTATE= 1;
	
	private static boolean fire,
						   water,
						   earth,
						   air;
	
	//add the other levelstates later
	
	public GameStateManager(){
		currentState = MENUSTATE;
		gameState[0] = new MenuState(this);
		gameState[1] = new HubState(this);
		//initialize menustate
	}
	
	public void setState(int state){
		currentState = state;
		gameStates[currentState].init();
	}
	
	public void update(){
		gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g){
		gameStates[currentState].draw(g);
	}
	
	public void keyPressed(int k){
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k){
		gameStates[currentState].keyPressed(k);
	}
}
