package com.cschockaert.kcomet.bus;

import java.util.Map;

public class ReceivedEvent extends SimpleEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7356137854785181895L;

	public ReceivedEvent(Map<Object, Object> data, String channel) {
		super(data, channel);
	}

}
