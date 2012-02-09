package org.vaadin.mgrankvi.dpulse;

import java.util.Map;

import org.vaadin.mgrankvi.dpulse.client.ui.VDataPulse;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Label;

@com.vaadin.ui.ClientWidget(org.vaadin.mgrankvi.dpulse.client.ui.VGraph.class)
public class Graph extends AbstractComponent {

	private static final long serialVersionUID = 5338363670578270509L;
	private Integer[] ints = new Integer[0];

	private Label info;

	public void setInts(final Integer[] ints) {
		this.ints = ints;
	}

	public void setInfoLabel(final Label info) {
		this.info = info;
	}

	@Override
	public void setWidth(final String width) {
		super.setWidth(width);
	}

	@Override
	public void setHeight(final String height) {
		super.setHeight(height);
	}

	@Override
	public void paintContent(final PaintTarget target) throws PaintException {
		super.paintContent(target);

		target.startTag(VDataPulse.OPTIONS_UIDL);
		target.addAttribute("width", getWidth());
		target.addAttribute("height", getHeight());
		target.endTag(VDataPulse.OPTIONS_UIDL);

		target.addAttribute("values", ints);
		int max = 0;
		for (final Integer i : ints) {
			if (i > max) {
				max = i;
			}
		}
		target.addAttribute("maxValue", max);
	}

	@Override
	public void changeVariables(final Object source, final Map<String, Object> variables) {
		super.changeVariables(source, variables);

		if (variables.containsKey("hover")) {
			if (info != null) {
				info.setValue(ints[((Integer) variables.get("hover"))] + " ms");
			}
		}
	}
}
