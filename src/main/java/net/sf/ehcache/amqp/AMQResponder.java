package net.sf.ehcache.amqp;

import static net.sf.ehcache.distribution.EventMessage.PUT;
import static net.sf.ehcache.distribution.EventMessage.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
public class AMQResponder extends DefaultConsumer {
	private static final String MESSAGE_TYPE_NAME = AMQEventMessage.class.getName();
	private static final Logger LOG = LoggerFactory.getLogger(AMQResponder.class);
	private final CacheManager manager;
	public AMQResponder(Channel channel, CacheManager manager) {
		super(channel);
		this.manager = manager;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		if(MESSAGE_TYPE_NAME.equals(properties.getType())){
			AMQEventMessage message = readMessageIn(body);
			Cache cache = manager.getCache(message.getCacheName());
			if(cache==null){
				handleMissingCache(message.getCacheName());
			}
			else{
				handleCacheEvent(message, cache);
			}
		}else{
			LOG.warn("Received non cache message of unknown type");
		}
		
	}

	private void handleMissingCache(String cacheName) {
		LOG.warn("Recieved replication update for cache not present: " + cacheName);
	}

	private void handleCacheEvent(AMQEventMessage message, Cache cache) {
		switch (message.getEvent()) {
			case PUT:
				cache.put(message.getElement());				
				break;
			case REMOVE:
				cache.remove(message.getElement().getKey());
				break;		
			case REMOVE_ALL:
				cache.removeAll();
				break;
			default:
				LOG.warn("Don't understand how to handle event of type " + message.getEvent());
				break;
		}
	}

	private AMQEventMessage readMessageIn(byte[] body) throws IOException {
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(
				body));
		Object o = null;
		try {
			o = in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return (AMQEventMessage) o;
	}

}
