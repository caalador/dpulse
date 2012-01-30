package org.vaadin.mgrankvi.dpulse.data;

import java.util.List;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;

public class ConnectionInformation {

	private Long pingTime;

	private String target, exception, description;

	private ConnectionState state = ConnectionState.NOT_KNOWN;

	private final RoundRobin pingTimes;

	public ConnectionInformation() {
		this(15);
	}

	public ConnectionInformation(int amount) {
		pingTimes = new RoundRobin(amount);
	}

	public Long getPingTime() {
		return pingTime;
	}

	public void setPingTime(final Long pingTime) {
		this.pingTime = pingTime;
		if (pingTime != null) {
			pingTimes.addValue(pingTime);
		}
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public String getException() {
		return exception;
	}

	public void setException(final String exception) {
		this.exception = exception;
	}

	public ConnectionState getState() {
		return state;
	}

	public void setState(final ConnectionState state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<Long> getPingTimes() {
		return pingTimes.getValues();
	}
}
