package net.sf.ehcache.amqp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	private final AMQCachePeer amqCachePeer;

	public AMQCacheManagerPeerProvider(Channel channel,
			CacheManager cacheManager) {
		this.channel = channel;
		amqCachePeer = new AMQCachePeer(channel, cacheManager);
	}

	public void registerPeer(String nodeId) {
	}

	public void unregisterPeer(String nodeId) {

	}

	public List<CachePeer> listRemoteCachePeers(Ehcache cache)
			throws CacheException {
		ArrayList<CachePeer> peers = new ArrayList<CachePeer>();
		peers.add(amqCachePeer);
		return Collections.unmodifiableList(peers);
	}

    /**
     * Notifies providers to initialise themselves.
     *
     * @throws CacheException
     */
	public void init() {
		try {
			DeclareOk result = channel.queueDeclare();
			String queueName = result.getQueue();
			channel.queueBind(queueName, "ehcache.replication","ehcache.replicate");
			channel.basicConsume(queueName, true, amqCachePeer);
		} catch (IOException e) {
			throw new CacheException(e);
		}
	}

	/**
     * Providers may be doing all sorts of exotic things and need to be able to clean up on dispose.
     * In this case, we are simply closing the communication channel.
     *
     * @throws CacheException
     */
	public void dispose() throws CacheException {
		try {
			channel.close();
		} catch (IOException e) {
			throw new CacheException(e);
		}
	}

	/**
	 * Time for a cluster to form. This varies considerably, depending on the
	 * implementation.
	 * 
	 * @return the time in ms, for a cluster to form
	 */
	public long getTimeForClusterToForm() {
		return 0;
	}

	public String getScheme() {
		return "AMQP";
	}

	
}
