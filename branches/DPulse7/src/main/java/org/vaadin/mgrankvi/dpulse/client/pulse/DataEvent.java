package org.vaadin.mgrankvi.dpulse.client.pulse;

import com.google.gwt.event.shared.GwtEvent;

public class DataEvent extends GwtEvent<DataEventHandler> {

	public enum EventType {
		REFRESH, SHOW
	}

	private static Type<DataEventHandler> TYPE;

	private final int value;
	private final EventType eventType;

	public DataEvent(final EventType eventType, final int value) {
		this.value = value;
		this.eventType = eventType;
	}

	@Override
	public Type<DataEventHandler> getAssociatedType() {
		return getType();
	}

	public static Type<DataEventHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<DataEventHandler>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(final DataEventHandler handler) {
		handler.onDataEvent(this);
	}

	public int getValue() {
		return value;
	}

	public EventType getEventType() {
		return eventType;
	}
}
