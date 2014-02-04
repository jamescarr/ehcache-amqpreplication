package net.sf.ehcache.amqp;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.EventMessage;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class PutEvent extends AMQEventMessage{
	private static final long serialVersionUID = 1L;
	public PutEvent(Element element, Ehcache cache){
		super(EventMessage.PUT, element.getKey(), element, cache.getName(), cache.getGuid());
	}
}
