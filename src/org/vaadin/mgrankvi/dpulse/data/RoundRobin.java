package org.vaadin.mgrankvi.dpulse.data;

import java.util.LinkedList;
import java.util.List;

public class RoundRobin {

	private final Long[] values;
	int empty = 0;
	int start = 0;
	int end = 0;
	int position = start;

	public RoundRobin(final int fields) {
		values = new Long[fields];
	}

	public void addValue(final long value) {
		values[empty++] = value;
		end = (end + 1) % values.length;
		if (end == start) {
			if (position == start) {
				position = (position + 1) % values.length;
			}
			start = (start + 1) % values.length;
		}
		if (empty == values.length) {
			empty = 0;
		}
	}

	public Long nextValue() {
		final Long next = values[position];
		position = (position + 1) % values.length;
		return next;
	}

	public List<Long> getValues() {
		final List<Long> valueList = new LinkedList<Long>();
		for (int i = start; i % values.length != end; i++) {
			valueList.add(values[i % values.length]);
		}
		return valueList;
	}
}
