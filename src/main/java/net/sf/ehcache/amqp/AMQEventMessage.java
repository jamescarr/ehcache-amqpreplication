package net.sf.ehcache.amqp;

import java.io.Serializable;

import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.EventMessage;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQEventMessage extends EventMessage{
	private static final long serialVersionUID = 1L;
	private String routingKey;

	public AMQEventMessage(int event, Serializable key, Element element, String cacheName) {
		super(event, key, element);
		routingKey = "ehcache.replicate."+cacheName;
	}



}
