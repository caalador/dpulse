package org.vaadin.mgrankvi.dpulse.client.pulse;

import java.util.LinkedList;
import java.util.List;

import org.vaadin.mgrankvi.dpulse.client.Size;
import org.vaadin.mgrankvi.dpulse.client.pulse.CDataPulse.Type;

import com.vaadin.shared.AbstractComponentState;

public class DataPulseState extends AbstractComponentState {

	private static final long serialVersionUID = -598614782413285766L;

	public List<ConnectorState> connectorStates = new LinkedList<ConnectorState>();

	public Size size = new Size();
	public Size canvasSize = new Size();
	public Type type = CDataPulse.Type.HEX;
	public int itemsInLine;

	public int pollInterval;

}
