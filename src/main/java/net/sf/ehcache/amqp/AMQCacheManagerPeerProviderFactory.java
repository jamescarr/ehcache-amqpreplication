package net.sf.ehcache.amqp;

import java.io.IOException;
import java.util.Properties;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CacheManagerPeerProviderFactory;
import net.sf.ehcache.util.PropertyUtil;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 
 * @author James R. Carr <james.r.carr@gmail.com>
 *
 */
public class AMQCacheManagerPeerProviderFactory extends CacheManagerPeerProviderFactory{
	private final String DEFAULT_EXCHANGE = "ehcache.replication";
	/**
     * @param cacheManager the CacheManager instance connected to this peer provider
     * @param properties   implementation specific properties. These are configured as comma
     *                     separated name value pairs in ehcache.xml
     * @return a provider, already connected to a message queue and exchange
     */
	@Override
	public CacheManagerPeerProvider createCachePeerProvider(
			CacheManager cacheManager, Properties properties) {
		ConnectionFactory factory = ObjectMapper.createFrom(ConnectionFactory.class, properties);
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(DEFAULT_EXCHANGE, "direct");
			AMQCacheManagerPeerProvider amqCacheManagerPeerProvider = new AMQCacheManagerPeerProvider(channel, cacheManager, getExchangeName(properties));
			return amqCacheManagerPeerProvider;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private String getExchangeName(Properties properties) {
		String exchangeName = PropertyUtil.extractAndLogProperty("exchange", properties);
		return exchangeName == null?DEFAULT_EXCHANGE:exchangeName;
	}

}
