package com.cschockaert.kcomet.bus;

import net.engio.mbassy.bus.BusFactory;
import net.engio.mbassy.bus.common.ISyncMessageBus;

public class Bus {

	@SuppressWarnings("unchecked")
	public static final ISyncMessageBus<Event, ?>  BUS = BusFactory.SynchronousOnly();
	
}
