package LevelMaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OptionsFrame extends JFrame implements ActionListener{
	
	private MainPanel parent;
	
	private JLabel x_label, y_label, path_label;
	
	private JTextField x_text_field, y_text_field;
	
	private JButton make_button, open_button, load_tile_map_button;
	
	private JFileChooser file_chooser;
	
	private BufferedImage tile_map;
	
	GroupLayout layout;
	
	public OptionsFrame(MainPanel parent){
		super("Options");
		this.parent = parent;
		JPanel panel = new JPanel();
		layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		file_chooser = new JFileChooser(System.getProperty("user.home"));
		file_chooser.setFileFilter(new FileNameExtensionFilter("Images (.PNG, .JPG, .JPEG, .GIF, .BMP)", "jpg", "jpeg", "gif", "png", "bmp"));
		
		x_label = new JLabel("Tiles horizontally across");
		x_label.setLabelFor(x_text_field);
		
		y_label = new JLabel("Tiles vertically across");
		y_label.setLabelFor(y_text_field);
		
		path_label = new JLabel("");
		
		x_text_field = new JTextField(10);
		
		y_text_field = new JTextField(10);
		
		make_button = new JButton("Make Level");
		make_button.addActionListener(this);
		
		open_button = new JButton("Open Existing Level");
		open_button.addActionListener(this);
		
		load_tile_map_button = new JButton("Upload Tile Map");
		load_tile_map_button.addActionListener(this);
		
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(x_label)
						.addComponent(y_label)
						.addComponent(load_tile_map_button)
						.addComponent(make_button)		
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(x_text_field)
						.addComponent(y_text_field)
						.addComponent(path_label)
						.addComponent(open_button)
				)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(x_label)
						.addComponent(x_text_field)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(y_label)
						.addComponent(y_text_field)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(load_tile_map_button)
						.addComponent(path_label)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(make_button)
						.addComponent(open_button)
				)
		);
		setResizable(false);
		setContentPane(panel);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		
		int return_val;
		
		if (source == load_tile_map_button){
			
			return_val = file_chooser.showOpenDialog(this);
			
			if (return_val == JFileChooser.APPROVE_OPTION){
				try {
					File f = file_chooser.getSelectedFile();
					tile_map = ImageIO.read(f);
					path_label.setText(f.getAbsolutePath());
					layout.replace(path_label, path_label);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			} 
		} else if (source == make_button){
			
			try{
				int[][] tile_types = new int[Integer.parseInt(y_text_field.getText())][Integer.parseInt(x_text_field.getText())];
				parent.setLevel(LevelMakerData.getLevelMakerData(tile_map, tile_types));
			} catch (NullPointerException e){
				System.out.println("You did not fill out all the fields!");
			} catch (NumberFormatException e){
				System.out.println("Only numbers in the x and y fields");
			}
			
		}
	}
	
}
