package LevelMaker;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LevelMakerData {

	private BufferedImage tileMap;
	
	private int[][] tileTypes;
	
	private String[][] enemyData;
	
	private LevelMakerData(BufferedImage tm, int[][] tt, String[][] ed){
		tileMap = tm;
		tileTypes = tt;
		enemyData = ed;
	}
	
	public BufferedImage getTileMap(){ return tileMap; }
	public int[][] getTileTypes(){ return tileTypes; }
	public String[][] getEnemyData(){ return enemyData; }
	
	public static String getSaveableData(BufferedImage tileMap, int[][] tileTypes, String[][] enemyData){
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
					contents += enemyData[j][i];
				}
			}
			contents += '\n';
		}
		return contents;
	}
	
	public static LevelMakerData getLevelMakerData(BufferedImage tileMap, int cols, int rows){
		return new LevelMakerData(tileMap, new int[rows][cols], new String[rows][cols]);
	}
	
	public static LevelMakerData parse(File f)throws IOException{
		Scanner in = new Scanner(f);
		int rows = in.nextInt();
		int cols = in.nextInt();
		BufferedImage tm = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < rows; j++){
			for (int i = 0; i < cols; i++){
				tm.setRGB(i, j, in.nextInt());
			}
		}
		int[][] tt = new int[in.nextInt()][in.nextInt()];
		for (int j = 0; j < tt.length; j++){
			for (int i = 0; i < tt[0].length; i++){
				tt[j][i] = in.nextInt();
			}
		}
		String[][] ed = new String[tt.length][tt[0].length];
		for (int j = 0; j < tt.length; j++){
			for (int i = 0; i < tt[0].length; i++){
				ed[j][i] = in.next();
			}
		}
		in.close();
		return new LevelMakerData(tm, tt, ed);
	}
	
}
