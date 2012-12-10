package org.vaadin.mgrankvi.dpulse.client.ui;

public enum PingState {
	SLOW("#FFCC80"), MEDIUM("#8080FF"), FAST("#80C080"), NOT_KNOWN("#989898");

	private String color;

	private PingState(final String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public static PingState getEnum(final String ping) {
		for (final PingState state : values()) {
			if (state.toString().equals(ping)) {
				return state;
			}
		}
		return NOT_KNOWN;
	}
}