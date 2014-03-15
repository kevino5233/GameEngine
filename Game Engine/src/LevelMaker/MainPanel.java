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

	private JFileChooser file_chooser;
	
	private OptionsFrame options_frame;
	
	private static final FileNameExtensionFilter LVMK = new FileNameExtensionFilter("Level Maker Files (.lvmk)", "lvmk");
	private static final FileNameExtensionFilter IMAGES = new FileNameExtensionFilter("Images (.PNG, .JPG, .JPEG, .GIF, .BMP)", "jpg", "jpeg", "gif", "png", "bmp");
	
	private JButton options_button;
	private JButton open_level_button;
	private JButton save_level_button;
	
	private JButton zoom_in_button;
	private JButton zoom_out_button;
	private JButton left_button;
	private JButton right_button;
	private JButton up_button;
	private JButton down_button;
	
	private MapView map;
	
	public MainPanel(){
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);
		
		file_chooser = new JFileChooser(System.getProperty("user.home"));
		file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		map = new MapView();

		options_frame = new OptionsFrame(this);
		
		open_level_button = new JButton("Open Level");
		save_level_button = new JButton("Save Level");
		options_button = new JButton("Options");
		
		zoom_in_button = new JButton("+");
		zoom_out_button = new JButton("-");
		left_button = new JButton("<");
		right_button = new JButton(">");
		up_button = new JButton("^");
		down_button = new JButton("V");

		open_level_button.addActionListener(this);
		save_level_button.addActionListener(this);
		options_button.addActionListener(this);
		
		zoom_in_button.addActionListener(this);
		zoom_out_button.addActionListener(this);
		left_button.addActionListener(this);
		right_button.addActionListener(this);
		up_button.addActionListener(this);
		down_button.addActionListener(this);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addGroup(layout.createSequentialGroup()
								.addComponent(open_level_button)
								.addComponent(save_level_button)
								.addComponent(options_button)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(zoom_in_button)
								.addComponent(zoom_out_button)
								.addComponent(left_button)
								.addComponent(right_button)
								.addComponent(up_button)
								.addComponent(down_button)
						)
						.addComponent(map)
				)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(open_level_button)
						.addComponent(save_level_button)
						.addComponent(options_button)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(zoom_in_button)
						.addComponent(zoom_out_button)
						.addComponent(left_button)
						.addComponent(right_button)
						.addComponent(up_button)
						.addComponent(down_button)
				)
				.addComponent(map)
		);
	}
	
	public void setLevel(LevelMakerData lvmk){
		map.load(lvmk);
		options_frame.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		file_chooser.resetChoosableFileFilters();
		file_chooser.setFileFilter(IMAGES);
		
		int return_val;
		
		if (e.getSource() == options_button){
			
			options_frame.pack();
			options_frame.setVisible(true);
			
		} else if (e.getSource() == open_level_button){
			
			file_chooser.resetChoosableFileFilters();
			file_chooser.setFileFilter(LVMK);
			
			return_val = file_chooser.showOpenDialog(this);
			
			if (return_val == JFileChooser.APPROVE_OPTION){
				try{
					
					
					map.load(LevelMakerData.parse(file_chooser.getSelectedFile()));
					
				} catch (IOException exception){
					System.out.println("Error while loading file");
				}
			}
			
		} else if (e.getSource() == save_level_button){

			file_chooser.resetChoosableFileFilters();
			file_chooser.setFileFilter(LVMK);
			
			return_val = file_chooser.showSaveDialog(this);
			
			if (return_val == JFileChooser.APPROVE_OPTION){
				try{
					
					String path = file_chooser.getSelectedFile().getAbsolutePath();
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
			
		} else if (e.getSource() == zoom_in_button){
			map.zoomIn();
		} else if (e.getSource() == zoom_out_button){
			map.zoomOut();
		} else if (e.getSource() == up_button){
			map.moveUp();
		} else if (e.getSource() == down_button){
			map.moveDown();
		} else if (e.getSource() == left_button){
			map.moveLeft();
		} else if (e.getSource() == right_button){
			map.moveRight();
		}
	}
	
}
