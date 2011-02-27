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
import net.sf.ehcache.Status;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.CacheReplicator;

public class AMQCacheReplicator implements CacheReplicator {
	private final CachePeerLookup cachePeerLookup;
	private Status status;
	
	public AMQCacheReplicator(CachePeerLookup cachePeerLookup){
		this.cachePeerLookup = cachePeerLookup;
		status = Status.STATUS_ALIVE;
	}
	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		sendMessage(cache, new RemoveEventMessage(element, cache));
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
		sendMessage(cache, new PutEvent(element, cache));
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
		sendMessage(cache, new PutEvent(element, cache));
	}
	/**
     * Called immediately after an element is <i>found</i> to be expired. The
     * {@link net.sf.ehcache.Cache#remove(Object)} method will block until this method returns.
     * <p/>
     * As the {@link Element} has been expired, only what was the key of the element is known.
     * <p/>
     * Elements are checked for expiry in ehcache at the following times:
     * <ul>
     * <li>When a get request is made
     * <li>When an element is spooled to the diskStore in accordance with a MemoryStore
     * eviction policy
     * <li>In the DiskStore when the expiry thread runs, which by default is
     * {@link net.sf.ehcache.Cache#DEFAULT_EXPIRY_THREAD_INTERVAL_SECONDS}
     * </ul>
     * If an element is found to be expired, it is deleted and this method is notified.
     * <p/>
     * This message is not replicated by this replicator.
     *
     * @param cache   the cache emitting the notification
     * @param element the element that has just expired
     *                <p/>
     *                Deadlock Warning: expiry will often come from the <code>DiskStore</code>
     *                expiry thread. It holds a lock to the DiskStorea the time the
     *                notification is sent. If the implementation of this method calls into a
     *                synchronized <code>Cache</code> method and that subsequently calls into
     *                DiskStore a deadlock will result. Accordingly implementers of this method
     *                should not call back into Cache.
     */
    public void notifyElementExpired(Ehcache cache, Element element) {
		// noop
	}
	/**
     * Called immediately after an element is evicted from the cache. Evicted in this sense
     * means evicted from one store and not moved to another, so that it exists nowhere in the
     * local cache.
     * <p/>
     * In a sense the Element has been <i>removed</i> from the cache, but it is different,
     * thus the separate notification.
     * <p/>
     * This message is not replicated by this replicator.
     *
     * @param cache   the cache emitting the notification
     * @param element the element that has just been evicted
     */
	public void notifyElementEvicted(Ehcache cache, Element element) {
		// noop

	}

	public void notifyRemoveAll(Ehcache cache) {
		sendMessage(cache, new RemoveAllEventMessage(cache));
	}
	/**
     * Give the listener a chance to cleanup and free resources when no longer needed.
     * Since queues are temporary and auto-delete, there is no need to purge any 
     * messages that are "in transit" as they will just disappear.
     */
	public void dispose() {
		status = Status.STATUS_UNINITIALISED;
	}
	 /**
     * Returns whether update is through copy or invalidate
     * 
     * For now I'm hardcoding this bitch to true. I'll change it if
     * I see reason to. 
     * 
     * @return true if update is via copy, else false if invalidate
     */
	public boolean isReplicateUpdatesViaCopy() {
		return true;
	}
	
	/**
     * {@inheritDoc}
     */
	public boolean notAlive() {
		return !alive();
	}

	/**
     * {@inheritDoc}
     */
	public boolean alive() {
		return status == Status.STATUS_ALIVE;
	}
	/**
     * Creates a clone of this listener. This method will only be called by ehcache before a
     * cache is initialized.
     * <p/>
     * This may not be possible for listeners after they have been initialized. Implementations
     * should throw CloneNotSupportedException if they do not support clone.
     *
     * @return a clone
     * @throws CloneNotSupportedException if the listener could not be cloned.
     */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new AMQCacheReplicator(cachePeerLookup);
	}

}
