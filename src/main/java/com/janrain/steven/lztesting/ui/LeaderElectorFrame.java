package com.janrain.steven.lztesting.ui;

import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.janrain.steven.lztesting.ElectionHandler;

public class LeaderElectorFrame extends JFrame implements ActionListener, PropertyChangeListener {
	
	private JButton closeButton;
	private JButton leaderButton;
	private ElectionHandler electionHandler;
	private JButton requeueButton;
	private JLabel statusLabel;
	private static final Logger logger = Logger.getLogger(LeaderElectorFrame.class);
	
	public LeaderElectorFrame(String man) throws HeadlessException {
		super(man);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		pane.add(closeButton);
		leaderButton = new JButton("Relinquish");
		leaderButton.setEnabled(false);
		leaderButton.addActionListener(this);
		pane.add(leaderButton);
		requeueButton = new JButton("Requeue");
		requeueButton.setEnabled(false);
		requeueButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				requeueButton.setEnabled(!electionHandler.requeue());
			}
		});
		pane.add(requeueButton);
		statusLabel = new JLabel("Starting");
		pane.add(statusLabel);
	}

	public LeaderElectorFrame(ElectionHandler eh, String clientId) {
		this("Leader Election Testing ["+clientId+"]");
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
			electionHandler.shutdown();
			WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
		}
		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("Got property change event "+evt.getPropertyName());
		String propertyName = evt.getPropertyName();
		if ("state".equals(propertyName)) {
			statusLabel.setText(evt.getNewValue().toString());
		} else if("isLeader".equals(propertyName)) {
			leaderButton.setEnabled((Boolean)evt.getNewValue());
		} else if ("isQueued".equals(propertyName)) {
			requeueButton.setEnabled(!(Boolean)evt.getNewValue());
		}
		
	}

}
