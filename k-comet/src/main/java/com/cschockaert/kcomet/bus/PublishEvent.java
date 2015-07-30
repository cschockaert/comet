package com.cschockaert.kcomet.bus;

import java.util.Map;

public class PublishEvent extends SimpleEvent {

	public PublishEvent(Map<Object, Object> data, String channel) {
		super(data, channel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4814365766463099841L;

}
