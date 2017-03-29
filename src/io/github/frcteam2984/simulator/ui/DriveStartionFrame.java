package io.github.frcteam2984.simulator.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * The frame to house all of the UI for the Drive Station
 * @author Max Apodaca
 *
 */
public class DriveStartionFrame extends JFrame implements Observer, ActionListener{


	private static final long serialVersionUID = -3111702769758651663L;
	
	private DriverStation driverStartion;
	
	private JToggleButton enabled;
	private JRadioButton teleOpButton;
	private JRadioButton autoButton;
	private JRadioButton testButton;
	
	public DriveStartionFrame(DriverStation ds){
		ds.addObserver(this);
		this.driverStartion = ds;
		this.setTitle("Drive Station");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(300, 150);
		
		JPanel panel = new JPanel();
		this.add(panel);
		panel.setLayout(new GridLayout(0, 1));
		JPanel modes = new JPanel();
	    panel.add(modes);
	    enabled = new JToggleButton("Enabled");
	    enabled.setMnemonic(KeyEvent.VK_E);
	    enabled.setActionCommand("enable");
	    panel.add(enabled);

		
		teleOpButton = new JRadioButton("TeleOperated");
	    teleOpButton.setMnemonic(KeyEvent.VK_T);
	    teleOpButton.setActionCommand("TeleOp");
	    teleOpButton.setSelected(true);

	    autoButton = new JRadioButton("Autonomous");
	    autoButton.setMnemonic(KeyEvent.VK_A);
	    autoButton.setActionCommand("Auto");

//	    JRadioButton practiceButton = new JRadioButton("Practice");
//	    practiceButton.setMnemonic(KeyEvent.VK_P);
//	    practiceButton.setActionCommand("Practice");

	    testButton = new JRadioButton("Test");
	    testButton.setMnemonic(KeyEvent.VK_S);
	    testButton.setActionCommand("Test");

	    ButtonGroup group = new ButtonGroup();
	    group.add(teleOpButton);
	    group.add(autoButton);
//	    group.add(practiceButton);
	    group.add(testButton);
	    
	    teleOpButton.addActionListener(this);
	    autoButton.addActionListener(this);
	    testButton.addActionListener(this);
	    enabled.addActionListener(this);


	    
	    modes.add(teleOpButton);
	    modes.add(autoButton);
//	    modes.add(practiceButton);
	    modes.add(testButton);
		
	    
		this.setVisible(true);
		this.setLocationRelativeTo(null); 
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.enabled.isSelected()){
			this.driverStartion.InAutonomous(false);
			this.driverStartion.InOperatorControl(false);
			this.driverStartion.InTest(false);
			this.driverStartion.InDisabled(true);
		} else {
			this.driverStartion.InAutonomous(this.autoButton.isSelected());
			this.driverStartion.InOperatorControl(this.teleOpButton.isSelected());
			this.driverStartion.InTest(this.testButton.isSelected());
			this.driverStartion.InDisabled(false);
		}
	}
	
}
