package org.vaadin.mgrankvi.dpulse.client.ui.geometry;


public class Hexagon {

	private int width, height, x, y;

	private final Point[] corners = new Point[6];

	public Hexagon(final int width, final int height) {
		this(width, height, 0, 0);
	}

	public Hexagon(final int width, final int height, final int x, final int y) {
		this.width = width;
		this.height = height;
		if (height % 2 == 1) {
			this.height = height - 1;
		}
		this.height = GeometryUtil.validateAndCorrectHexProportions(width,
				height);

		this.x = x;
		this.y = y;

		calculatePoints();
	}

	private void calculatePoints() {
		corners[0] = new Point(x, y + (height / 2));
		corners[1] = new Point(x + (width / 2), y);
		corners[2] = new Point(corners[1].getX() + width, y);
		corners[3] = new Point(x + (2 * width), corners[0].getY());
		corners[4] = new Point(corners[2].getX(), y + height);
		corners[5] = new Point(corners[1].getX(), corners[4].getY());
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

	public void setX(final int x) {
		this.x = x;
		calculatePoints();
	}

	public int getX() {
		return x;
	}

	public void setY(final int y) {
		this.y = y;
		calculatePoints();
	}

	public int getY() {
		return y;
	}

	public Point[] getCorners() {
		final Point[] copyCorners = new Point[corners.length];
		for (int i = 0; i < corners.length; i++) {
			copyCorners[i] = corners[i];
		}
		return copyCorners;
	}
}
