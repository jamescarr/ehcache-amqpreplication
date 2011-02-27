package net.sf.ehcache.amqp;

import java.io.IOException;
import java.util.Properties;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

/**
 * A factory for creating JMSCacheReplicators.
 * 
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheReplicatorFactory extends CacheEventListenerFactory {
	private static final String EHCACHE_REPLICATION = "ehcache.replication";

	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		return new AMQCacheReplicator(new CachePeerLookup()); // not sure how this really is supposed to work
	}

}
