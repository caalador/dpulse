package org.vaadin.mgrankvi.dpulse.client.graph;

import com.google.gwt.event.shared.GwtEvent;

public class HoverEvent extends GwtEvent<HoverEventHandler> {

	private static Type<HoverEventHandler> TYPE;

	private final int value;

	public HoverEvent(final int value) {
		this.value = value;
	}

	@Override
	public Type<HoverEventHandler> getAssociatedType() {
		return getType();
	}

	public static Type<HoverEventHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<HoverEventHandler>();
		}
		return TYPE;
	}

	@Override
	protected void dispatch(final HoverEventHandler handler) {
		handler.onHoverEvent(this);
	}

	public int getValue() {
		return value;
	}

}
