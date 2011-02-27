package net.sf.ehcache.amqp;

import static java.util.Arrays.asList;
import static net.sf.ehcache.amqp.TestHelper.inMemoryCacheManager;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.rmi.RemoteException;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.distribution.CachePeer;
import net.sf.ehcache.distribution.EventMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.rabbitmq.client.Channel;
/**
 * @author James R. Carr <james.r.carr@gmail.com>
 */
@RunWith(MockitoJUnitRunner.class)
public class AMQCacheReplicatorTest {
	private AMQCacheReplicator replicator;
	@Spy CacheManager cacheManager = inMemoryCacheManager();
	@Mock Channel channel;
	@Mock AMQCacheManagerPeerProvider peerProvider;
	@Mock CachePeer peer;
	@Mock CachePeerLookup lookup;
	@Mock Ehcache cache;
	@Captor ArgumentCaptor<List<AMQEventMessage>> messages;
	@Before
	public void beforeEach(){
		replicator = new AMQCacheReplicator(lookup);
		given(lookup.listRemoteCachePeers(cache)).willReturn(asList(peer));
	}
	
	@Test
	public void shouldSendPutEventOnNotify() throws RemoteException{
		replicator.notifyElementPut(cache, new Element("a", 54));
		
		AMQEventMessage messageSent = getMessageSentToPeer();
		assertThat(messageSent.getEvent(), equalTo(EventMessage.PUT));
	}
	@Test
	public void shouldSendPutEventOnRemove() throws RemoteException{
		replicator.notifyElementRemoved(cache, new Element("a", 54));
		
		AMQEventMessage messageSent = getMessageSentToPeer();
		assertThat(messageSent.getEvent(), equalTo(EventMessage.REMOVE));
	}

	private AMQEventMessage getMessageSentToPeer() throws RemoteException {
		verify(peer).send(messages.capture());
		AMQEventMessage messageSent = messages.getValue().get(0);
		return messageSent;
	}
	
}
