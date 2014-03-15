package LevelMaker;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class LevelMakerData {

	private BufferedImage tile_map;
	
	private int[][] tile_types;
	
	private LevelMakerData(BufferedImage tm, int[][] tt){
		tile_map = tm;
		tile_types = tt;
	}
	
	public BufferedImage getTileMap(){ return tile_map; }
	public int[][] getTileTypes(){ return tile_types; }
	
	public static String getSaveableData(BufferedImage tile_map, int[][] tile_types){
		String contents = "";
		contents += String.format("%d %d\n", tile_map.getHeight(), tile_map.getWidth());
		for (int j = 0; j < tile_map.getHeight(); j++){
			for (int i = 0; i < tile_map.getWidth(); i++){
				contents += tile_map.getRGB(i, j) + " ";
			}
			contents += '\n';
		}
		contents += String.format("%d %d\n", tile_types.length, tile_types[0].length);
		for (int j = 0; j < tile_types.length; j++){
			for (int i = 0; i < tile_types[0].length; i++){
				contents += tile_types[j][i] + " ";
			}
			contents += '\n';
		}
		return contents;
	}
	
	public static LevelMakerData getLevelMakerData(BufferedImage tile_map, int[][] tile_types){
		return new LevelMakerData(tile_map, tile_types);
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
		return new LevelMakerData(tm, tt);
	}
	
}
