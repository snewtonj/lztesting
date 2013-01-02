package com.janrain.steven.lztesting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

import com.janrain.steven.lztesting.model.LeaderState;
import com.janrain.steven.lztesting.ui.LeaderElectorPanel;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.leader.LeaderSelector;
import com.netflix.curator.framework.recipes.leader.LeaderSelectorListener;
import com.netflix.curator.framework.state.ConnectionState;

public class ElectionHandler implements LeaderSelectorListener, PropertyChangeListener {

	private LeaderSelector leaderSelector;
	private String id;
	private static final Logger logger = Logger.getLogger(ElectionHandler.class);
	private volatile boolean continueProcessing;
	private LeaderState stateBean;
	private LeaderElectorPanel uiPanel;
	
	public ElectionHandler(LeaderState stateBean, LeaderElectorPanel lep, CuratorFramework client, int clientIdNumber) {
		this.stateBean = stateBean;
		this.uiPanel = lep;
		this.uiPanel.addPropertyChangeListener(this);
		String leaderPath = "/" + ElectionHandler.class.getSimpleName();
		this.id = leaderPath+ " #"+ clientIdNumber;
		leaderSelector = new LeaderSelector(client, leaderPath, this);
		leaderSelector.setId(this.id);
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
		stateBean.setLeader(true);
		uiPanel.setLeader(true);
		while(continueProcessing) {
			Thread.sleep(3333);
			logger.info(id+" still leader? "+ leaderSelector.hasLeadership());
		}
		stateBean.setLeader(false);
		uiPanel.setLeader(false);
		logger.info(id+ " Lost leader");
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

	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if ("processing".equals(propertyName)) {
			Boolean isProcessing = (Boolean) evt.getNewValue();
			if (!isProcessing) {
				stopProcessing();
			}
		}
		if ("requeue".equals(propertyName)) {
			requeue();
		}
	}

	public void start() {
		leaderSelector.start();
	}

}
