package com.janrain.steven.lztesting.ui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class LeaderIndicator extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6974825870946161038L;
	private ImageIcon lightOn;
	private ImageIcon lightOff;

	public LeaderIndicator() {
		super();
		int size = 32;
		lightOn = resizeImage(size, "slim-green-led-on-th.png");
		lightOff = resizeImage(size, "slim-green-led-off-th.png");
		this.setIcon(lightOff);
	}

	private ImageIcon resizeImage(int size, String imageURL) {
		try {
			BufferedImage sourceImage;
			sourceImage = ImageIO.read(getClass().getResource(imageURL));
			BufferedImage resizedImg = new BufferedImage(size, size,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = resizedImg.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(sourceImage, 0, 0, size, size, null);
			g2.dispose();
			return new ImageIcon(resizedImg);
		} catch (IOException e) {
			return new ImageIcon(getClass().getResource(
					"slim-green-led-off-th.png"));
		}
	}
	
	public void setEnabled(boolean enabled) {
		setIcon(enabled ? lightOn : lightOff);
	}

}
