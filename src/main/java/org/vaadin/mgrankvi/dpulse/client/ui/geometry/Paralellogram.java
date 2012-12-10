package org.vaadin.mgrankvi.dpulse.client.ui.geometry;

public class Paralellogram extends Rectangle {

	public Paralellogram(final int width, final int height) {
		super(width, height, 0, 0);
	}

	public Paralellogram(final int width, final int height, final int x,
			final int y) {
		super(width, height, x, y);
	}

	@Override
	protected void calculatePoints() {
		corners[1] = new Point(x + width, y);
		corners[2] = new Point(corners[1].getX(), y + height);
		corners[0] = new Point(x, corners[2].getY() + height / 2);
		corners[3] = new Point(x, corners[0].getY() + height);
	}

	public void flip() {
		corners[1] = new Point(corners[1].getX(), corners[0].getY());
		corners[0] = new Point(x, y);
		final int corner2Y = corners[2].getY();
		corners[2] = new Point(corners[2].getX(), corners[3].getY());
		corners[3] = new Point(corners[3].getX(), corner2Y);
	}

}
