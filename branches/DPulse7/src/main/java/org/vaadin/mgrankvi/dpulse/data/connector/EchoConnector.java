package org.vaadin.mgrankvi.dpulse.data.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.data.ConnectionInformation;
import org.vaadin.mgrankvi.dpulse.interfaces.ConnectionVerifier;

public class EchoConnector implements ConnectionVerifier {

	private static final long serialVersionUID = 3192342994979899305L;

	private final int[] pingTimes = new int[] { 250, 750 };
	private final ConnectionInformation connectionInfo;

	public EchoConnector(final String url, final String description) {
		connectionInfo = new ConnectionInformation();
		connectionInfo.setTarget(url);
		connectionInfo.setDescription(description);
	}

	public void testConnection() {
		testEchoPort();
	}

	private void testEchoPort() {
		String exception = null;
		ConnectionState state = ConnectionState.NOT_KNOWN;
		Long time = null;
		final long start = System.currentTimeMillis();
		try {
			Socket t = null;
			try {
				t = new Socket(InetAddress.getByName(connectionInfo.getTarget()), 7);
				t.setSoTimeout(3000);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(t.getInputStream()));
				final PrintStream ps = new PrintStream(t.getOutputStream());
				ps.println("Hello");
				final String str = reader.readLine();
				if (str.equals("Hello")) {
					state = ConnectionState.OK;
				} else {
					state = ConnectionState.DOWN;
				}
			} finally {
				if (t != null) {
					t.close();
				}
				time = System.currentTimeMillis() - start;
			}
		} catch (final IOException e) {
			exception = "Error in echo to server." + e.getMessage();
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
