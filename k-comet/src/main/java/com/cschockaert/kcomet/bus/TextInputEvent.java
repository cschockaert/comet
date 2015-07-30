package com.cschockaert.kcomet.bus;


public class TextInputEvent extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5405227983339580906L;
	private String data;

	public TextInputEvent(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	};
	
	
}
