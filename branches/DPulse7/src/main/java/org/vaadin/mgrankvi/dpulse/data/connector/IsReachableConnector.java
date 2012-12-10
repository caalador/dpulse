package org.vaadin.mgrankvi.dpulse.data.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.data.ConnectionInformation;
import org.vaadin.mgrankvi.dpulse.interfaces.ConnectionVerifier;

public class IsReachableConnector implements ConnectionVerifier {

	private static final long serialVersionUID = 3192342994979899305L;

	private final int[] pingTimes = new int[] { 250, 750 };
	private final ConnectionInformation connectionInfo;

	public IsReachableConnector(final String url, final String description) {
		connectionInfo = new ConnectionInformation();
		connectionInfo.setTarget(url);
		connectionInfo.setDescription(description);
	}

	public void testConnection() {
		isReachable();
	}

	private void isReachable() {
		String exception = null;
		ConnectionState state = ConnectionState.NOT_KNOWN;
		Long time = null;
		InetAddress addr;
		final long start = System.currentTimeMillis();
		try {
			addr = InetAddress.getByName(connectionInfo.getTarget());
			if (addr.isReachable(2500)) {
				state = ConnectionState.OK;
			} else {
				state = ConnectionState.DOWN;
			}
			time = System.currentTimeMillis() - start;
		} catch (final UnknownHostException e) {
			exception = "Target host is unkown " + e.getMessage();
			time = null;
			state = ConnectionState.DOWN;
		} catch (final IOException e) {
			exception = "Error creating connection " + e.getMessage();
			time = System.currentTimeMillis() - start;
			state = ConnectionState.DOWN;
		}
		synchronized (connectionInfo) {
			connectionInfo.setPingTime(time);
			connectionInfo.setState(state);
			connectionInfo.setException(exception);
		}
	}

	public PingState getPing() {
		Long pingTime;
		synchronized (connectionInfo) {
			pingTime = connectionInfo.getPingTime();
		}

		if (pingTime != null && pingTime < pingTimes[0]) {
			return PingState.FAST;
		} else if (pingTime != null && pingTime < pingTimes[1]) {
			return PingState.MEDIUM;
		} else if (pingTime != null) {
			return PingState.SLOW;
		}
		return PingState.NOT_KNOWN;
	}

	public ConnectionInformation getConnectionInfo() {
		synchronized (connectionInfo) {
			return connectionInfo;
		}
	}

	public ConnectionState getConnectionState() {
		return connectionInfo.getState();
	}

}
