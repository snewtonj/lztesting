package com.janrain.steven.lztesting.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.janrain.steven.lztesting.ElectionHandler;

public class LeaderElectorFrame extends JFrame implements ActionListener {
	
	private JButton closeButton;
	private JButton leaderButton;
	private ElectionHandler electionHandler;
	
	public LeaderElectorFrame(String man) throws HeadlessException {
		super(man);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		pane.add(closeButton, BorderLayout.CENTER);
		leaderButton = new JButton("Relinquish");
		leaderButton.addActionListener(this);
		pane.add(leaderButton, BorderLayout.CENTER);
	}

	public LeaderElectorFrame(ElectionHandler eh) {
		this("Leader Election Testing");
		this.electionHandler = eh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent evt) {
		if ("Relinquish".equals(evt.getActionCommand())) {
			electionHandler.stopProcessing();
		}
		if ("Close".equals(evt.getActionCommand())) {
			electionHandler.stopProcessing();
			WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		}
		
	}

}
