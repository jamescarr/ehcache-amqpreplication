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

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.CacheReplicator;
import net.sf.ehcache.distribution.EventMessage;

public class AMQCacheReplicator implements CacheReplicator {
	private final CachePeerLookup cachePeerLookup;
	public AMQCacheReplicator(CachePeerLookup cachePeerLookup){
		this.cachePeerLookup = cachePeerLookup;
	}
	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		AMQEventMessage message = new AMQEventMessage(EventMessage.REMOVE,
				element.getKey(), element, cache.getName());
		sendMessage(cache, message);
	}
	 /**
     * Called immediately after an element has been put into the cache. The
     * {@link net.sf.ehcache.Cache#put(net.sf.ehcache.Element)} method
     * will block until this method returns.
     * <p/>
     * Implementers may wish to have access to the Element's fields, including value, so the
     * element is provided. Implementers should be careful not to modify the element. The
     * effect of any modifications is undefined.
     *
     * @param cache   the cache emitting the notification
     * @param element the element which was just put into the cache.
     */
	public void notifyElementPut(final Ehcache cache, Element element)
			throws CacheException {
		AMQEventMessage message = new AMQEventMessage(EventMessage.PUT,
				element.getKey(), element, cache.getName());
		sendMessage(cache, message);

	}

	private void sendMessage(final Ehcache cache, AMQEventMessage message) {
		for (CachePeer peer : listRemoteCachePeers(cache)) {
			try {
				peer.send(Arrays.asList(message));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected List<CachePeer> listRemoteCachePeers(Ehcache cache) {
		return cachePeerLookup.listRemoteCachePeers(cache);
	}
	/**
     * Called immediately after an element has been put into the cache and the element already
     * existed in the cache. This is thus an update.
     * <p/>
     * The {@link net.sf.ehcache.Cache#put(net.sf.ehcache.Element)} method
     * will block until this method returns.
     * <p/>
     * Implementers may wish to have access to the Element's fields, including value, so the
     * element is provided. Implementers should be careful not to modify the element. The
     * effect of any modifications is undefined.
     *
     * @param cache   the cache emitting the notification
     * @param element the element which was just put into the cache.
     */ 
	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
//		throw new CacheException("not yet implemented");
	}

	public void notifyElementExpired(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	public void notifyElementEvicted(Ehcache cache, Element element) {
		// TODO Auto-generated method stub

	}

	public void notifyRemoveAll(Ehcache cache) {
		AMQEventMessage message = new AMQEventMessage(EventMessage.REMOVE_ALL,
				null, null, cache.getName());
		sendMessage(cache, message);
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
