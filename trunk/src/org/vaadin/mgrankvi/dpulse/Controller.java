package org.vaadin.mgrankvi.dpulse;

import org.vaadin.mgrankvi.dpulse.client.ui.VDataPulse.Type;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;

public class Controller extends HorizontalLayout {

	private static final long serialVersionUID = -2445848825755916027L;

	private final DataPulse connected;

	public Controller(final DataPulse connected) {
		this.connected = connected;
		init();
	}

	private void init() {
		final NativeSelect selectBox = new NativeSelect();

		selectBox.setNullSelectionAllowed(false);

		selectBox.addItem(Type.HEX);
		selectBox.addItem(Type.LINK_BOX);
		selectBox.addItem(Type.TILE);
		selectBox.select(Type.HEX);

		selectBox.setImmediate(true);
		selectBox.addListener(typeSelect);

		addComponent(selectBox);
	}

	private final ValueChangeListener typeSelect = new ValueChangeListener() {
		private static final long serialVersionUID = 6227019473959144398L;

		public void valueChange(final ValueChangeEvent event) {
			connected.setType((Type) event.getProperty().getValue());
			switch ((Type) event.getProperty().getValue()) {
			case HEX:
				connected.setWidth("25px");
				connected.setHeight("50px");
				break;
			case LINK_BOX:
				connected.setWidth("60px");
				connected.setHeight("20px");
				break;
			case TILE:
				connected.setWidth("50px");
				connected.setHeight("50px");
			}
		}
	};

}
