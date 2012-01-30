package org.vaadin.mgrankvi.dpulse.client.ui.geometry;

public class Rectangle {

	protected int width, height, x, y;

	protected final Point[] corners = new Point[4];

	public Rectangle(final int width, final int height) {
		this(width, height, 0, 0);
	}

	public Rectangle(final int width, final int height, final int x, final int y) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;

		calculatePoints();
	}

	protected void calculatePoints() {
		corners[0] = new Point(x, y);
		corners[1] = new Point(x + width, y);
		corners[2] = new Point(corners[1].getX(), y + height);
		corners[3] = new Point(x, corners[2].getY());
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
		calculatePoints();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
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
		final Point[] copyCorners = new Point[corners.length];
		for (int i = 0; i < corners.length; i++) {
			copyCorners[i] = corners[i];
		}
		return copyCorners;
	}
}
