package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.color.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Event.TextEvent;
import Event.TextEventListener;
import Main.GamePanel;
import TileMap.TileMap;

public class EndState extends GameState{
	
	private int numFramesBlack, numFramesBlacked, framesDelay, framesDelayed;
	
	private BufferedImage bg,
							dgyeresteroff, 
							waterGod,
							fireGod,
							earthGod,
							airGod,
							illuminati;
	
	private ArrayList<TextEvent> finalTextEvents;
	
	private TextEventListener textEventListener;
	
	public EndState(GameStateManager gsm){
		
		super(gsm);
		
		numFramesBlacked = 0;
		
		numFramesBlack = 60;
		
		textEventListener = new TextEventListener();
		
		finalTextEvents = new ArrayList<>();
		
		try{
			bg = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Backgrounds/hub.png")
					);
			waterGod = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/waterGod.png")
					);
			airGod = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/airGod.png")
					);
			earthGod = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/earthGod.png")
					);
			fireGod = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/fireGod.png")
					);
			dgyeresteroff = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/dgyeresteroff.png")
					);
			illuminati = ImageIO.read(
					getClass().getResourceAsStream("/Resources/Sprites/Objects/GrassionsShittySprite.png")
					);
		} catch (IOException e){
			System.out.println("A ruined ending...");
			e.printStackTrace();
		} catch (Exception e){
			System.out.println("A ruined ending indeed.");
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		framesDelay = 30;
		framesDelayed = 0;
		finalTextEvents.clear();
		finalTextEvents.add(
				new TextEvent(
						"Fire God",
						"I thank you for finding my shrubbery. "
						+ "In the process of embracing them,"
						+ "I found my marbles too!"
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Air God",
						"My home is no longer as wasted as it once was. I am glad you came."
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Water God",
						"Those holy knick knacks are invaluable to me. "
						+ "Thank you securing their return to me."
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Earth God",
						"You did slay ALL the goat right?"
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Dgyeresteroff",
						"There is no need for thanks, "
						+ "it is only right that I come to the aid of "
						+ "my spiritual watchers in their time of need."
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Earth God",
						"I concur."
						+ "Never the less, we are grateful for your sacrifices. "
						+ "Is there something you may wish for? "
						+ "Perhaps it is in our power to grant it."
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Dgyeresteroff",
						"The famines around have been causing much suffering among my people. "
						+ "Please bless my lands so that it may leave us."
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Fire God",
						"It will be done. If you will excuse me however, I need to retrieve my plants."
						+ "They help...harness...my powers."
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Water God",
						"That must be one hell of a plant"
						)
				);
		finalTextEvents.add(
				new TextEvent(
						"Dgyeresteroff",
						"Wait, what's that?"
						)
				);
		playSound("credits");
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (!textEventListener.isPlaying()){
			if (finalTextEvents.size() > 0){
				textEventListener.playMessage(finalTextEvents.remove(0));
			} else if (framesDelay > 0){
				if (isPlaying()) stopSound();
				framesDelayed++;
			} else {
				numFramesBlacked++;
			}
		} else {
			textEventListener.update();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		BufferedImage frame = new BufferedImage(
				GamePanel.WIDTH, 
				GamePanel.HEIGHT, 
				BufferedImage.TYPE_INT_RGB
				);
		Graphics2D frameGraphics = (Graphics2D)frame.getGraphics();
		
		if (framesDelay > 0){
			
			frameGraphics.drawImage(bg, 0, 0, null);
			frameGraphics.drawImage(fireGod, TileMap.tileSize, 11 * TileMap.tileSize, null);
			frameGraphics.drawImage(earthGod, 5 * TileMap.tileSize, 11 * TileMap.tileSize, null);
			frameGraphics.drawImage(dgyeresteroff, 10 * TileMap.tileSize, 12 * TileMap.tileSize, null);
			frameGraphics.drawImage(waterGod, 14 * TileMap.tileSize, 11 * TileMap.tileSize, null);
			frameGraphics.drawImage(airGod, 5 * TileMap.tileSize, 6 * TileMap.tileSize, null);
			if (finalTextEvents.size() == 0){
				if (framesDelayed >= framesDelay){
					framesDelayed = 0;
					framesDelay *= 2;
					framesDelay /= 3;
					frameGraphics.drawImage(
							illuminati,
							GamePanel.WIDTH / 2,
							GamePanel.HEIGHT / 2,
							null
							);
				} 
				
			}
		} else {
			if (numFramesBlacked >= numFramesBlack){
				frameGraphics.setFont(new Font("Arial", Font.BOLD, 20));
				frameGraphics.setColor(Color.WHITE);
				frameGraphics.drawString(
						"The End?",
						GamePanel.WIDTH / 2 - 100, 
						GamePanel.HEIGHT / 2
						);
			}
		}
		
		g.drawImage(
				frame.getScaledInstance(
						GamePanel.WIDTH * GamePanel.SCALE, 
						GamePanel.HEIGHT * GamePanel.SCALE, 
						0
						),
				0,
				0,
				null
				);
		if (textEventListener.isPlaying()){
			textEventListener.draw(g);
		}
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		if (k == KeyEvent.VK_SPACE){
			if(textEventListener.isPlaying()){
				textEventListener.keyPressed(k);
			}
		}

	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	
	
}
