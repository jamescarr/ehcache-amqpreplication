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
