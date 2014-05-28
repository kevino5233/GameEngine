package LevelMaker;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import Event.TextEvent;

public class LevelMakerData {

	private BufferedImage tileMap;
	
	private int[][] tileTypes;
	
	private String[][] enemyData;
	
	private ArrayList<String> events;
	
	private LevelMakerData(BufferedImage tm, int[][] tt, String[][] ed, ArrayList<String> ev){
		tileMap = tm;
		tileTypes = tt;
		enemyData = ed;
		events = ev;
	}
	
	public BufferedImage getTileMap(){ return tileMap; }
	public int[][] getTileTypes(){ return tileTypes; }
	public String[][] getEnemyData(){ return enemyData; }
	public ArrayList<String> getEvents(){ return events; }
	
	public static String getSaveableData(BufferedImage tileMap, int[][] tileTypes, String[][] enemyData, TextEvent[][] textEvents){
		String contents = "";
		contents += String.format("%d %d\n", tileMap.getHeight(), tileMap.getWidth());
		for (int j = 0; j < tileMap.getHeight(); j++){
			for (int i = 0; i < tileMap.getWidth(); i++){
				contents += tileMap.getRGB(i, j) + " ";
			}
			contents += '\n';
		}
		contents += String.format("%d %d\n", tileTypes.length, tileTypes[0].length);
		for (int j = 0; j < tileTypes.length; j++){
			for (int i = 0; i < tileTypes[0].length; i++){
				contents += tileTypes[j][i] + " ";
			}
			contents += '\n';
		}
		for (int j = 0; j < enemyData.length; j++){
			for (int i = 0; i < enemyData[0].length; i++){
				if (enemyData[j][i] == null){
					contents += "None ";
				} else {
					contents += enemyData[j][i] + " ";
				}
			}
			contents += '\n';
		}
		for (int j = 0; j < textEvents.length; j++){
			for (int i = 0; i < textEvents[0].length; i++){
				if (textEvents[j][i] != null){
					contents += i + "|" + j + "|" + textEvents[j][i] + "\n";
				}
			}
		}
		return contents;
	}
	
	public static LevelMakerData getLevelMakerData(BufferedImage tileMap, int cols, int rows){
		return new LevelMakerData(tileMap, new int[rows][cols], new String[rows][cols], new ArrayList<String>());
	}
	
	public static LevelMakerData parse(String levelName)throws IOException{
		
		/*
		 * Switch to InputStreamReader
		 * parse until a space, then parse that String
		 */
		
		InputStream in = Main.class.getResourceAsStream("/Resources/Levels/" + levelName + ".lvmk");		
		BufferedReader level = 
				new BufferedReader(
					new InputStreamReader(in)
				);
		String[] dat = level.readLine().split(" ");
		int rows = Integer.parseInt(dat[0]);
		int cols = Integer.parseInt(dat[1]);
		BufferedImage tm = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < rows; j++){
			dat = level.readLine().split(" ");
			for (int i = 0; i < cols; i++){
				tm.setRGB(i, j, Integer.parseInt(dat[i]));
			}
		}
		
		dat = level.readLine().split(" ");
		rows = Integer.parseInt(dat[0]);
		cols = Integer.parseInt(dat[1]);
		
		int[][] tt = new int[rows][cols];
		for (int j = 0; j < tt.length; j++){
			dat = level.readLine().split(" ");
			for (int i = 0; i < tt[0].length; i++){
				tt[j][i] = Integer.parseInt(dat[i]);
			}
		}
		String[][] ed = new String[tt.length][tt[0].length];
		for (int j = 0; j < tt.length; j++){
			dat = level.readLine().split(" ");
			for (int i = 0; i < tt[0].length; i++){
				ed[j][i] = dat[i];
			}
		}
		int num = 0;
		try{
			num = Integer.parseInt(level.readLine());
		} catch (Exception e){}
		ArrayList<String> ev = new ArrayList<String>();
		while(num-- > 0){
			ev.add(level.readLine());
		}
		level.close();
		return new LevelMakerData(tm, tt, ed, ev);
	}
	
}
