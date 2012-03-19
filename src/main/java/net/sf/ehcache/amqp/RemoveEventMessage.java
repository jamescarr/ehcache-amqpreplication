package net.sf.ehcache.amqp;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.EventMessage;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class RemoveEventMessage extends AMQEventMessage {
	private static final long serialVersionUID = 1L;
	public RemoveEventMessage(Element element, Ehcache cache){
		super(EventMessage.REMOVE , element.getKey(), element, cache.getName(), cache.getGuid());
	}
}
