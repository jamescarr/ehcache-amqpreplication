package net.sf.ehcache.amqp;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.AMQP.BasicProperties;

import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CachePeer;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCachePeer implements CachePeer {

	private final Channel channel;
	private final String cacheName;

	public AMQCachePeer(Channel channel, String cacheName) {
		this.channel = channel;
		this.cacheName = cacheName;
	}

	public void put(Element element) throws IllegalArgumentException,
			IllegalStateException, RemoteException {
		// TODO Auto-generated method stub
		
	}

	public boolean remove(Serializable key) throws IllegalStateException,
			RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeAll() throws RemoteException, IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	public void send(List eventMessages) throws RemoteException {
		AMQEventMessage message = (AMQEventMessage) eventMessages.get(0);
		BasicProperties basicProperties = new BasicProperties();
		basicProperties.setContentType("application/x-java-serialized-object");
		basicProperties.setHeaders(new HashMap<String, Object>() {
			{
				put("x-cache-name", cacheName);
			}
		});

		try {
			channel.basicPublish("ehcache.replication", message.getRoutingKey(),
					basicProperties, message.toBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public String getName() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGuid() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrl() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrlBase() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getKeys() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Element getQuiet(Serializable key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getElements(List keys) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
