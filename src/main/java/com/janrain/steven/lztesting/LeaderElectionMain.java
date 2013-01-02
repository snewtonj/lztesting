package com.janrain.steven.lztesting;

import java.util.ResourceBundle;

import javax.swing.UIManager;

import com.janrain.steven.lztesting.model.LeaderState;
import com.janrain.steven.lztesting.ui.ElectionHandlerListFrame;
import com.janrain.steven.lztesting.ui.LeaderElectorPanel;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;

public class LeaderElectionMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceBundle bundle = ResourceBundle.getBundle(LeaderElectionMain.class.getName());
		final int lsListenerThreadCount = Integer.valueOf(bundle.getString("threads"));
		final String zkHostname = bundle.getString("zkHostname");
		final String zkPort = bundle.getString("zkPort");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(lsListenerThreadCount, zkHostname+":"+zkPort);
            }
        });
	}

	private static void createAndShowGUI(int lsListenerThreadCount, String serverConnectionString)  {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Well that sucks, falling back to generic Java LookAndFeel");
		}

		ElectionHandlerListFrame ui = new ElectionHandlerListFrame("Election Leader");

		for (int i = 0; i < lsListenerThreadCount; i++) {
	        CuratorFramework client = CuratorFrameworkFactory.newClient(serverConnectionString, new ExponentialBackoffRetry(333, 3));
			final LeaderState stateBean = new LeaderState();
			LeaderElectorPanel lep = new LeaderElectorPanel();
			ElectionHandler electionHandler = new ElectionHandler(stateBean, lep, client, i);
			ui.add(lep);
	        electionHandler.start();
	        client.start();
		}
		ui.pack();
		ui.setVisible(true);
	}

}
