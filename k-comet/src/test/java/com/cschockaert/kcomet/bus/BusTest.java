package com.cschockaert.kcomet.bus;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.junit.Test;

import com.cschockaert.kcomet.bus.Bus;
import com.cschockaert.kcomet.bus.PublishEvent;

@Listener
public class BusTest {

	List<PublishEvent> publishEvents = new ArrayList<PublishEvent>();
	
	@Test
	public void testPublishAndHandle() {
		Bus.BUS.subscribe(this);
		
		assertEquals(0, publishEvents.size());
		Bus.BUS.publish(new PublishEvent(Collections.singletonMap("key", "val"), "test1"));
		
		assertEquals(1, publishEvents.size());
		
	}
	
	@Handler
	public void handleEvent1(PublishEvent event) {
		publishEvents.add(event);
	}

}
