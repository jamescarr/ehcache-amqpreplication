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
public class ObjectMapperTest {
	@Test
	public void shouldDefaultProperties(){
		ConnectionFactory cf = ObjectMapper.createFrom(ConnectionFactory.class, new Properties());
		
		assertEquals(cf.getHost(), "localhost");
		assertEquals(cf.getPassword(), "guest");
		assertEquals(cf.getUsername(), "guest");
		assertEquals(cf.getVirtualHost(), "/");
		assertEquals(cf.getPort(), 5672);
	}
	
	@Test
	public void shouldFillInAnyPropertiesProvided(){
		ConnectionFactory cf = ObjectMapper.createFrom(ConnectionFactory.class, properties(
					"host", "yourhost", 
					"username", "james",
					"port", "5922"));
		
		assertEquals(cf.getHost(), "yourhost");
		assertEquals(cf.getPassword(), "guest");
		assertEquals(cf.getUsername(), "james");
		assertEquals(cf.getVirtualHost(), "/");
		assertEquals(cf.getPort(), 5922);
		
	}

	private Properties properties(String... props) {
		Properties properties = new Properties();
		for(int i = 0; i < props.length; i+=2){
			properties.put(props[i], props[i+1]);
		}
		return properties;
	}
}
