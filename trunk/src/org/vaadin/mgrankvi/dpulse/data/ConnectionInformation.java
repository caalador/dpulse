package org.vaadin.mgrankvi.dpulse.data;

import java.io.Serializable;
import java.util.List;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;

/**
 * Class that holds needed and interesting information for DataPulse.
 * 
 * @author Mikael
 * 
 */
public class ConnectionInformation implements Serializable {

	private static final long serialVersionUID = -2588063490513134364L;

	private Long pingTime;

	private String target, exception, description;

	private ConnectionState state = ConnectionState.NOT_KNOWN;

	private final RoundRobin pingTimes;

	/**
	 * Initializes a RoundRobin for 15 ping times
	 */
	public ConnectionInformation() {
		this(15);
	}

	/**
	 * Creates the ping times RoundRobin
	 * 
	 * @param amount
	 *            Amount of ping times to save
	 */
	public ConnectionInformation(final int amount) {
		pingTimes = new RoundRobin(amount);
	}

	/**
	 * Get the latest ping time
	 * 
	 * @return
	 */
	public Long getPingTime() {
		return pingTime;
	}

	/**
	 * Sets the latest ping time and adds it to the RoundRobin.
	 * 
	 * @param pingTime
	 */
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

	/**
	 * Get all ping times stored in the round robin.
	 * 
	 * @return
	 */
	public List<Long> getPingTimes() {
		return pingTimes.getValues();
	}
}
