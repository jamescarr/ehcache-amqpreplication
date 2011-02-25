package net.sf.ehcache.amqp;

import com.rabbitmq.client.impl.AMQChannel;
import com.rabbitmq.client.impl.AMQImpl;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class AMQResponder implements CacheEventListener{
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
	
	public Object clone() throws CloneNotSupportedException{
		return this;
	}

}
