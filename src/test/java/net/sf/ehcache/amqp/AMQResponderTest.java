package net.sf.ehcache.amqp;

import static net.sf.ehcache.distribution.EventMessage.PUT;
import static net.sf.ehcache.distribution.EventMessage.REMOVE;
import static net.sf.ehcache.distribution.EventMessage.REMOVE_ALL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;


/**
 * @author James R. Carr <james.r.carr@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class AMQResponderTest {
	private static final Envelope ENVELOPE = new Envelope(1L, false, "", "");
	@Mock Channel channel;
	private CacheManager manager;
	private AMQCachePeer responder;
	@Before
	public void beforeEach(){
		Configuration configuration = new Configuration();
		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setName("cacheA");
		cacheConfiguration.setMaxElementsInMemory(100);
		configuration.addCache(cacheConfiguration);
		manager = new CacheManager(configuration);
		responder = new AMQCachePeer(channel, manager);
	}
	@Test
	public void shouldHandlePut() throws IOException{
		Element element = new Element("hello", "test");
		byte[] bytes = message(PUT, element, "cacheA");
		
		responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(), bytes);
		
		Element value = manager.getCache("cacheA").get("hello");
		assertEquals(element, value);
	}
	@Test
	public void shouldNotHandlePutWhenTypeFieldIsEmpty() throws IOException{
		byte[] bytes = message(PUT, new Element("hello", "test"), "cacheA");
		
		responder.handleDelivery("", ENVELOPE, new BasicProperties(), bytes);
		
		assertFalse(manager.getCache("cacheA").isKeyInCache("hello"));
	}
	
	@Test
	public void shouldHandleRemoval() throws IOException{
		Element element = new Element("hello", "test");
		manager.getCache("cacheA").put(element);
		byte[] bytes = message(REMOVE, element, "cacheA");
		
		responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(), bytes);
		
		assertFalse(manager.getCache("cacheA").isKeyInCache("hello"));
	}
	@Test
	public void shouldHandleRemoveAll() throws IOException{
		manager.getCache("cacheA").put(new Element("a", "test"));
		manager.getCache("cacheA").put(new Element("b", "test"));
		manager.getCache("cacheA").put(new Element("c", "test"));
		byte[] bytes = message(REMOVE_ALL, null, "cacheA");
		
		responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(), bytes);
		
		int size = manager.getCache("cacheA").getSize();
		assertThat(size, equalTo(0));
	}
	
	@Test
	public void shouldDoNothingIfRecievedEventForCacheThatDoesNotExist() throws IOException{
		byte[] bytes = message(REMOVE_ALL, null, "cacheB");
		
		try{
			responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(), bytes);
		}catch(Exception e){
			fail("should not have thrown exception");
		}
	}
	private byte[] message(int type, Element element, String cacheName) {
		AMQEventMessage message = new AMQEventMessage(type, element!=null?element.getKey():"", element, cacheName);
		byte[] bytes = message.toBytes();
		return bytes;
	}
	private BasicProperties basicPropertiesForMessage() {
		BasicProperties basicProperties = new BasicProperties();
		basicProperties.setType(AMQEventMessage.class.getName());
		return basicProperties;
	}
	
	
}
