package org.vaadin.mgrankvi.dpulse.client.ui;

public enum ConnectionState {
	DOWN("#FF8080", "red"), OK("#80C080", "green"), NOT_KNOWN("#FFE066", "#989898");

	private String color;
	private String border;

	private ConnectionState(final String color, final String border) {
		this.color = color;
		this.border = border;
	}

	public String getColor() {
		return color;
	}

	public String getBorderColor() {
		return border;
	}

	public static ConnectionState getEnum(final String connection) {
		for (final ConnectionState state : values()) {
			if (state.toString().equals(connection)) {
				return state;
			}
		}
		return NOT_KNOWN;
	}
}
