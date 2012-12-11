package com.example.dpulse_demo;

import org.vaadin.mgrankvi.dpulse.DataPulse;
import org.vaadin.mgrankvi.dpulse.client.ui.VDataPulse.Type;
import org.vaadin.mgrankvi.dpulse.data.connector.EchoConnector;
import org.vaadin.mgrankvi.dpulse.data.connector.HtmlConnector;
import org.vaadin.mgrankvi.dpulse.data.connector.IsReachableConnector;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

public class Controller extends HorizontalLayout {

	private static final long serialVersionUID = -2445848825755916027L;

	private final DataPulse connected;
	private final ConnectorController connectorType = new ConnectorController();
	private TextField target;

	public Controller(final DataPulse connected) {
		this.connected = connected;
		setSpacing(true);
		init();
	}

	private void init() {
		final NativeSelect selectBox = new NativeSelect("Visualization type");

		selectBox.setNullSelectionAllowed(false);

		selectBox.addItem(Type.HEX);
		selectBox.addItem(Type.LINK_BOX);
		selectBox.addItem(Type.TILE);
		selectBox.select(Type.HEX);

		selectBox.setImmediate(true);
		selectBox.addListener(typeSelect);

		final TextField inLine = new TextField("Items in line");
		inLine.setValue(connected.getItemsInLine());
		inLine.addListener(inLineChange);
		inLine.setImmediate(true);

		final TextField pollTime = new TextField("Polling time in ms");
		pollTime.setValue(connected.getPollInterval());
		pollTime.addListener(timingChange);
		pollTime.setImmediate(true);

		addComponent(selectBox);
		addComponent(inLine);
		addComponent(pollTime);

		addComponent(connectorType);
		target = new TextField("Target url");
		addComponent(target);

		addComponent(new Button("add 1", new Button.ClickListener() {

			private static final long serialVersionUID = -3431736429301232283L;

			public void buttonClick(final ClickEvent event) {
				addConnector(1);
			}
		}));

		addComponent(new Button("add 3", new Button.ClickListener() {

			private static final long serialVersionUID = -3431736429301232283L;

			public void buttonClick(final ClickEvent event) {
				for (int i = 0; i < 3; i++) {
					addConnector(i);
				}
			}
		}));

		addComponent(new Button("add 5", new Button.ClickListener() {

			private static final long serialVersionUID = -3431736429301232283L;

			public void buttonClick(final ClickEvent event) {
				for (int i = 0; i < 5; i++) {
					addConnector(i);
				}
			}
		}));

		addComponent(new Button("Clear connectors", new Button.ClickListener() {

			private static final long serialVersionUID = -3431736429301232283L;

			public void buttonClick(final ClickEvent event) {
				connected.removeAllConnections();
				connected.requestRepaint();
			}
		}));
	}

	private void addConnector(final int i) {
		switch (connectorType.getType()) {
		case ECHO:
			connected.addConnection(new EchoConnector((String) target.getValue(), "Random result set N-" + i));
			break;
		case HTML:
			connected.addConnection(new HtmlConnector((String) target.getValue(), "Random result set N-" + i));
			break;
		case REACHABLE:
			connected.addConnection(new IsReachableConnector((String) target.getValue(), "Random result set N-" + i));
			break;
		default:
			connected.addConnection(new HtmlConnector((String) target.getValue(), "Random result set N-" + i));
		}
	}

	private final ValueChangeListener typeSelect = new ValueChangeListener() {
		private static final long serialVersionUID = 6227019473959144398L;

		public void valueChange(final ValueChangeEvent event) {
			connected.setType((Type) event.getProperty().getValue());
			switch ((Type) event.getProperty().getValue()) {
			case HEX:
				connected.setWidth("25px");
				connected.setHeight("50px");
				break;
			case LINK_BOX:
				connected.setWidth("60px");
				connected.setHeight("20px");
				break;
			case TILE:
				connected.setWidth("50px");
				connected.setHeight("50px");
			}
		}
	};

	private final ValueChangeListener inLineChange = new ValueChangeListener() {

		private static final long serialVersionUID = 9127479775819496933L;

		public void valueChange(final ValueChangeEvent event) {
			final Object value = event.getProperty().getValue();
			if (value instanceof String) {
				final int val = Integer.parseInt((String) value);
				connected.setItemsInLine(val);
			}
		}

	};

	private final ValueChangeListener timingChange = new ValueChangeListener() {

		private static final long serialVersionUID = 9127479775819496933L;

		public void valueChange(final ValueChangeEvent event) {
			final Object value = event.getProperty().getValue();
			if (value instanceof String) {
				final int val = Integer.parseInt((String) value);
				connected.setPollInterval(val);
			}
		}
	};
}
