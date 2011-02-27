package net.sf.ehcache.amqp;

import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.CachePeer;

/**
 * Simple wrapper around the logic to lookup the cache peer provider
 * for a particular cache. 
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class CachePeerLookup{
	@SuppressWarnings("unchecked")
	public List<CachePeer> listRemoteCachePeers(Ehcache cache) {
		CacheManagerPeerProvider provider = cache.getCacheManager()
				.getCacheManagerPeerProvider("AMQP");
		return provider.listRemoteCachePeers(cache);
	}
}
