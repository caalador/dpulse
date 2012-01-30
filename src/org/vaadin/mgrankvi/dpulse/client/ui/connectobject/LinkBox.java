package org.vaadin.mgrankvi.dpulse.client.ui.connectobject;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.Paint;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.GeometryUtil;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.Paralellogram;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.Point;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class LinkBox extends Paralellogram implements Paint, Selectable {

	private ConnectionState state = ConnectionState.DOWN;
	private PingState ping = PingState.MEDIUM;
	private Point[] points;

	public LinkBox(final int width, final int height, final int x, final int y) {
		super(width, height, x, y);

		calculatePingBox();
	}

	@Override
	public void setWidth(final int width) {
		super.setWidth(width);
		calculatePingBox();
	}

	@Override
	public void setHeight(final int height) {
		super.setHeight(height);
		calculatePingBox();
	}

	@Override
	public void setX(final int x) {
		super.setX(x);
		calculatePingBox();
	}

	@Override
	public void setY(final int y) {
		super.setY(y);
		calculatePingBox();
	}

	@Override
	public void flip() {
		super.flip();
		calculatePingBox();
	}

	public void paint(final Canvas canvas) {
		final Point[] corners = super.getCorners();

		final Context2d context = canvas.getContext2d();
		context.setStrokeStyle(ConnectionState.NOT_KNOWN.getBorderColor());
		context.setLineWidth(2.0);
		context.setFillStyle(state.getColor());

		context.beginPath();
		context.moveTo(corners[0].getX(), corners[0].getY());
		context.lineTo(corners[1].getX(), corners[1].getY());
		context.lineTo(corners[2].getX(), corners[2].getY());
		context.lineTo(corners[3].getX(), corners[3].getY());
		context.lineTo(corners[0].getX(), corners[0].getY());
		context.closePath();
		context.fill();
		context.stroke();

		context.setFillStyle(ping.getColor());
		context.beginPath();
		context.moveTo(points[0].getX(), points[0].getY());
		// TODO: calc vector lines for next 2 point targets
		context.lineTo(points[1].getX(), points[1].getY());
		context.lineTo(points[2].getX(), points[2].getY());
		context.lineTo(points[3].getX(), points[3].getY());
		context.closePath();
		context.fill();
		context.stroke();
	}

	public void setConnectionState(final ConnectionState state) {
		this.state = state;
	}

	public void setPingState(final PingState ping) {
		this.ping = ping;
	}

	public void calculatePingBox() {
		points = new Point[4];
		points[0] = new Point(corners[0].getX(), corners[0].getY() + height / 3);

		final double k = GeometryUtil.getDirectionalCoefficient(corners[0], corners[1]);
		final int quarterWay = corners[0].getX() + width / 4;

		points[1] = new Point(quarterWay, (int) (k * quarterWay - k * points[0].getX() + points[0].getY()));
		points[2] = new Point(quarterWay, (int) (k * quarterWay - k * corners[0].getX() + corners[0].getY()));
		points[3] = new Point(corners[0].getX(), corners[0].getY());
	}

	public boolean pointInObject(final int x, final int y) {
		final Point t = new Point(x, y);
		final Point[] p = super.getCorners();
		final int bottom = p[3].getY() > p[2].getY() ? p[3].getY() : p[2].getY();
		if (x < this.x || x > p[1].getX() || y < this.y || y > bottom) {
			return false;
		}

		Point a, b, c;
		a = p[0];
		b = p[1];
		c = p[2];

		// Check triangle
		if (getArea(a, b, c) == (getArea(t, a, b) + getArea(t, b, c) + getArea(t, a, c))) {
			return true;
		}
		a = p[0];
		b = p[2];
		c = p[3];

		// Check triangle
		if (getArea(a, b, c) == (getArea(t, a, b) + getArea(t, b, c) + getArea(t, a, c))) {
			return true;
		}

		return false;
	}

	private Double getArea(final Point p1, final Point p2, final Point p3) {
		return Math.abs((p1.getX() * p2.getY() + p2.getX() * p3.getY() + p3.getX() * p1.getY() - p1.getX() * p3.getY() - p3.getX() * p2.getY() - p2.getX()
				* p1.getY()) / 2.0);
	}
}
