package io.github.frcteam2984.simulator.ui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

/**
 * The frame to house all of the UI for the Drive Station
 * @author Max Apodaca
 *
 */
public class DriveStartionFrame extends JFrame {


	private static final long serialVersionUID = -3111702769758651663L;
	
	public DriveStartionFrame(){
		
		this.setTitle("Drive Station");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(300, 150);
		
		JPanel panel = new JPanel();
		this.add(panel);
		panel.setLayout(new GridLayout(0, 1));
		JPanel modes = new JPanel();
	    panel.add(modes);
	    JToggleButton enabled = new JToggleButton("Enabled");
	    enabled.setMnemonic(KeyEvent.VK_E);
	    enabled.setActionCommand("enable");
	    panel.add(enabled);

		
		JRadioButton teleOpButton = new JRadioButton("TeleOperated");
	    teleOpButton.setMnemonic(KeyEvent.VK_T);
	    teleOpButton.setActionCommand("TeleOp");
	    teleOpButton.setSelected(true);

	    JRadioButton autoButton = new JRadioButton("Autonomous");
	    autoButton.setMnemonic(KeyEvent.VK_A);
	    autoButton.setActionCommand("Auto");

	    JRadioButton practiceButton = new JRadioButton("Practice");
	    practiceButton.setMnemonic(KeyEvent.VK_P);
	    practiceButton.setActionCommand("Practice");

	    JRadioButton testButton = new JRadioButton("Test");
	    testButton.setMnemonic(KeyEvent.VK_S);
	    testButton.setActionCommand("Test");

	    ButtonGroup group = new ButtonGroup();
	    group.add(teleOpButton);
	    group.add(autoButton);
	    group.add(practiceButton);
	    group.add(testButton);

	    
	    modes.add(teleOpButton);
	    modes.add(autoButton);
	    modes.add(practiceButton);
	    modes.add(testButton);
		
	    
		this.setVisible(true);
		this.setLocationRelativeTo(null); 
		
	}
	
}
