package org.vaadin.mgrankvi.dpulse.client.pulse;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.mgrankvi.dpulse.DataPulse.class)
public class DataPulseConnector extends AbstractComponentConnector implements DataEventHandler {

	private static final long serialVersionUID = -3699573318640056793L;
	private final DataPulseServerRpc rpc = RpcProxy.create(DataPulseServerRpc.class, this);
	private final List<HandlerRegistration> handlerRegistration = new ArrayList<HandlerRegistration>();

	@Override
	public void init() {
		super.init();

		handlerRegistration.add(getWidget().addDataEventHandler(this));
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(CDataPulse.class);
	}

	@Override
	public CDataPulse getWidget() {
		return (CDataPulse) super.getWidget();
	};

	@Override
	public DataPulseState getState() {
		return (DataPulseState) super.getState();
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		getWidget().setItemWidth(getState().size.width);
		getWidget().setItemHeight(getState().size.height);
		getWidget().setWidth(getState().canvasSize.width);
		getWidget().setHeight(getState().canvasSize.height);
		getWidget().setType(getState().type);

		if (getWidget().getInterval() != getState().pollInterval) {
			getWidget().setInterval(getState().pollInterval);
		}
		if (getWidget().getLineAmount() != getState().itemsInLine) {
			getWidget().setLineAmount(getState().itemsInLine);
		}
		if (getState().connectorStates != null) {
			getWidget().createConnectors(getState().connectorStates);
		}
	}

	@Override
	public void onDataEvent(final DataEvent event) {
		switch (event.getEventType()) {
		case REFRESH:
			rpc.refreshConnectors();
			break;
		case SHOW:
			rpc.showConnectorInformation(event.getValue());
		}
	}
}
