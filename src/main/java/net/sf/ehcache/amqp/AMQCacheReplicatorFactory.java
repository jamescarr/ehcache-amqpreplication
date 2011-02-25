package net.sf.ehcache.amqp;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;
/**
 * A factory for creating JMSCacheReplicators.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheReplicatorFactory extends CacheEventListenerFactory{
	@Override
	public CacheEventListener createCacheEventListener(Properties properties) {
		// TODO Auto-generated method stub
		return null;
	}

}
