package net.sf.ehcache.amqp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.CacheReplicator;
import net.sf.ehcache.distribution.EventMessage;

public class AMQCacheReplicator implements CacheReplicator {


	public AMQCacheReplicator(Channel channel, String exchangeName) {
	}

	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {

	}

	public void notifyElementPut(final Ehcache cache, Element element)
			throws CacheException {
		AMQEventMessage message = new AMQEventMessage(EventMessage.PUT,
				element.getKey(), element, cache.getName());
		for (CachePeer peer : listRemoteCachePeers(cache)) {
			try {
				peer.send(Arrays.asList(message));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}

	protected static List<CachePeer> listRemoteCachePeers(Ehcache cache) {
		CacheManagerPeerProvider provider = cache.getCacheManager()
				.getCacheManagerPeerProvider("AMQP");
		return provider.listRemoteCachePeers(cache);
	}

	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	public void notifyElementExpired(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	public void notifyElementEvicted(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	public void notifyRemoveAll(Ehcache cache) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isReplicateUpdatesViaCopy() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean notAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean alive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this;
	}

}
