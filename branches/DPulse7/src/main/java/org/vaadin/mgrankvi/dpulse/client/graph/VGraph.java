package org.vaadin.mgrankvi.dpulse.client.graph;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.VConsole;

public class VGraph extends Widget {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-connected";

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	// protected ApplicationConnection client;

	private final Canvas canvas;

	private int width, height, maxValue;
	private Integer[] points = new Integer[0];
	private double[] xPoints;
	private double lastSent = -1;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VGraph() {

		setElement(Document.get().createDivElement());

		setStyleName(CLASSNAME);

		sinkEvents(Event.ONMOUSEMOVE);

		canvas = Canvas.createIfSupported();
		if (canvas != null) {
			getElement().appendChild(canvas.getElement());
			canvas.setCoordinateSpaceWidth(50);
			canvas.setCoordinateSpaceHeight(50);
		} else {
			getElement().setInnerHTML("Canvas not supported");
		}
	}

	// /**
	// * Called whenever an update is received from the server
	// */
	// public void updateFromUIDL(final UIDL uidl, final ApplicationConnection
	// client) {
	//
	// if (client.updateComponent(this, uidl, true) || canvas == null) {
	// return;
	// }
	//
	// this.client = client;
	//
	// paintableId = uidl.getId();
	//
	// final UIDL options = uidl.getChildByTagName(VDataPulse.OPTIONS_UIDL);
	//
	// // width = options.getIntAttribute("width");
	// // height = options.getIntAttribute("height");
	//
	// // points = uidl.getIntArrayAttribute("values");
	// // xPoints = new double[points.length];
	// maxValue = uidl.getIntAttribute("maxValue");
	//
	// paint();
	// }

	public void setWidth(final int width) {
		this.width = width;
		setWidth(width + "px");
		canvas.setCoordinateSpaceWidth(width);
		paint();
	}

	public void setHeight(final int height) {
		this.height = height;
		setHeight(height + "px");
		canvas.setCoordinateSpaceHeight(height);
		paint();
	}

	public void setPoints(final Integer[] points) {
		this.points = points;
		xPoints = new double[points.length];
		paint();
	}

	public void setMaxValue(final int maxValue) {
		this.maxValue = maxValue;
		paint();
	}

	@Override
	public void onBrowserEvent(final Event event) {

		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEMOVE: {
			final int mouseX = event.getClientX() - canvas.getAbsoluteLeft();
			for (int i = 0; i + 1 < xPoints.length; i++) {
				if (mouseX > xPoints[i + 1]) {
					continue;
				}
				if (mouseX - xPoints[i] > xPoints[i + 1] - mouseX) {
					if (lastSent == i + 1) {
						break;
					}
					repaintAndAddPointMarker(i + 1);
					fireEvent(new HoverEvent(i + 1));
					// client.updateVariable(paintableId, "hover", i + 1, true);
					lastSent = i + 1;
				} else {
					if (lastSent == i) {
						break;
					}
					repaintAndAddPointMarker(i);
					fireEvent(new HoverEvent(i));
					// client.updateVariable(paintableId, "hover", i, true);
					lastSent = i;
				}
				break;
			}
		}
		}
	}

	private void repaintAndAddPointMarker(final int i) {
		clearGraph();
		paint();
		markPointInGraph(i);
	}

	private void clearGraph() {
		canvas.getContext2d().clearRect(0.0, 0.0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceWidth());
	}

	private void markPointInGraph(final int position) {
		final Context2d context = canvas.getContext2d();

		final double scale = height / (maxValue * 1.1);

		context.setFillStyle("green");
		context.beginPath();
		context.fillRect(xPoints[position] - 2, height - (points[position] * scale) - 2, 4, 4);
		context.closePath();
		context.fill();
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

		context.beginPath();
		for (int i = 0; i < height; i += height / 10) {
			context.moveTo(0, i);
			context.lineTo(5, i);
		}
		context.moveTo(0, 0);
		context.closePath();
		context.stroke();

		plotDataPoints(context);
	}

	private void plotDataPoints(final Context2d context) {
		if (points == null || points.length == 0) {
			return;
		}
		context.setFillStyle("black");
		context.setFont("10px Courier");
		context.fillText(Integer.toString((int) (maxValue * 1.1)), 5, 12);

		context.setStrokeStyle("green");

		final double xIncrement = (double) width / points.length;
		final double scale = height / (maxValue * 1.1);
		final double first = points.length > 0 ? height - (points[0] * scale) : 0.0;

		context.beginPath();
		context.moveTo(0, first);
		for (int i = 0; i < points.length; i++) {
			final double x = i * xIncrement;
			final double y = height - (points[i] * scale);
			VConsole.log("" + y);
			context.lineTo(x, y);
			xPoints[i] = x;
		}

		context.moveTo(width, height);
		context.closePath();
		context.stroke();
	}

	public HandlerRegistration addHoverEventHandler(final HoverEventHandler hoverEventHandler) {
		return addHandler(hoverEventHandler, HoverEvent.getType());
	}

}
