package net.sf.ehcache.amqp;

import static net.sf.ehcache.distribution.EventMessage.PUT;
import static net.sf.ehcache.distribution.EventMessage.REMOVE;
import static net.sf.ehcache.distribution.EventMessage.REMOVE_ALL;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import static net.sf.ehcache.amqp.TestHelper.inMemoryCacheManager;
/**
 * @author James R. Carr <james.r.carr@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class AMQPeerTest {
	private static final Envelope ENVELOPE = new Envelope(1L, false, "", "");
	@Captor
	ArgumentCaptor<BasicProperties> basicProperties;
	@Mock
	Channel channel;
	private CacheManager manager;
	private AMQCachePeer responder;

	@Before
	public void beforeEach() {
		manager = inMemoryCacheManager();
		responder = new AMQCachePeer(channel, manager, "ehcache.exchange");
	}

	

	@Test
	public void shouldHandlePut() throws IOException {
		Element element = new Element("hello", "test");
		byte[] bytes = messageAsBytes(PUT, element, "cacheA");

		responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(),
				bytes);

		Element value = manager.getCache("cacheA").get("hello");
		assertEquals(element, value);
	}

	@Test
	public void shouldNotHandlePutWhenTypeFieldIsEmpty() throws IOException {
		byte[] bytes = messageAsBytes(PUT, new Element("hello", "test"),
				"cacheA");

		responder.handleDelivery("", ENVELOPE, new BasicProperties(), bytes);

		assertFalse(manager.getCache("cacheA").isKeyInCache("hello"));
	}

	@Test
	public void shouldHandleRemoval() throws IOException {
		Element element = new Element("hello", "test");
		manager.getCache("cacheA").put(element);
		byte[] bytes = messageAsBytes(REMOVE, element, "cacheA");

		responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(),
				bytes);

		assertFalse(manager.getCache("cacheA").isKeyInCache("hello"));
	}

	@Test
	public void shouldHandleRemoveAll() throws IOException {
		manager.getCache("cacheA").put(new Element("a", "test"));
		manager.getCache("cacheA").put(new Element("b", "test"));
		manager.getCache("cacheA").put(new Element("c", "test"));
		byte[] bytes = messageAsBytes(REMOVE_ALL, null, "cacheA");

		responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(),
				bytes);

		int size = manager.getCache("cacheA").getSize();
		assertThat(size, equalTo(0));
	}

	@Test
	public void shouldDoNothingIfRecievedEventForCacheThatDoesNotExist()
			throws IOException {
		byte[] bytes = messageAsBytes(REMOVE_ALL, null, "cacheB");

		try {
			responder.handleDelivery("", ENVELOPE, basicPropertiesForMessage(),
					bytes);
		} catch (Exception e) {
			fail("should not have thrown exception");
		}
	}

	// sending
	@Test
	public void shouldAddRequiredHeadersToMessage() throws IOException {
		AMQEventMessage message = message(REMOVE_ALL, null, "cacheA");

		responder.send(Arrays.asList(message));

		verify(channel).basicPublish(anyString(), anyString(),
				basicProperties.capture(), eq(message.toBytes()));
		BasicProperties props = basicProperties.getValue();

		assertThat(props.getContentType(),
				equalTo("application/x-java-serialized-object"));
		assertThat(props.getType(), equalTo(AMQEventMessage.class.getName()));
	}

	@Test
	public void sendsRoutingKeyOutOnExchange() throws IOException {
		AMQEventMessage message = message(REMOVE_ALL, null, "cacheA");

		responder.send(Arrays.asList(message));

		verify(channel).basicPublish(eq("ehcache.exchange"), eq(message.getRoutingKey()),
				basicProperties.capture(), eq(message.toBytes()));

	}

	private byte[] messageAsBytes(int type, Element element, String cacheName) {
		AMQEventMessage message = message(type, element, cacheName);
		byte[] bytes = message.toBytes();
		return bytes;
	}

	private AMQEventMessage message(int type, Element element, String cacheName) {
		return new AMQEventMessage(type, element != null ? element.getKey()
				: "", element, cacheName);
	}

	private BasicProperties basicPropertiesForMessage() {
		BasicProperties basicProperties = new BasicProperties();
		basicProperties.setType(AMQEventMessage.class.getName());
		return basicProperties;
	}

}
