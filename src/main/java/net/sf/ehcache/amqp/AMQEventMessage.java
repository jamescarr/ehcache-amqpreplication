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
import java.io.ObjectOutputStream;
import java.io.Serializable;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.EventMessage;

import org.apache.commons.io.output.ByteArrayOutputStream;

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
