package LevelMaker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Event.TextEvent;

public class TextEventPalettePanel extends JPanel implements ActionListener{

	private int x, y;
	
	private MapView parent;
	
	private JLabel speakerLabel;
	private JLabel messageLabel;
	private JTextField speakerTextField;
	private JTextArea messageTextArea;
	private JButton makeButton;
	
	private JFrame frame;
	
	public TextEventPalettePanel(MapView parent, JFrame frame){
		requestFocusInWindow();
		this.parent = parent;
		this.frame = frame;
		
		speakerLabel = new JLabel("Speaker: ");
		messageLabel = new JLabel("Message: ");
		
		speakerTextField = new JTextField(20);
		
		messageTextArea = new JTextArea(10, 20);
		
		makeButton = new JButton("Make");
		makeButton.addActionListener(this);
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		setLayout(layout);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(speakerLabel)
				.addComponent(speakerTextField)
				.addComponent(messageLabel)
				.addComponent(messageTextArea)
				.addComponent(makeButton)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(speakerLabel)
				.addComponent(speakerTextField)
				.addComponent(messageLabel)
				.addComponent(messageTextArea)
				.addComponent(makeButton)
		);
	}
	
	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
		TextEvent temp = parent.getTextEvent(x, y);
		if (temp != null){
			speakerTextField.setText(temp.getSpeaker());
			messageTextArea.setText(temp.getMessage());
		} else {
			speakerTextField.setText("");
			messageTextArea.setText("");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == makeButton){
			parent.addTextEvent(x, y, speakerTextField.getText(), messageTextArea.getText());
			frame.setVisible(false);
		}
	}
	
}
