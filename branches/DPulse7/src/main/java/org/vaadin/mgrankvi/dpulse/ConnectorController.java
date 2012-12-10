package org.vaadin.mgrankvi.dpulse;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;

public class ConnectorController extends HorizontalLayout {

	private static final long serialVersionUID = -2094358616918678103L;

	enum Type {
		ECHO, HTML, REACHABLE
	}

	final NativeSelect types;

	public ConnectorController() {
		types = new NativeSelect("Connector type");

		types.addItem(Type.HTML);
		types.addItem(Type.ECHO);
		types.addItem(Type.REACHABLE);

		types.setItemCaption(Type.HTML, "HTML Connector");
		types.setItemCaption(Type.ECHO, "ECHO Connector");
		types.setItemCaption(Type.REACHABLE, "Is Reachable Connector");

		types.setNullSelectionAllowed(false);
		types.select(Type.HTML);

		addComponent(types);

	}

	public Type getType() {
		return (Type) types.getValue();
	}
}
