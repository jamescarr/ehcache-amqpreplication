package net.sf.ehcache.amqp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.io.output.ByteArrayOutputStream;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.EventMessage;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQEventMessage extends EventMessage{
	private static final long serialVersionUID = 1L;
	private String routingKey;
	private final String cacheName;

	public AMQEventMessage(int event, Serializable key, Element element, String cacheName) {
		super(event, key, element);
		this.cacheName = cacheName;
		routingKey = "ehcache.replicate";
	}
	

	public String getCacheName() {
		return cacheName;
	}


	public String getRoutingKey() {
		return routingKey;
	}

	public byte[] toBytes() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(this);
		} catch (IOException e) {
			throw new CacheException(e);
		}
		return baos.toByteArray();
	}

	



}
