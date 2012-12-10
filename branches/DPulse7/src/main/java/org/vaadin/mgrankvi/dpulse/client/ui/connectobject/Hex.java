package org.vaadin.mgrankvi.dpulse.client.ui.connectobject;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.Paint;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.GeometryUtil;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.Hexagon;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.Point;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.Triangle;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;

public class Hex extends Hexagon implements Paint, Selectable {

	private ConnectionState state = ConnectionState.DOWN;
	private PingState ping = PingState.SLOW;

	private int fontSize = 12;
	private String font = "Courier";
	private final double halfWidth;

	public Hex(final int width, final int height, final int x, final int y) {
		super(width, height, x, y);
		halfWidth = width / 2.0;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(final int fontSize) {
		this.fontSize = fontSize;
	}

	public String getFont() {
		return font;
	}

	public void setFont(final String font) {
		this.font = font;
	}

	public void paint(final Canvas canvas) {
		final Point[] corners = super.getCorners();

		final Context2d context = canvas.getContext2d();
		context.setLineWidth(2);
		context.setFillStyle(state.getColor());
		context.setStrokeStyle(state.getBorderColor());

		context.beginPath();
		context.moveTo(corners[0].getX(), corners[0].getY());
		context.lineTo(corners[1].getX(), corners[1].getY());
		context.lineTo(corners[2].getX(), corners[2].getY());
		context.lineTo(corners[3].getX(), corners[3].getY());
		context.lineTo(corners[4].getX(), corners[4].getY());
		context.lineTo(corners[5].getX(), corners[5].getY());
		context.lineTo(corners[0].getX(), corners[0].getY());
		context.closePath();
		context.fill();
		context.stroke();

		if (state != ConnectionState.DOWN) {
			context.setStrokeStyle(ping.getColor());
			context.beginPath();

			final Double k = GeometryUtil.getDirectionalCoefficient(corners[0], corners[1]);

			final double upperY = k * ((corners[1].getX() + 3) - (corners[0].getX() + 5)) + corners[0].getY();

			context.moveTo(corners[0].getX() + 5, corners[0].getY());
			context.lineTo(corners[1].getX() + 3, upperY);
			context.lineTo(corners[2].getX() - 3, upperY);
			context.moveTo(corners[0].getX() + 5, corners[0].getY());
			context.closePath();
			context.stroke();
		} else {
			context.setFillStyle("white");
			context.setFont(fontSize + "px " + font);
			context.setTextAlign(TextAlign.CENTER);
			context.fillText("ERROR", corners[0].getX() + getWidth(), corners[0].getY() + (fontSize / 2), getWidth() + halfWidth);

			context.setFillStyle("gray");

			final int quarter = getHeight() / 6;
			Triangle triangle = new Triangle(quarter, (int) (corners[0].getX() + getWidth() - ((2 * quarter) / Math.sqrt(3)) / 2), corners[0].getY() - quarter
					- (getHeight() / 6));
			Point[] trCorner = triangle.getCorners();

			context.beginPath();
			context.moveTo(trCorner[0].getX(), trCorner[0].getY());
			context.lineTo(trCorner[1].getX(), trCorner[1].getY());
			context.lineTo(trCorner[2].getX(), trCorner[2].getY());
			context.closePath();
			context.fill();

			triangle = new Triangle(quarter, (int) (corners[0].getX() + getWidth() - ((2 * quarter) / Math.sqrt(3)) / 2), corners[0].getY() + (getHeight() / 6));
			trCorner = triangle.getCorners();

			context.beginPath();
			context.moveTo(trCorner[0].getX(), trCorner[1].getY());
			context.lineTo(trCorner[1].getX(), trCorner[0].getY());
			context.lineTo(trCorner[2].getX(), trCorner[1].getY());
			context.closePath();
			context.fill();
		}
	}

	public void setConnectionState(final ConnectionState state) {
		this.state = state;
	}

	public void setPingState(final PingState ping) {
		this.ping = ping;
	}

	public boolean pointInObject(final int x, final int y) {
		final Point t = new Point(x, y);
		final Point[] p = super.getCorners();
		if (x < p[0].getX() || x > p[3].getX() || y < p[1].getY() || y > p[4].getY()) {
			return false;
		}
		if (x > p[1].getX() && x < p[4].getX() && y > p[1].getY() && y < p[4].getY()) {
			return true;
		}

		Point a, b, c;
		if (x < p[1].getX()) {
			a = p[0];
			b = p[1];
			c = p[5];
		} else {
			a = p[2];
			b = p[3];
			c = p[4];
		}
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
