package org.vaadin.mgrankvi.dpulse.client.pulse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.vaadin.mgrankvi.dpulse.client.ui.connectobject.Hex;
import org.vaadin.mgrankvi.dpulse.client.ui.connectobject.LinkBox;
import org.vaadin.mgrankvi.dpulse.client.ui.connectobject.Selectable;
import org.vaadin.mgrankvi.dpulse.client.ui.connectobject.Tile;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.GeometryUtil;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Util;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VDataPulse extends Widget implements ClickHandler {

	public static final String POLL_INTERVAL = "interval";
	public static final String OPTIONS_UIDL = "options";
	public static final String CONNECTOR = "connector";
	public static final String CONNECTION_STATE = "connectionState";
	public static final String PING = "ping";
	public static final String CONNECTOR_TYPE = "connectorType";
	public static final String ITEMS_IN_LINE = "itemsInLine";
	public static final String POSITION = "connector_id";

	public static final String CLICK_EVENT = "itemClick";

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-connected";

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	// protected ApplicationConnection client;

	private final Canvas canvas;

	private final Poller poller;

	private boolean pollerSuspendedDueDetach = true;

	private int width = 25;
	private int height = 43;
	private int lineAmount = 4;
	private int interval = 15000;
	private Type type;

	private final Map<Integer, Selectable> objects = new HashMap<Integer, Selectable>();

	public enum Type {
		HEX, LINK_BOX, TILE;

		public static Type getType(final String target) {
			for (final Type type : values()) {
				if (type.toString().equals(target)) {
					return type;
				}
			}
			return HEX;
		}
	}

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VDataPulse() {

		setElement(Document.get().createDivElement());

		// This method call of the Paintable interface sets the component
		// style name in DOM tree
		setStyleName(CLASSNAME);

		// Tell GWT we are interested in receiving click events
		sinkEvents(Event.ONCLICK);
		// Add a handler for the click events (this is similar to
		// FocusWidget.addClickHandler())
		addDomHandler(this, ClickEvent.getType());

		canvas = Canvas.createIfSupported();
		if (canvas != null) {
			getElement().appendChild(canvas.getElement());
			canvas.setCoordinateSpaceWidth(50);
			canvas.setCoordinateSpaceHeight(50);
		} else {
			getElement().setInnerHTML("Canvas not supported");
		}
		poller = new Poller();
	}

	// /**
	// * Called whenever an update is received from the server
	// */
	// public void updateFromUIDL(final UIDL uidl, final ApplicationConnection
	// client) {
	// // This call should be made first.
	// // It handles sizes, captions, tooltips, etc. automatically.
	// if (client.updateComponent(this, uidl, true) || canvas == null) {
	// // If client.updateComponent returns true there has been no changes
	// // and we
	// // do not need to update anything.
	// return;
	// }
	//
	// // Save reference to server connection object to be able to send
	// // user interaction later
	// this.client = client;
	//
	// // Save the client side identifier (paintable id) for the widget
	// paintableId = uidl.getId();
	//
	// final UIDL options = uidl.getChildByTagName(OPTIONS_UIDL);
	//
	// // width = options.getIntAttribute("width");
	// // height = options.getIntAttribute("height");
	// // type = Type.getType(options.getStringAttribute(CONNECTOR_TYPE));
	//
	// if (options.hasAttribute(ITEMS_IN_LINE)) {
	// lineAmount = options.getIntAttribute(ITEMS_IN_LINE);
	// }
	//
	// if (options.hasAttribute(POLL_INTERVAL)) {
	// interval = options.getIntAttribute(POLL_INTERVAL);
	//
	// }
	// createConnectors(type, uidl);
	// }

	public void setType(final Type type) {
		this.type = type;
	}

	public void setLineAmount(final int lineAmount) {
		this.lineAmount = lineAmount;
	}

	public int getLineAmount() {
		return lineAmount;
	}

	public void setInterval(final int interval) {
		this.interval = interval;
		poller.cancel();
		poller.scheduleRepeating(interval);
	}

	public int getInterval() {
		return interval;
	}

	public void setItemWidth(final int width) {
		this.width = width;
	}

	public void setItemHeight(final int height) {
		this.height = height;
	}

	public void setWidth(final int width) {
		setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
	}

	public void setHeight(final int height) {
		setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);
	}

	public void createConnectors(final List<ConnectorState> connectors) {
		objects.clear();
		int y = 5, x = 5, j = 0;

		switch (type) {
		case HEX:
			height = GeometryUtil.validateAndCorrectHexProportions(width, height);
			for (int i = 0; i < connectors.size(); i++) {
				final ConnectorState connector = connectors.get(i);

				if (i > 0 && i % lineAmount == 0) {
					y += height + 5;
					j = 0;
				}
				final Hex hex = new Hex(width, height, x + (height * j), (i % lineAmount) % 2 == 0 ? y : y + 1 + (height / 2));
				hex.setPingState(connector.ping);
				hex.setConnectionState(connector.state);
				hex.paint(canvas);
				objects.put(connector.position, hex);
				j++;
			}
			break;
		case LINK_BOX:
			boolean flip = false;
			for (int i = 0; i < connectors.size(); i++) {
				final ConnectorState connector = connectors.get(i);

				if (i > 0 && i % lineAmount == 0) {
					x += 1.3 * width;
					j = 0;
					flip = !flip;
				}
				final LinkBox link = new LinkBox(width, height, x, y + (int) (j * 1.75 * height));
				if (flip) {
					link.flip();
				}
				link.setPingState(connector.ping);
				link.setConnectionState(connector.state);
				link.paint(canvas);
				objects.put(connector.position, link);
				j++;
			}
			break;
		case TILE:
			for (int i = 0; i < connectors.size(); i++) {
				final ConnectorState connector = connectors.get(i);

				if (i > 0 && i % lineAmount == 0) {
					y += 1.2 * width;
					j = 0;
				}
				final Tile tile = new Tile(width, x + (int) (width * 1.2 * j), y);
				tile.setPing(connector.ping);
				tile.setState(connector.state);
				tile.paint(canvas);
				objects.put(connector.position, tile);
				j++;
			}
			break;

		}
	}

	// private void createConnectors(final Type type, final UIDL uidl) {
	// objects.clear();
	// int y = 5, x = 5, j = 0;
	//
	// switch (type) {
	// case HEX:
	// height = GeometryUtil.validateAndCorrectHexProportions(width, height);
	// for (int i = 0; i + 1 < uidl.getChildCount(); i++) {
	// final UIDL child = uidl.getChildUIDL(i + 1);
	// final String conn = child.getStringAttribute(CONNECTION_STATE);
	// final String ping = child.getStringAttribute(PING);
	//
	// if (i > 0 && i % lineAmount == 0) {
	// y += height + 5;
	// j = 0;
	// }
	// final Hex hex = new Hex(width, height, x + (height * j), (i % lineAmount)
	// % 2 == 0 ? y : y + 1 + (height / 2));
	// hex.setPingState(PingState.getEnum(ping));
	// hex.setConnectionState(ConnectionState.getEnum(conn));
	// hex.paint(canvas);
	// j++;
	// objects.put(child.getIntAttribute(POSITION), hex);
	// }
	// break;
	// case LINK_BOX:
	// boolean flip = false;
	// for (int i = 0; i + 1 < uidl.getChildCount(); i++) {
	// final UIDL child = uidl.getChildUIDL(i + 1);
	// final String conn = child.getStringAttribute(CONNECTION_STATE);
	// final String ping = child.getStringAttribute(PING);
	//
	// if (i > 0 && i % lineAmount == 0) {
	// x += 1.3 * width;
	// j = 0;
	// flip = !flip;
	// }
	// final LinkBox link = new LinkBox(width, height, x, y + (int) (j * 1.75 *
	// height));
	// if (flip) {
	// link.flip();
	// }
	// link.setPingState(PingState.getEnum(ping));
	// link.setConnectionState(ConnectionState.getEnum(conn));
	// link.paint(canvas);
	// j++;
	// objects.put(child.getIntAttribute(POSITION), link);
	// }
	// break;
	// case TILE:
	// for (int i = 0; i + 1 < uidl.getChildCount(); i++) {
	// final UIDL child = uidl.getChildUIDL(i + 1);
	// final String conn = child.getStringAttribute(CONNECTION_STATE);
	// final String ping = child.getStringAttribute(PING);
	//
	// if (i > 0 && i % lineAmount == 0) {
	// y += 1.2 * width;
	// j = 0;
	// }
	// final Tile tile = new Tile(width, x + (int) (width * 1.2 * j), y);
	// tile.setPing(PingState.getEnum(ping));
	// tile.setState(ConnectionState.getEnum(conn));
	// tile.paint(canvas);
	// j++;
	// objects.put(child.getIntAttribute(POSITION), tile);
	// }
	// break;
	// }
	// }

	/**
	 * Called when a native click event is fired.
	 * 
	 * @param event
	 *            the {@link ClickEvent} that was fired
	 */
	@Override
	public void onClick(final ClickEvent event) {
		final int x = event.getRelativeX(canvas.getElement());
		final int y = event.getRelativeY(canvas.getElement());
		for (final Entry<Integer, Selectable> set : objects.entrySet()) {
			if (set.getValue().pointInObject(x, y)) {
				fireEvent(new DataEvent(DataEvent.EventType.SHOW, set.getKey()));
				// client.updateVariable(paintableId, CLICK_EVENT, set.getKey(),
				// true);
				return;
			}
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if (pollerSuspendedDueDetach) {
			poller.scheduleRepeating(interval);
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if (interval > 0) {
			poller.cancel();
			pollerSuspendedDueDetach = true;
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			poller.cancel();
		}
	}

	class Poller extends Timer {

		@Override
		public void run() {
			if (Util.isAttachedAndDisplayed(VDataPulse.this)) {
				fireEvent(new DataEvent(DataEvent.EventType.REFRESH, 0));
				// client.sendPendingVariableChanges();
				// client.updateVariable(paintableId, "refresh", "update",
				// true);
			}
		}

	}

	public HandlerRegistration addHoverEventHandler(final DataEventHandler dataEventHandler) {
		return addHandler(dataEventHandler, DataEvent.getType());
	}

}
