package org.vaadin.mgrankvi.dpulse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.vaadin.mgrankvi.dpulse.client.ui.VDataPulse;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.GeometryUtil;
import org.vaadin.mgrankvi.dpulse.data.ConnectionInformation;
import org.vaadin.mgrankvi.dpulse.interfaces.ConnectionVerifier;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VConnected widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.mgrankvi.dpulse.client.ui.VDataPulse.class)
public class DataPulse extends AbstractComponent {

	private static final long serialVersionUID = -4059718377143540897L;

	private final List<ConnectionVerifier> connections = new LinkedList<ConnectionVerifier>();
	private final List<ConnectionVerifier> runConnectors = new LinkedList<ConnectionVerifier>();

	private final Map<ConnectionVerifier, ConnectionTester> threads = new HashMap<ConnectionVerifier, ConnectionTester>();

	private VDataPulse.Type type = VDataPulse.Type.HEX;
	private int pollInterval = 15000;
	private int oldPoll = 15000;
	private int itemsInLine = 4;
	private boolean send = false;
	private int canvaswidth, canvasheight;

	public DataPulse() {
		setWidth("25px");
		setHeight("43px");
	}

	/**
	 * Add new connection to be polled and verified.
	 * 
	 * @param connection
	 */
	public void addConnection(final ConnectionVerifier connection) {
		if (connection != null && !connections.contains(connection)) {
			connections.add(connection);
			requestRepaint();
			if (oldPoll == pollInterval) {
				oldPoll = pollInterval;
				pollInterval = 100;
				send = true;
			}
		}
		calculateSize();
	}

	/**
	 * Remove connection from polling.
	 * 
	 * @param connection
	 */
	public void removeConnection(final ConnectionVerifier connection) {
		connections.remove(connection);
		runConnectors.remove(connection);
	}

	/**
	 * Clear all verification connections.
	 */
	public void removeAllConnections() {
		final List<ConnectionVerifier> clone = new LinkedList<ConnectionVerifier>(connections);
		for (final ConnectionVerifier connection : clone) {
			removeConnection(connection);
		}
	}

	/**
	 * Set visualization type.
	 * 
	 * @param type
	 */
	public void setType(final VDataPulse.Type type) {
		this.type = type;
	}

	public int getPollInterval() {
		return pollInterval;
	}

	/**
	 * Set polling interval in ms.
	 * 
	 * @param pollInterval
	 */
	public void setPollInterval(final int pollInterval) {
		this.pollInterval = pollInterval;
		oldPoll = pollInterval;
		send = true;
	}

	public int getItemsInLine() {
		return itemsInLine;
	}

	/**
	 * Set amount of components one full line should contain
	 * 
	 * @param itemsInLine
	 */
	public void setItemsInLine(final int itemsInLine) {
		this.itemsInLine = itemsInLine;
		calculateSize();
	}

	@Override
	public void setWidth(final String width) {
		super.setWidth(width);
		calculateSize();
	}

	@Override
	public void setHeight(final String height) {
		super.setHeight(height);
		calculateSize();
	}

	@Override
	public void paintContent(final PaintTarget target) throws PaintException {
		super.paintContent(target);

		target.startTag(VDataPulse.OPTIONS_UIDL);
		target.addAttribute("width", getWidth());
		target.addAttribute("height", getHeight());

		target.addAttribute("canvaswidth", canvaswidth);
		target.addAttribute("canvasheight", canvasheight);

		target.addAttribute(VDataPulse.CONNECTOR_TYPE, type.toString());
		target.addAttribute(VDataPulse.ITEMS_IN_LINE, itemsInLine);
		if (send) {
			target.addAttribute(VDataPulse.POLL_INTERVAL, pollInterval);
			send = false;
		}

		target.endTag(VDataPulse.OPTIONS_UIDL);

		for (final ConnectionVerifier connection : connections) {
			target.startTag(VDataPulse.CONNECTOR);
			target.addAttribute(VDataPulse.POSITION, connections.indexOf(connection));
			target.addAttribute(VDataPulse.CONNECTION_STATE, connection.getConnectionState().toString());
			target.addAttribute(VDataPulse.PING, connection.getPing().toString());
			target.endTag(VDataPulse.CONNECTOR);
		}
	}

	private void calculateSize() {
		float pixelWidth = 0;
		float pixelHeight = 0;
		final int items = connections.size();
		switch (type) {
		case HEX:
			final long height = GeometryUtil.validateAndCorrectHexProportions((int) getWidth(), (int) getHeight());

			pixelWidth = 15 + (itemsInLine * (getWidth() * 2));
			pixelHeight = 15 + (int) (1.15 * height * (1 + (1.0 * items / itemsInLine)));
			break;
		case LINK_BOX:
			pixelWidth = 15 + (int) (1.3 * (1 + (1.0 * items / itemsInLine)) * getWidth());
			pixelHeight = 15 + (int) (2 * getHeight() * itemsInLine);
			break;
		case TILE:
			pixelWidth = 15 + (int) (items * (getWidth() * 1.25));
			pixelHeight = 15 + (int) (1.3 * getWidth() * (1 + (items / itemsInLine)));
			break;
		default:
			pixelWidth = 15 + (items * (getWidth() * 2));
			pixelHeight = 15 + (getHeight() * ((items % itemsInLine) + 1));
		}

		canvasheight = (int) pixelHeight;
		canvaswidth = (int) pixelWidth;
	}

	/**
	 * Receive and handle events and other variable changes from the client.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void changeVariables(final Object source, final Map<String, Object> variables) {
		super.changeVariables(source, variables);
		// Variables set by the widget are returned in the "variables" map.

		if (variables.containsKey("refresh")) {
			if (connections.size() != runConnectors.size()) {
				startConnectorsAsSingle();
			} else {
				if (pollInterval != oldPoll) {
					if (!threads.isEmpty()) {
						return;
					}
					pollInterval = oldPoll;
					send = true;
				}
				fireAllConnectors();
			}
		}
		if (variables.containsKey(VDataPulse.CLICK_EVENT)) {
			final Integer position = (Integer) variables.get(VDataPulse.CLICK_EVENT);
			final ConnectionInformation info = connections.get(position).getConnectionInfo();

			getWindow().addWindow(new InfoWindow(info));
		}

		requestRepaint();
	}

	private void startConnectorsAsSingle() {
		for (final ConnectionVerifier connection : connections) {
			if (!runConnectors.contains(connection)) {
				synchronized (threads) {
					if (threads.isEmpty()) {
						threads.put(connection, new ConnectionTester(connection));
						threads.get(connection).start();
						runConnectors.add(connection);
					}
				}
				break;
			}
		}
	}

	private void fireAllConnectors() {
		for (final ConnectionVerifier connection : connections) {
			synchronized (threads) {
				if (!threads.containsKey(connection)) {
					threads.put(connection, new ConnectionTester(connection));
					threads.get(connection).start();
				}
			}
		}
	}

	private class ConnectionTester extends Thread {

		private final ConnectionVerifier connection;

		public ConnectionTester(final ConnectionVerifier connection) {
			this.connection = connection;
		}

		@Override
		public void run() {
			connection.testConnection();
			synchronized (threads) {
				threads.remove(connection);
			}
		}
	}
}
