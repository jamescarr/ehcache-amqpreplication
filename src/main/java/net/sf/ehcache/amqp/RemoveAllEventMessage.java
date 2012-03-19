package net.sf.ehcache.amqp;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.EventMessage;

/**
 * Description Here.
 * 
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class RemoveAllEventMessage extends AMQEventMessage {

	public RemoveAllEventMessage(Ehcache cache) {
		super(EventMessage.REMOVE_ALL, null, null, cache.getName(), cache.getGuid());
	}

}
