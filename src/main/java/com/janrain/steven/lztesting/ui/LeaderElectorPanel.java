package com.janrain.steven.lztesting.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class LeaderElectorPanel extends JPanel implements PropertyChangeListener {
	
	private JButton relinquishButton;
	private JButton requeueButton;
	private JLabel statusLabel;
	private ImageIcon lightOn;
	private ImageIcon lightOff;
	private JLabel onOffLabel;
	private static final Logger logger = Logger.getLogger(LeaderElectorPanel.class);
	
	public LeaderElectorPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		relinquishButton = new JButton("Relinquish");
		relinquishButton.setEnabled(false);
		relinquishButton.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				setLeader(false);
			}
		});
		add(relinquishButton);
		requeueButton = new JButton("Requeue");
		requeueButton.setEnabled(false);
		requeueButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent evt) {
				handleRequeue();
			}
		});
		add(requeueButton);
		statusLabel = new JLabel("Ready");
		loadImages(statusLabel.getSize());
		add(statusLabel);
		onOffLabel = new JLabel(lightOff);
		add(onOffLabel);
	}

	private void loadImages(Dimension dimension) {
		int size = 32;
		lightOn = resizeImage(size, "slim-green-led-on-th.png");
 		lightOff = resizeImage(size, "slim-green-led-off-th.png");
	}

	private ImageIcon resizeImage(int size, String imageURL) {
		try {
			BufferedImage sourceImage;
			sourceImage = ImageIO.read(getClass().getResource(imageURL));
			BufferedImage resizedImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2 = resizedImg.createGraphics();
	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g2.drawImage(sourceImage, 0, 0, size, size, null);
	        g2.dispose();
	        return new ImageIcon(resizedImg);
		} catch (IOException e) {
			return new ImageIcon(getClass().getResource("slim-green-led-off-th.png"));
		}
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
		logger.debug("setLeader: "+isLeader);
		firePropertyChange("processing", !isLeader, isLeader);
		requeueButton.setEnabled(!isLeader);
		relinquishButton.setEnabled(isLeader);
		if (isLeader) {
			statusLabel.setText("Leader");
			onOffLabel.setIcon(lightOn);
		} else {
			onOffLabel.setIcon(lightOff);
			statusLabel.setText("Not Queued");
		}
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("Got property change event "+evt.getPropertyName());
		String propertyName = evt.getPropertyName();

		if("isLeader".equals(propertyName)) {
			relinquishButton.setEnabled((Boolean)evt.getNewValue());
			statusLabel.setText(evt.getNewValue().toString());
		} else if ("isQueued".equals(propertyName)) {
			requeueButton.setEnabled(!(Boolean)evt.getNewValue());
		}
		
	}

	public void setState(String name) {
		statusLabel.setText(name);		
	}

}
