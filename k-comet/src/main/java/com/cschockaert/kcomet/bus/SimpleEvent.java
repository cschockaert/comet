package com.cschockaert.kcomet.bus;

import java.util.Map;

public abstract class SimpleEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7342483389374541119L;
	private Map<Object, Object> values;
	private String channel;
	
	public SimpleEvent() {
		super();
	}
	
	



	public SimpleEvent(Map<Object, Object> values, String channel) {
		super();
		this.values = values;
		this.channel = channel;
	}





	public Map<Object, Object> getValues() {
		return values;
	}

	public void setValues(Map<Object, Object> values) {
		this.values = values;
	}



	public String getChannel() {
		return channel;
	}



	public void setChannel(String channel) {
		this.channel = channel;
	}





	
}
