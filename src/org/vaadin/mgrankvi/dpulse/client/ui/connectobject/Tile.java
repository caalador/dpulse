package org.vaadin.mgrankvi.dpulse.client.ui.connectobject;

import org.vaadin.mgrankvi.dpulse.client.ui.ConnectionState;
import org.vaadin.mgrankvi.dpulse.client.ui.Paint;
import org.vaadin.mgrankvi.dpulse.client.ui.PingState;
import org.vaadin.mgrankvi.dpulse.client.ui.geometry.Square;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class Tile extends Square implements Paint, Selectable {

	private ConnectionState state = ConnectionState.DOWN;
	private PingState ping = PingState.MEDIUM;

	public Tile(final int side, final int x, final int y) {
		super(side, x, y);
	}

	public void paint(final Canvas canvas) {
		final Context2d context = canvas.getContext2d();
		context.setStrokeStyle(state.getBorderColor());
		context.setLineWidth(2.0);
		context.setFillStyle(state.getColor());

		context.fillRect(corners[0].getX(), corners[0].getY(), width, height);

		final int eight = height / 8;

		context.beginPath();
		context.moveTo(corners[0].getX(), corners[0].getY() + 2 * eight);
		context.lineTo(corners[0].getX(), corners[0].getY());
		context.lineTo(corners[1].getX(), corners[1].getY());
		context.lineTo(corners[1].getX(), corners[1].getY() + 2 * eight);
		context.moveTo(corners[0].getX(), corners[0].getY() + 2 * eight);
		context.closePath();

		context.stroke();

		context.beginPath();
		context.moveTo(corners[2].getX(), corners[2].getY() - 2 * eight);
		context.lineTo(corners[2].getX(), corners[2].getY());
		context.lineTo(corners[3].getX(), corners[3].getY());
		context.lineTo(corners[3].getX(), corners[3].getY() - 2 * eight);
		context.moveTo(corners[2].getX(), corners[2].getY() - 2 * eight);
		context.closePath();

		context.stroke();

		// Ping line
		context.setLineWidth(2.5);
		context.setStrokeStyle(ping.getColor());

		context.beginPath();
		context.moveTo(corners[0].getX(), corners[0].getY() + 3 * eight);
		context.lineTo(corners[0].getX(), corners[0].getY() + 5 * eight);
		context.closePath();

		context.stroke();

		context.beginPath();
		context.moveTo(corners[1].getX(), corners[1].getY() + 3 * eight);
		context.lineTo(corners[1].getX(), corners[1].getY() + 5 * eight);
		context.closePath();

		context.stroke();
	}

	public ConnectionState getState() {
		return state;
	}

	public void setState(final ConnectionState state) {
		this.state = state;
	}

	public PingState getPing() {
		return ping;
	}

	public void setPing(final PingState ping) {
		this.ping = ping;
	}

	public boolean pointInObject(final int x, final int y) {
		if (x < corners[0].getX() || x > corners[1].getX() || y < corners[0].getY() || y > corners[3].getY()) {
			return false;
		}
		return true;
	}

}
