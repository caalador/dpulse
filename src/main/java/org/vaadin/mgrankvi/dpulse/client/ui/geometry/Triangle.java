package org.vaadin.mgrankvi.dpulse.client.ui.geometry;

public class Triangle {

	private int height, x, y;

	private final double sqare3 = Math.sqrt(3.0);
	private double side;

	private final Point[] corners = new Point[3];

	public Triangle(final int height) {
		this(height, 0, 0);
	}

	public Triangle(final int height, final int x, final int y) {
		this.height = height;

		this.x = x;
		this.y = y;

		side = (2 * height) / sqare3;
		calculatePoints();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
		side = (2 * height) / sqare3;
		calculatePoints();
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
		calculatePoints();
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
		calculatePoints();
	}

	public Point[] getCorners() {
		return corners;
	}

	private void calculatePoints() {
		corners[0] = new Point(x, y + height);
		corners[1] = new Point((int) (x + side / 2), y);
		corners[2] = new Point((int) (x + side), corners[0].getY());
	}
}
