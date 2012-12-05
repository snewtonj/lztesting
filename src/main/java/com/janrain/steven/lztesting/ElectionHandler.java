package com.janrain.steven.lztesting;

import java.util.Random;

import javax.swing.UIManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.janrain.steven.lztesting.model.LeaderStateBean;
import com.janrain.steven.lztesting.ui.LeaderElectorFrame;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.framework.recipes.leader.LeaderSelector;
import com.netflix.curator.framework.recipes.leader.LeaderSelectorListener;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.retry.ExponentialBackoffRetry;

public class ElectionHandler implements LeaderSelectorListener {

	private static ElectionHandler eh;
	private static LeaderSelector leaderSelector;
	private static LeaderElectorFrame ui;
	private static String id;
	private static final Logger logger = Logger.getLogger(ElectionHandler.class);
	private volatile boolean continueProcessing;
	private LeaderStateBean stateBean;
	
	public ElectionHandler(LeaderStateBean stateBean) {
		this.stateBean = stateBean;
	}

	public void stateChanged(CuratorFramework cf, ConnectionState state) {
		logger.info(id+" State Changed: "+state);
		stateBean.setState(state.name());
		switch (state) {
		case SUSPENDED:
			continueProcessing = false;
			stateBean.setLeader(continueProcessing);
			break;
		case LOST:
			leaderSelector.close();
			//System.exit(ConnectionState.LOST.ordinal());
		default:
			break;
		}
	}

	public void takeLeadership(CuratorFramework cf) throws Exception {
		logger.info(id+" Elected leader");
		continueProcessing = true;
		stateBean.setLeader(continueProcessing);
		while(continueProcessing) {
			Thread.sleep(3333);
			logger.info(id+" still leader? "+ leaderSelector.hasLeadership());
		}
		logger.info(id+ " Lost leader");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final LeaderStateBean stateBean = new LeaderStateBean();
		eh = new ElectionHandler(stateBean);
		byte[] binaryData = new byte[3];
		new Random().nextBytes(binaryData);
		
        id = Base64.encodeBase64URLSafeString(binaryData);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(stateBean, id);
            }
        });
        CuratorFramework client = CuratorFrameworkFactory.newClient("bp-dev:2181", new ExponentialBackoffRetry(333, 3));
        client.start();
        leaderSelector = new LeaderSelector(client, "/"+ElectionHandler.class.getSimpleName(), eh);
        leaderSelector.setId(id);
//        leaderSelector.autoRequeue();
        leaderSelector.start();
	}

	private static void createAndShowGUI(LeaderStateBean lsBean, String id)  {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Well that sucks, falling back to generic Java LookAndFeel");
		}
		ui = new LeaderElectorFrame(eh, id);
		lsBean.addPropertyChangeListener(ui);
		ui.pack();
		ui.setVisible(true);
	}

	public void shutdown() {
		logger.info(id+" Shutting down");
		leaderSelector.close();
	}
	
	public void stopProcessing() {
		logger.info(id+" Relinquishing leader");
		continueProcessing = false;	
		stateBean.setLeader(continueProcessing);
		stateBean.setQueued(false);		
	}

	public boolean requeue() {
		logger.info(id+" Requeuing");
		boolean isQueued = leaderSelector.requeue();
		stateBean.setQueued(isQueued);
		return isQueued;
	}

}
