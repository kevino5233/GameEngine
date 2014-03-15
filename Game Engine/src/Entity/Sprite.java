package Entity;

public abstract class Sprite{
	
	//check over stuff
	
	protected TileMap tileMap;
	protected int tileSize;
	
	protected double xmap, ymap;
	
	protected double x, y, dx, dy;
	
	protected boolean topLeft, topRight, bottomLeft, bottomRight;
	
	protected double terminalVelocity;
	
	protected Animation animatino;
	protected int currentState;
	protected boolean right;
	
	protected int width, height;
	
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;

    public int getX(){ return (int)x; }
    public int getY(){ return (int)y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height;} 
    
    public abstract void init();
    public abstract void collide(Sprite sp, int code);
    
    public Sprite(TileMap tm){
    	tileMap = tm;
    	tileSize = tm.getTileSize();
    }
    
    public static int isTouching(Sprite a, Sprite b){
        int x = a.getX();
        int x2 = x + a.getWidth();
        int y = a.getY();
        int y2 = y + a.getHeight();
        
        int i = b.getX();
        int i2 = i + b.getWidth();
        int j = b.getY();
        int j2 = j + b.getHeight();
        
        int i_x = Math.abs(i - x);
		int x_i2 = Math.abs(x - i2);
		int i_x2 = Math.abs(i - x2);
		int x2_i2 = Math.abs(x2 - i2);
        
        int j_y = Math.abs(j - y);
		int y_j2 = Math.abs(y - j2);
		int j_y2 = Math.abs(j - y2);
		int y2_j2 = Math.abs(y2 - j2);
		
        if (j2 >= y && j < y && ((i >= x && i2 <= x2) || (i2 > x && i < x) || (i < x2 && i2 > x2)) && (j_y > y_j2 && i_x <= x_i2 && i_x2 >= x2_i2)){
			return UP;
		} else if (j < y2 && j2 > y2 && ((i >= x && i2 <= x2) || (i2 > x && i < x) || (i < x2 && i2 > x2)) && ( j_y2 < y2_j2)){
			return DOWN;
		} else if (i2 > x && i < x && ((j >= y && j2 <= y2) || (j2 > y && j < y) || (j < y2 && j2 > y2)) && ( i_x > x_i2)) {	
			return LEFT;
		} else if (i > x && i < x2 && ((j >= y && j2 <= y2) || (j2 > y && j < y) || (j < y2 && j2 > y2)) && ( (i_x2 < x2_i2))){
			return RIGHT;
		}
		return -1;
    }
}


