package org.vaadin.mgrankvi.dpulse.client.pulse;

import com.vaadin.shared.communication.ServerRpc;

public interface DataPulseServerRpc extends ServerRpc {

	public void refreshConnectors();

	public void showConnectorInformation(Integer connector);
}
