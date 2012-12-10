package org.vaadin.mgrankvi.dpulse.client.ui.geometry;

public class Square extends Rectangle {

	public Square(final int side) {
		super(side, side, 0, 0);
	}

	public Square(final int side, final int x, final int y) {
		super(side, side, x, y);
	}

	@Override
	protected void calculatePoints() {
		corners[0] = new Point(x, y);
		corners[1] = new Point(x + width, y);
		corners[2] = new Point(corners[1].getX(), y + height);
		corners[3] = new Point(x, corners[2].getY());
	}
}
