package net.sf.ehcache.amqp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.Test;


/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class Tomfoolery {
	Cache c2 = null;
	@Test
	public void test() throws InterruptedException{
		CacheManager manager = new CacheManager();
		CacheManager manager2 = new CacheManager();

		Cache c = manager.getCache("sampleCacheAsync");
		c2 = manager2.getCache("sampleCacheAsync");
		c.put(new Element("crap", "v"));
		
		Thread.sleep(1000); // replace with custom queue that waits
		assertThat(c2.getQuiet("crap").getValue(), equalTo((Serializable)"v"));
	}
	
	@Test
	public void daemon() throws InterruptedException{
		CacheManager manager = new CacheManager();
		Cache c = manager.getCache("sampleCacheAsync");
		
		while(true){
			System.out.println(c.get("crap"));
			Thread.sleep(2000);
		}
	}
}
