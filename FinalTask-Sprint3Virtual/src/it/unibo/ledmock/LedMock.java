package it.unibo.ledmock;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LedMock implements ILed{
	private final JFrame frame  = new JFrame();
	private JPanel panel;
	private boolean ledState = false;
	public LedMock(){
		this.frame.setTitle("Led");
		frame.setSize(200,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("LED Test");
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
			
		// declare a panel
		this.panel = new JPanel(true);
		this.panel.setSize(200,200);
		this.panel.setVisible(true);
		this.panel.setBackground(Color.GRAY);	
		frame.add(this.panel);	
	}
	
	@Override
	public void turnOn() {
		this.ledState = true;
		this.panel.setBackground(Color.RED);
	}

	@Override
	public void turnOff() {
		this.ledState = false;
		this.panel.setBackground(Color.GRAY);
	}

	@Override
	public void switchState() {
		this.ledState = !this.ledState;
		if(this.ledState)this.turnOn(); else this.turnOff();
	}
}
