package GameState;

public class GameStateManager {

	private static GameState[] gameStates = new GameState[5];
	
	private int currentState;
	
	private static int difficulty = 0;
	
	public static final int MENUSTATE = 0;
	public static final int HUBSTATE= 1;
	public static final int FIRESTATE = 2;
	public static final int WATERSTATE = 3;
	public static final int EARTHSTATE = 4;
	public static final int AIRSTATE = 5;
	
	private static boolean fire,
						   water,
						   earth,
						   air;
	
	//add the other levelstates later
	
	public GameStateManager(){
		gameStates[MENUSTATE] = new MenuState(this);
		gameStates[HUBSTATE] = new HubState(this);
		gameStates[EARTHSTATE] = new EarthState(this);
		setState(0);
		//initialize menustate
	}
	
	public void setState(int state){
		currentState = state;
		gameStates[currentState].init();
	}
	
	public void complete(int state){
		difficulty++;
		switch(state){
		case FIRESTATE : fire = true;
		case WATERSTATE : water = true;
		case EARTHSTATE : earth = true;
		case AIRSTATE : air = true;
		}
	}
	
	public void update(){
		gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g){
		gameStates[currentState].draw(g);
	}
	

	public boolean getFire(){ return fire; }
	public boolean getWater(){ return water; }
	public boolean getEarth(){ return earth; }
	public boolean getAir(){ return air; }
	
	public int getDifficulty(){ return difficulty; }
	
	public void keyPressed(int k){
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k){
		gameStates[currentState].keyReleased(k);
	}
}
