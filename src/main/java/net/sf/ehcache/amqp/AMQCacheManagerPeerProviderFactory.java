package net.sf.ehcache.amqp;

import java.io.IOException;
import java.util.Properties;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CacheManagerPeerProviderFactory;

public class AMQCacheManagerPeerProviderFactory extends CacheManagerPeerProviderFactory{

	@Override
	public CacheManagerPeerProvider createCachePeerProvider(
			CacheManager cacheManager, Properties properties) {
		ConnectionFactory factory = ObjectMapper.createFrom(ConnectionFactory.class, properties);
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			
			AMQCacheManagerPeerProvider amqCacheManagerPeerProvider = new AMQCacheManagerPeerProvider(channel, cacheManager);
			return amqCacheManagerPeerProvider;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
