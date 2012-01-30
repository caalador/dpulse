package org.vaadin.mgrankvi.dpulse.client.ui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VGraph extends Widget implements Paintable {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-connected";

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	protected ApplicationConnection client;

	private final Canvas canvas;

	private int width, height, maxValue;
	private int[] points;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VGraph() {

		setElement(Document.get().createDivElement());

		// This method call of the Paintable interface sets the component
		// style name in DOM tree
		setStyleName(CLASSNAME);

		// Tell GWT we are interested in receiving click events
		// sinkEvents(Event.ONCLICK);
		// Add a handler for the click events (this is similar to
		// FocusWidget.addClickHandler())
		// addDomHandler(this, ClickEvent.getType());

		canvas = Canvas.createIfSupported();
		if (canvas != null) {
			getElement().appendChild(canvas.getElement());
			canvas.setCoordinateSpaceWidth(50);
			canvas.setCoordinateSpaceHeight(50);
		} else {
			getElement().setInnerHTML("Canvas not supported");
		}
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {
		// This call should be made first.
		// It handles sizes, captions, tooltips, etc. automatically.
		if (client.updateComponent(this, uidl, true) || canvas == null) {
			// If client.updateComponent returns true there has been no changes
			// and we
			// do not need to update anything.
			return;
		}

		// Save reference to server connection object to be able to send
		// user interaction later
		this.client = client;

		// Save the client side identifier (paintable id) for the widget
		paintableId = uidl.getId();

		final UIDL options = uidl.getChildByTagName(VDataPulse.OPTIONS_UIDL);

		width = options.getIntAttribute("width");
		height = options.getIntAttribute("height");

		setWidth(width + "px");
		setHeight(height + "px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);

		points = uidl.getIntArrayAttribute("values");
		maxValue = uidl.getIntAttribute("maxValue");

		paint();
	}

	private void paint() {
		final Context2d context = canvas.getContext2d();

		context.setFillStyle("#E0E0E0");
		context.fillRect(0, 0, width, height);

		context.setStrokeStyle("#BBB");

		context.beginPath();
		context.moveTo(0, 0);
		context.lineTo(0, height);
		context.lineTo(width, height);
		context.moveTo(0, 0);
		context.closePath();
		context.stroke();

		plotDataPoints(context);
	}

	private void plotDataPoints(final Context2d context) {
		context.setStrokeStyle("green");

		context.beginPath();
		context.moveTo(0, height);

		final double xIncrement = (double) width / points.length;
		final double scale = height / (maxValue * 1.1);
		for (int i = 0; i < points.length; i++) {
			final double x = i * xIncrement;
			final double y = height - (points[i] * scale);
			VConsole.log("" + y);
			context.lineTo(x, y);
		}

		context.moveTo(width, height);
		context.closePath();
		context.stroke();
	}
}
