package net.sf.ehcache.amqp;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CacheReplicator;

public class AMQCacheReplicator implements CacheReplicator {

	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public void notifyElementPut(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub
		
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
		return false;
	}

	public boolean notAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean alive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this;
	}

	
}
