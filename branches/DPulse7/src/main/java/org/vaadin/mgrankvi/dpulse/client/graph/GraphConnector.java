package org.vaadin.mgrankvi.dpulse.client.graph;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(org.vaadin.mgrankvi.dpulse.Graph.class)
public class GraphConnector extends AbstractComponentConnector implements HoverEventHandler {

	private static final long serialVersionUID = -2302255739328500205L;

	private final GraphServerRpc rpc = RpcProxy.create(GraphServerRpc.class, this);
	private final List<HandlerRegistration> handlerRegistration = new ArrayList<HandlerRegistration>();

	@Override
	public void init() {
		super.init();

		handlerRegistration.add(getWidget().addHoverEventHandler(this));
	}

	@Override
	protected Widget createWidget() {
		return GWT.create(VGraph.class);
	}

	@Override
	public VGraph getWidget() {
		return (VGraph) super.getWidget();
	};

	@Override
	public GraphState getState() {
		return (GraphState) super.getState();
	}

	@Override
	public void onStateChanged(final StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		getWidget().setHeight(getState().size.height);
		getWidget().setWidth(getState().size.width);
		if (getState().values != null) {
			getWidget().setPoints(getState().values);
		}
		getWidget().setMaxValue(getState().max);
	}

	@Override
	public void onHoverEvent(final HoverEvent event) {
		rpc.hoverElement(event.getValue());
	}

}
