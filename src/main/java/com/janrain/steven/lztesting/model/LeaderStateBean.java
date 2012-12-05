package com.janrain.steven.lztesting.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LeaderStateBean {
	private boolean isQueued;
	private boolean isLeader;
	private String state;
	private PropertyChangeSupport changeSupport;
	
	public LeaderStateBean() {
		changeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changeSupport.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changeSupport.removePropertyChangeListener(l);
	}
	
	public boolean isQueued() {
		return isQueued;
	}
	public void setQueued(boolean isQueued) {
		this.isQueued = isQueued;
		changeSupport.firePropertyChange("isQueued", !this.isQueued, this.isQueued);
	}
	public boolean isLeader() {
		return isLeader;
	}
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
		changeSupport.firePropertyChange("isLeader", !this.isLeader, this.isLeader);
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		String oldState = this.state;
		this.state = state;
		changeSupport.firePropertyChange("state", oldState, this.state);
	}

}
