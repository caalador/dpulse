package org.vaadin.mgrankvi.dpulse.client.graph;

import org.vaadin.mgrankvi.dpulse.client.Size;

import com.vaadin.shared.AbstractComponentState;

public class GraphState extends AbstractComponentState {

	private static final long serialVersionUID = 9155585728953144033L;

	public Size size = new Size();

	public Integer[] values = new Integer[0];
	public int max;
}
