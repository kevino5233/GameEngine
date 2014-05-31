package GameState;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Entity.Player;

public class GameStateManager {

	private static GameState[] gameStates = new GameState[7];
	
	private String username, password;
	
	private int currentState;
	
	private static int difficulty = 0;
	
	public static final int MENUSTATE = 0;
	public static final int HUBSTATE= 1;
	public static final int FIRESTATE = 2;
	public static final int WATERSTATE = 3;
	public static final int EARTHSTATE = 4;
	public static final int AIRSTATE = 5;
	public static final int ENDSTATE = 6;
	
	private static boolean fire,
						   water,
						   earth,
						   air;
	
	//add the other levelstates later
	
	public GameStateManager(){
		gameStates[MENUSTATE] = new MenuState(this);
		gameStates[HUBSTATE] = new HubState(this);
		gameStates[FIRESTATE] = new FireState(this);
		gameStates[EARTHSTATE] = new EarthState(this);
		gameStates[WATERSTATE] = new WaterState(this);
		gameStates[AIRSTATE] = new AirState(this);
		gameStates[ENDSTATE] = new EndState(this);
		username = "";
		password = "";
		
		setState(0);
		//initialize menustate
	}
	
	public void setState(int state){
		GameState.stopSound();
		if (fire && water && earth && air){
			currentState = ENDSTATE;
			gameStates[ENDSTATE].init();
		} else {
			currentState = state;
			gameStates[currentState].init();
		}
	}
	
	public void setUser(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){ return username; }
	public String getPassword(){ return password; }
	
	public void complete(int state){
		difficulty++;
		switch(state){
		case FIRESTATE : fire = true; Player.setFire(true); break;
		case WATERSTATE : water = true; break;
		case EARTHSTATE : earth = true; break;
		case AIRSTATE : air = true; Player.setAir(air);break;
		}
		try{
			File profile = new File("Save Profiles/" + getUsername() + ".gg");
			FileWriter fileWriter = new FileWriter(profile);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			
			String levels = "";
			levels += air ? '1' : '0';
			levels += fire ? '1' : '0';
			levels += water ? '1' : '0';
			levels += earth ? '1' : '0';
			
			writer.write(getUsername() + " " + getPassword() + " " + levels);
			
			writer.close();
			
		} catch (IOException e){
			System.out.println("Trouble getting shit done GSM");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("Some other error GSM");
			e.printStackTrace();
		}
	}
	
	public void reset(){
		fire = water = earth = air = false;
		Player.setFire(false);
		Player.setAir(false);
	}
	
	public void update(){
		gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g){
		gameStates[currentState].draw(g);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
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
