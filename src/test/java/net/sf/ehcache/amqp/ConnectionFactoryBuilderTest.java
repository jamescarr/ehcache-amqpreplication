package net.sf.ehcache.amqp;

import java.util.Properties;

import org.junit.Test;

import com.rabbitmq.client.ConnectionFactory;
import static org.junit.Assert.*;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class ConnectionFactoryBuilderTest {
	@Test
	public void shouldDefaultProperties(){
		ConnectionFactoryBuilder builder = new ConnectionFactoryBuilder();
		ConnectionFactory cf = builder.createFrom(new Properties());
		
		assertEquals(cf.getHost(), "localhost");
		assertEquals(cf.getPassword(), "guest");
		assertEquals(cf.getUsername(), "guest");
		assertEquals(cf.getVirtualHost(), "/");
	}
}
