package org.vaadin.mgrankvi.dpulse.interfaces;

import java.io.Serializable;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.data.ConnectionInformation;

public interface ConnectionVerifier extends Serializable {

	public void testConnection();

	public PingState getPing();

	public ConnectionInformation getConnectionInfo();

	public ConnectionState getConnectionState();
}
