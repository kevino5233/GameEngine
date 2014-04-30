package LevelMaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EnemyPalettePanel extends JPanel implements ActionListener{
	
	JFrame frame;
	
	private JButton playerSpawnButton;
	private JButton noneButton;
	
	private JButton[][] enemyButtons;
	
	private MapView mapView;
	
	private int x, y;
	
	public EnemyPalettePanel(MapView parent, JFrame frame){
		mapView = parent;
		
		this.frame = frame;
		
		playerSpawnButton = new JButton("Player");
		playerSpawnButton.addActionListener(this);
		
		noneButton = new JButton("None");
		noneButton .addActionListener(this);
		
		enemyButtons = new JButton[2][];
		
		enemyButtons[0] = new JButton[5];
		enemyButtons[0][0] = new JButton("Bat");
		enemyButtons[0][1] = new JButton("Guppy");
		enemyButtons[0][2] = new JButton("Slug");
		enemyButtons[0][3] = new JButton("FireElemental");
		enemyButtons[0][4] = new JButton("Spike");
		
		enemyButtons[1] = new JButton[4];
		enemyButtons[1][0] = new JButton("Bat");
		enemyButtons[1][1] = new JButton("Goat");
		enemyButtons[1][2] = new JButton("Cyclops");
		enemyButtons[1][3] = new JButton("Spike");
		
		for (JButton[] list : enemyButtons){
			for (JButton b : list){
				b.addActionListener(this);
			}
		}
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);
		
		JLabel fireLabel = new JLabel("Fire Enemies");
		JLabel earthLabel = new JLabel("Earth Enemies");
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(playerSpawnButton)
					.addComponent(fireLabel)
					.addComponent(enemyButtons[0][0])
					.addComponent(earthLabel)
					.addComponent(enemyButtons[1][0])
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(noneButton)
					.addComponent(enemyButtons[0][1])
					.addComponent(enemyButtons[1][1])
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(enemyButtons[0][2])
					.addComponent(enemyButtons[1][2])
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(enemyButtons[0][3])
					.addComponent(enemyButtons[1][3])
				)
				.addComponent(enemyButtons[0][4])
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(playerSpawnButton)
					.addComponent(noneButton)
				)
				.addComponent(fireLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(enemyButtons[0][0])
					.addComponent(enemyButtons[0][1])
					.addComponent(enemyButtons[0][2])
					.addComponent(enemyButtons[0][3])
					.addComponent(enemyButtons[0][4])
				)
				.addComponent(earthLabel)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(enemyButtons[1][0])
					.addComponent(enemyButtons[1][1])
					.addComponent(enemyButtons[1][2])
					.addComponent(enemyButtons[1][3])
				)
		);
		
	}
	
	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() instanceof JButton){
			JButton b = (JButton)ae.getSource();
			mapView.addEnemy(b.getText(), x, y);
			frame.setVisible(false);
		}
	}
	
}
