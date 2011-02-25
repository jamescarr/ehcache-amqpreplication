package net.sf.ehcache.amqp;

import java.util.List;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheManagerPeerProvider implements CacheManagerPeerProvider {

	public void registerPeer(String nodeId) {
		// TODO Auto-generated method stub

	}

	public void unregisterPeer(String nodeId) {
		// TODO Auto-generated method stub

	}

	public List<CachePeer> listRemoteCachePeers(Ehcache cache) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public void dispose() throws CacheException {
		// TODO Auto-generated method stub

	}

	public long getTimeForClusterToForm() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getScheme() {
		return "AMQP";
	}

}
