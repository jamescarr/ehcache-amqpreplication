package net.sf.ehcache.amqp;

import static net.sf.ehcache.distribution.EventMessage.PUT;
import static net.sf.ehcache.distribution.EventMessage.REMOVE;
import static net.sf.ehcache.distribution.EventMessage.REMOVE_ALL;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.DefaultConsumer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CachePeer;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCachePeer extends DefaultConsumer implements CachePeer {
	private static final String MESSAGE_TYPE_NAME = AMQEventMessage.class.getName();
	private static final Logger LOG = LoggerFactory.getLogger(AMQResponder.class);
	private final Channel channel;
	private final CacheManager cacheManager;

	public AMQCachePeer(Channel channel, CacheManager cacheManager) {
		super(channel);
		this.channel = channel;
		this.cacheManager = cacheManager;
	}

	public void put(Element element) throws IllegalArgumentException,
			IllegalStateException, RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public boolean remove(Serializable key) throws IllegalStateException,
			RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public void removeAll() throws RemoteException, IllegalStateException {
		 throw new RemoteException("Not implemented for AMQP");
		
	}

	public void send(List eventMessages) throws RemoteException {
		AMQEventMessage message = (AMQEventMessage) eventMessages.get(0);
		BasicProperties basicProperties = new BasicProperties();
		basicProperties.setContentType("application/x-java-serialized-object");
		basicProperties.setType(AMQEventMessage.class.getName());
		try {
			channel.basicPublish("ehcache.replication", message.getRoutingKey(),
					basicProperties, message.toBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getName() throws RemoteException {
		return cacheManager.getName() + " AMQCachePeer";
	}

	public String getGuid() throws RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public String getUrl() throws RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public String getUrlBase() throws RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public List<?> getKeys() throws RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public Element getQuiet(Serializable key) throws RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}

	public List getElements(List keys) throws RemoteException {
		throw new RemoteException("Not implemented for AMQP");
	}
	
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		if(MESSAGE_TYPE_NAME.equals(properties.getType())){
			AMQEventMessage message = readMessageIn(body);
			Cache cache = cacheManager.getCache(message.getCacheName());
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
