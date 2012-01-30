package org.vaadin.mgrankvi.dpulse.data.connector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.data.ConnectionInformation;
import org.vaadin.mgrankvi.dpulse.interfaces.ConnectionVerifier;

public class HtmlConnector implements ConnectionVerifier {

	private static final long serialVersionUID = 3192342994979899305L;

	private final int[] pingTimes = new int[] { 250, 750 };
	private final ConnectionInformation connectionInfo;

	public HtmlConnector(final String url, final String description) {
		connectionInfo = new ConnectionInformation();
		connectionInfo.setTarget(url);
		connectionInfo.setDescription(description);
	}

	public void testConnection() {
		httpResponse();
	}

	private void httpResponse() {
		String exception = null;
		ConnectionState state = ConnectionState.NOT_KNOWN;
		Long time = null;
		HttpURLConnection urlConn = null;
		final long start = System.currentTimeMillis();
		try {
			try {
				final URL url = new URL(connectionInfo.getTarget());
				urlConn = (HttpURLConnection) url.openConnection();
				urlConn.connect();

				if (HttpURLConnection.HTTP_OK == urlConn.getResponseCode()) {
					state = ConnectionState.OK;
				} else {
					state = ConnectionState.DOWN;
					exception = "Server returned response code " + urlConn.getResponseCode();
				}
			} finally {
				urlConn.disconnect();
				time = System.currentTimeMillis() - start;
			}
		} catch (final SocketTimeoutException ste) {
			exception = "Connection timed out. " + ste.getMessage();
			time = System.currentTimeMillis() - start;
			state = ConnectionState.DOWN;
		} catch (final IOException e) {
			exception = "Error creating HTTP connection " + e.getMessage();
			time = null;
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
		if (connectionInfo.getState().equals(ConnectionState.DOWN)) {
			return PingState.NOT_KNOWN;
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
