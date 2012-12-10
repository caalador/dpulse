package org.vaadin.mgrankvi.dpulse;

import org.vaadin.mgrankvi.dpulse.client.graph.GraphServerRpc;
import org.vaadin.mgrankvi.dpulse.client.graph.GraphState;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Label;

public class Graph extends AbstractComponent {

	private static final long serialVersionUID = 5338363670578270509L;
	private Integer[] ints = new Integer[0];

	private Label info;

	private final GraphServerRpc rpc = new GraphServerRpc() {

		@Override
		public void hoverElement(final int element) {
			if (info != null) {
				info.setValue(ints[element] + " ms");
			}
		}
	};

	public Graph() {
		registerRpc(rpc);
	}

	public void setInts(final Integer[] ints) {
		this.ints = ints;
		getState().values = ints;

		int max = 0;
		for (final Integer i : ints) {
			if (i > max) {
				max = i;
			}
		}
		getState().max = max;
	}

	public void setInfoLabel(final Label info) {
		this.info = info;
	}

	@Override
	public void setWidth(final String width) {
		super.setWidth(width);
		getState().size.width = (int) getWidth();
	}

	@Override
	public void setHeight(final String height) {
		super.setHeight(height);
		getState().size.height = (int) getHeight();
	}

	@Override
	protected GraphState getState() {
		return (GraphState) super.getState();
	}

	// @Override
	// public void paintContent(final PaintTarget target) throws PaintException
	// {
	// super.paintContent(target);
	//
	// // target.startTag(VDataPulse.OPTIONS_UIDL);
	// // target.addAttribute("width", getWidth());
	// // target.addAttribute("height", getHeight());
	// // target.endTag(VDataPulse.OPTIONS_UIDL);
	//
	// // target.addAttribute("values", ints);
	// // int max = 0;
	// // for (final Integer i : ints) {
	// // if (i > max) {
	// // max = i;
	// // }
	// // }
	// // target.addAttribute("maxValue", max);
	// }

}
