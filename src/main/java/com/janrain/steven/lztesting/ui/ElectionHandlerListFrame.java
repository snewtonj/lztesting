package com.janrain.steven.lztesting.ui;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class ElectionHandlerListFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ElectionHandlerListFrame (String name) {
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = getContentPane();
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));	
	}
	
	public void addElectionHandler(LeaderElectorPanel leFrame) {
		getContentPane().add(leFrame);
	}
//	if ("Close".equals(evt.getActionCommand())) {
//		electionHandler.shutdown();
//		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
//        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
//	}
	

}
