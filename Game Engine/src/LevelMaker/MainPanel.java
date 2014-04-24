package LevelMaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainPanel extends JPanel implements ActionListener{

	private JFileChooser fileChooser;
	
	private OptionsFrame optionsFrame;
	
	private static final FileNameExtensionFilter LVMK = new FileNameExtensionFilter("Level Maker Files (.lvmk)", "lvmk");
	private static final FileNameExtensionFilter IMAGES = new FileNameExtensionFilter("Images (.PNG, .JPG, .JPEG, .GIF, .BMP)", "jpg", "jpeg", "gif", "png", "bmp");
	
	private JButton options_button;
	private JButton openLevelButton;
	private JButton saveLevelButton;
	
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton leftButton;
	private JButton rightButton;
	private JButton upButton;
	private JButton downButton;
	private JButton seeEnemiesButton;
	
	private MapView map;
	
	public MainPanel(){
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);
		
		fileChooser = new JFileChooser(System.getProperty("user.home"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		map = new MapView();

		optionsFrame = new OptionsFrame(this);
		
		openLevelButton = new JButton("Open Level");
		saveLevelButton = new JButton("Save Level");
		options_button = new JButton("Options");
		
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		leftButton = new JButton("<");
		rightButton = new JButton(">");
		upButton = new JButton("^");
		downButton = new JButton("V");
		seeEnemiesButton = new JButton("See Enemies");

		openLevelButton.addActionListener(this);
		saveLevelButton.addActionListener(this);
		options_button.addActionListener(this);
		
		zoomInButton.addActionListener(this);
		zoomOutButton.addActionListener(this);
		leftButton.addActionListener(this);
		rightButton.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		seeEnemiesButton.addActionListener(this);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(openLevelButton)
								.addComponent(saveLevelButton)
								.addComponent(options_button)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(zoomInButton)
								.addComponent(zoomOutButton)
								.addComponent(leftButton)
								.addComponent(rightButton)
								.addComponent(upButton)
								.addComponent(downButton)
								.addComponent(seeEnemiesButton)
						)
						.addComponent(map)
				)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(openLevelButton)
						.addComponent(saveLevelButton)
						.addComponent(options_button)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(zoomInButton)
						.addComponent(zoomOutButton)
						.addComponent(leftButton)
						.addComponent(rightButton)
						.addComponent(upButton)
						.addComponent(downButton)
						.addComponent(seeEnemiesButton)
				)
				.addComponent(map)
		);
	}
	
	public void setLevel(LevelMakerData lvmk){
		map.load(lvmk);
		optionsFrame.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		fileChooser.resetChoosableFileFilters();
		fileChooser.setFileFilter(IMAGES);
		
		int return_val;
		
		if (e.getSource() == options_button){
			
			optionsFrame.pack();
			optionsFrame.setVisible(true);
			
		} else if (e.getSource() == openLevelButton){
			
			fileChooser.resetChoosableFileFilters();
			fileChooser.setFileFilter(LVMK);
			
			return_val = fileChooser.showOpenDialog(this);
			
			if (return_val == JFileChooser.APPROVE_OPTION){
				try{
					
					
					map.load(LevelMakerData.parse(fileChooser.getSelectedFile()));
					
				} catch (IOException exception){
					System.out.println("Error while loading file");
				}
			}
			
		} else if (e.getSource() == saveLevelButton){

			fileChooser.resetChoosableFileFilters();
			fileChooser.setFileFilter(LVMK);
			
			return_val = fileChooser.showSaveDialog(this);
			
			if (return_val == JFileChooser.APPROVE_OPTION){
				try{
					
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					path += ".lvmk";
					File file = new File(path);
					if (!file.exists()){
						file.createNewFile();
					}
					BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
					writer.write(map.getLevelData());
					writer.close();
				} catch(IOException exception){
					System.out.println("Error while writing to file");
				}
			}
			
		} else if (e.getSource() == zoomInButton){
			map.zoomIn();
		} else if (e.getSource() == zoomOutButton){
			map.zoomOut();
		} else if (e.getSource() == upButton){
			map.moveUp();
		} else if (e.getSource() == downButton){
			map.moveDown();
		} else if (e.getSource() == leftButton){
			map.moveLeft();
		} else if (e.getSource() == rightButton){
			map.moveRight();
		} else if (e.getSource() == seeEnemiesButton){
			map.toggleEnemyDisplay();
		}
	}
	
}
