/**
 *  Copyright 2011 James Carr
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sf.ehcache.amqp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;

/**
 * Creates a single AMQCachePeer which handles both publishing of cache events as well
 * as handling cache events received from the amqp queue.
 * 
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheManagerPeerProvider implements CacheManagerPeerProvider {
	private static final Logger LOG = LoggerFactory.getLogger(AMQCacheManagerPeerProvider.class);
	private final Channel channel;
	private final String exchangeName;
	private final AMQCachePeer amqCachePeer;
	private final List<CachePeer> cachePeers;
	
	public AMQCacheManagerPeerProvider(Channel channel, CacheManager cacheManager, String exchangeName) {
		this.channel = channel;
		this.exchangeName = exchangeName;
		this.amqCachePeer = new AMQCachePeer(channel, cacheManager, exchangeName);
		this.cachePeers = Arrays.asList((CachePeer)amqCachePeer);
	}

	public void registerPeer(String nodeId) {
        throw new CacheException("Not implemented for AMQP");
	}

	public void unregisterPeer(String nodeId) {
        throw new CacheException("Not implemented for AMQP");
	}
	
	/**
	 * Always only one cache peer for AMQP implementation... one peer delegates
	 * responding to all queue messages and sending all cache events on the exchange,
	 * 
     * @return a list of {@link CachePeer} peers for the given cache, excluding the local peer.
     */
	public List<CachePeer> listRemoteCachePeers(Ehcache cache)
			throws CacheException {
		return cachePeers;
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
			channel.queueBind(queueName, exchangeName,"ehcache.replicate");
			channel.basicConsume(queueName, true, amqCachePeer);
			LOG.info("Binding queue " + queueName + " to exchange " + exchangeName + " on key ehcache.replicate");
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
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
			LOG.info("disposing of provider and closing amqp channel.");
			channel.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
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

	/**
	 * The sheme this provider uses
	 */
	public String getScheme() {
		return "AMQP";
	}
}
