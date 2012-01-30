package org.vaadin.mgrankvi.dpulse;

import java.util.List;

import org.vaadin.mgrankvi.dpulse.data.ConnectionInformation;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class InfoWindow extends Window {

	private static final long serialVersionUID = -4779715268052046252L;

	public InfoWindow(final ConnectionInformation info) {
		super(info.getDescription());

		setStyleName(Reindeer.WINDOW_LIGHT);
		((VerticalLayout) getContent()).setSpacing(true);
		setWidth("550px");
		center();

		addComponent(new Label("Target: " + info.getTarget()));

		if (info.getException() != null) {
			addComponent(new Label(info.getException()));
		}

		final HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);
		hl.addComponent(new Label("Ping times:"));
		final VerticalLayout vl = new VerticalLayout();
		hl.addComponent(vl);
		for (final Long time : info.getPingTimes()) {
			vl.addComponent(new Label(time + "ms"));
		}

		Graph graph = new Graph();
		graph.setWidth("250px");
		graph.setHeight("175px");
		List<Long> times = info.getPingTimes();
		Integer[] ints = new Integer[times.size()];
		for (int i = 0; i < times.size(); i++) {
			ints[i] = times.get(i).intValue();
		}

		graph.setInts(ints);
		hl.addComponent(graph);

		addComponent(hl);
	}
}
