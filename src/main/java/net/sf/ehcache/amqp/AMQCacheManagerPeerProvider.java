package net.sf.ehcache.amqp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.impl.LongStringHelper;
import com.rabbitmq.client.impl.LongStringHelper.ByteArrayLongString;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;

/**
 * Description Here.
 * 
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheManagerPeerProvider implements CacheManagerPeerProvider {
	private final Channel channel;
	private final CacheManager cacheManager;

	public AMQCacheManagerPeerProvider(Channel channel,
			CacheManager cacheManager) {
		this.channel = channel;
		this.cacheManager = cacheManager;
	}

	public void registerPeer(String nodeId) {
		System.out.println(nodeId);
	}

	public void unregisterPeer(String nodeId) {

	}

	public List<CachePeer> listRemoteCachePeers(Ehcache cache)
			throws CacheException {
		ArrayList<CachePeer> peers = new ArrayList<CachePeer>();
		peers.add(new AMQCachePeer(channel, cache.getName()));
		return peers;
	}

	public void init() {
		try {
			DeclareOk result = channel.queueDeclare();
			String queueName = result.getQueue();
			channel.queueBind(queueName, "ehcache.replication",
					"ehcache.replicate");
			System.out.println("bound queue " + queueName);
			
			DefaultConsumer callback = new DefaultConsumer(channel) {
			     @Override public void handleDelivery(String consumerTag,
			                                          Envelope envelope,
			                                          BasicProperties properties,
			                                          byte[] body)
			         throws IOException
			     {
			         long deliveryTag = envelope.getDeliveryTag();
			        	 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(body));
			        	 try {
							AMQEventMessage m = (AMQEventMessage) in.readObject();
							ByteArrayLongString cacheName = (ByteArrayLongString) properties.getHeaders().get("x-cache-name");
							cacheManager.getCache(cacheName.toString()).put(m.getElement());
							
			        	 } catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
			     }
			 };
			 channel.setDefaultConsumer(callback);
			channel.basicConsume(queueName, true,
				     callback);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dispose() throws CacheException {
		// close channel
		System.out.println("dispose");
	}

	/**
	 * Time for a cluster to form. This varies considerably, depending on the
	 * implementation.
	 * 
	 * @return the time in ms, for a cluster to form
	 */
	public long getTimeForClusterToForm() {
		System.out.println("time for cluster to form called");
		return 0;
	}

	public String getScheme() {
		return "AMQP";
	}

}
