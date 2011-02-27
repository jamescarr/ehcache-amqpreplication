package net.sf.ehcache.amqp;

import static net.sf.ehcache.amqp.TestHelper.inMemoryCacheManager;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.distribution.CachePeer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;

@RunWith(MockitoJUnitRunner.class)
public class AMQCacheManagerPeerProviderFactoryTest {
	private static final String EXCHANGE_NAME = "EXCHANGE_NAME";
	@Captor ArgumentCaptor<AMQCachePeer> amqCachePeer;
	@Mock Channel channel;
	private AMQCacheManagerPeerProvider peerProvider;
	private CacheManager cacheManager = inMemoryCacheManager();

	@Before
	public void beforeEach() {
		peerProvider = new AMQCacheManagerPeerProvider(channel, cacheManager,
				EXCHANGE_NAME);
	}

	@Test
	public void initShouldInitializeAQueueWithThePeerAsListener() throws IOException {
		givenTempraryQueueDeclaredWithName("random");
		
		peerProvider.init();
		
		verify(channel).basicConsume(eq("random"),eq(true),amqCachePeer.capture());
		AMQCachePeer peer = amqCachePeer.getValue();
		assertNotNull(peer);
	}
	@Test
	public void shouldCloseTheChannelOnDispose() throws IOException{
		peerProvider.dispose();
		
		verify(channel).close();
	}
	@Test
	public void messagesSentShouldContainHeadersSpecifyingContentTypeAndClassName(){
		List<CachePeer> peersForCache = peerProvider.listRemoteCachePeers(cacheManager.getCache(TestHelper.SIMPLE_CACHE));
		
		
		List<CachePeer> peersForNull = peerProvider.listRemoteCachePeers(null);
		
		assertThat(peersForCache.size(), equalTo(1));
		assertThat(peersForNull.size(), equalTo(1));
		assertSame(peersForCache.get(0), peersForNull.get(0));
	}
	private void givenTempraryQueueDeclaredWithName(String queueName) {
		try {
			DeclareOk result = mock(DeclareOk.class);
			given(channel.queueDeclare()).willReturn(result);
			given(result.getQueue()).willReturn(queueName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
