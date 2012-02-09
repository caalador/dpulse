package org.vaadin.mgrankvi.dpulse;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;

public class ConnectorController extends HorizontalLayout {

	private static final long serialVersionUID = -2094358616918678103L;

	DataPulse dpulse;

	public ConnectorController(final DataPulse connected) {
		dpulse = connected;
		final NativeSelect types = new NativeSelect();

		types.addItem("ECHO Connector");
		types.addItem("HTML Connector");
		types.addItem("Is Reachable Connector");

		addComponent(types);

	}

}
