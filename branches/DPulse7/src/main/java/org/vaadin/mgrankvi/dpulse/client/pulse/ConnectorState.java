package org.vaadin.mgrankvi.dpulse.client.pulse;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;

import com.vaadin.shared.AbstractComponentState;

public class ConnectorState extends AbstractComponentState {

	public int position;
	public ConnectionState state;
	public PingState ping;

	// target.addAttribute(VDataPulse.POSITION,
	// connections.indexOf(connection));
	// target.addAttribute(VDataPulse.CONNECTION_STATE,
	// connection.getConnectionState().toString());
	// target.addAttribute(VDataPulse.PING, connection.getPing().toString());
}
