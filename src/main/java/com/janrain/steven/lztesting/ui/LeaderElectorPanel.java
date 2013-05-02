package com.janrain.steven.lztesting.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;

public class LeaderElectorPanel extends JPanel implements
PropertyChangeListener {

	private JButton relinquishButton;
	private JButton requeueButton;
	private JLabel statusLabel;
	private JLabel onOffLabel;
	private static final Logger logger = Logger
			.getLogger(LeaderElectorPanel.class);

	public LeaderElectorPanel() {
		super();
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		relinquishButton = new JButton("Relinquish");
		relinquishButton.setEnabled(false);
		relinquishButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLeader(false);
			}
		});
		layout.putConstraint(SpringLayout.WEST, relinquishButton, 5,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, relinquishButton, 5,
				SpringLayout.NORTH, this);
		add(relinquishButton);

		requeueButton = new JButton("Requeue");
		requeueButton.setEnabled(false);
		requeueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				handleRequeue();
			}
		});
		layout.putConstraint(SpringLayout.WEST, requeueButton, 5,
				SpringLayout.EAST, relinquishButton);
		layout.putConstraint(SpringLayout.NORTH, requeueButton, 5,
				SpringLayout.NORTH, this);
		add(requeueButton);

		statusLabel = new JLabel("Ready");
		layout.putConstraint(SpringLayout.WEST, statusLabel, 5,
				SpringLayout.EAST, requeueButton);
		layout.putConstraint(SpringLayout.NORTH, statusLabel, 5,
				SpringLayout.NORTH, this);
		add(statusLabel);
		onOffLabel = new LeaderIndicator();
		layout.putConstraint(SpringLayout.WEST, onOffLabel, 5,
				SpringLayout.EAST, statusLabel);
		layout.putConstraint(SpringLayout.NORTH, onOffLabel, 5,
				SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.EAST, this, 5, SpringLayout.EAST,
				onOffLabel);
		layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH,
				onOffLabel);
		add(onOffLabel);
	}


	protected void handleRequeue() {
		boolean oldValue = requeueButton.isEnabled();
		requeueButton.setEnabled(!requeueButton.isEnabled());
		firePropertyChange("requeue", oldValue, requeueButton.isEnabled());
		statusLabel.setText("Queued");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setLeader(boolean isLeader) {
		logger.debug("setLeader: " + isLeader);
		firePropertyChange("processing", !isLeader, isLeader);
		requeueButton.setEnabled(!isLeader);
		relinquishButton.setEnabled(isLeader);
		onOffLabel.setEnabled(isLeader);
		if (isLeader) {
			statusLabel.setText("Leader");
		} else {
			statusLabel.setText("Not Queued");
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("Got property change event " + evt.getPropertyName());
		String propertyName = evt.getPropertyName();

		if ("isLeader".equals(propertyName)) {
			relinquishButton.setEnabled((Boolean) evt.getNewValue());
			statusLabel.setText(evt.getNewValue().toString());
		} else if ("isQueued".equals(propertyName)) {
			requeueButton.setEnabled(!(Boolean) evt.getNewValue());
		}

	}

	public void setState(String name) {
		statusLabel.setText(name);
	}

}
