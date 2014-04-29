package LevelMaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class InsertFrame extends JFrame implements ActionListener{

	private int x, y;
	
	private TextEventPalettePanel textEventPalettePanel;
	private JButton textEventPaletteButton;
	private JFrame textEventFrame;
	
	private EnemyPalettePanel enemyPalettePanel;
	private JButton enemyPaletteButton;
	private JFrame enemyFrame;
	
	private TilePalettePanel tilePalettePanel;
	private JButton tilePaletteButton;
	private JFrame tileFrame;
	
	public InsertFrame(MapView parent){
		textEventFrame = new JFrame();
		enemyFrame = new JFrame();
		tileFrame = new JFrame();
		
		textEventPalettePanel = new TextEventPalettePanel(parent, textEventFrame);
		enemyPalettePanel = new EnemyPalettePanel(parent, enemyFrame);
		tilePalettePanel = new TilePalettePanel(parent, tileFrame);
		
		textEventFrame.setResizable(false);
		textEventFrame.setContentPane(textEventPalettePanel);
		
		enemyFrame.setResizable(false);
		enemyFrame.setContentPane(enemyPalettePanel);
		enemyFrame.pack();
		
		tileFrame.setResizable(false);
		tileFrame.setContentPane(tilePalettePanel);
		
		textEventPaletteButton = new JButton("Text Event");
		textEventPaletteButton.addActionListener(this);
		
		enemyPaletteButton = new JButton("Enemy");
		enemyPaletteButton.addActionListener(this);
		
		tilePaletteButton = new JButton("Tile");
		tilePaletteButton.addActionListener(this);
		
		JPanel panel = new JPanel();
		
		GroupLayout layout = new GroupLayout(panel);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		panel.setLayout(layout);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(textEventPaletteButton)
				.addComponent(enemyPaletteButton)
				.addComponent(tilePaletteButton)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(textEventPaletteButton)
				.addComponent(enemyPaletteButton)
				.addComponent(tilePaletteButton)
		);
		
		this.setContentPane(panel);
		
	}
	
	public void setMap(BufferedImage tileMap, int resolution){
		tilePalettePanel.setMap(tileMap, 16);
	}
	
	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton temp = (JButton)e.getSource();
		if (temp == textEventPaletteButton){
			textEventPalettePanel.setCoordinates(x, y);
			textEventFrame.pack();
			textEventFrame.setVisible(true);
			this.setVisible(false);
		} else if (temp == enemyPaletteButton){
			enemyPalettePanel.setCoordinates(x, y);
			enemyFrame.pack();
			enemyFrame.setVisible(true);
			this.setVisible(false);
		} else if (temp == tilePaletteButton){
			tilePalettePanel.setCoordinates(x, y);
			tileFrame.pack();
			tileFrame.setVisible(true);
			this.setVisible(false);
		}
	}
	
}
