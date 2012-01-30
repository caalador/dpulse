package org.vaadin.mgrankvi.dpulse.client.ui.geometry;

public class GeometryUtil {

	public static Double getDirectionalCoefficient(final Point point1,
			final Point point2) {

		return (1.0 * (point2.getY() - point1.getY()))
				/ (point2.getX() - point1.getX());
	}

	public static int validateAndCorrectHexProportions(final int width,
			int height) {
		final int divide = (int) ((1.0 * width / height) * 100);
		if (divide != 57 || divide != 58) {
			height = (int) (width / 0.58);
		}
		return height;
	}
}
