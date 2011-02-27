package net.sf.ehcache.amqp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.Test;


/**
 * A really crappy integration test. And by crappy I mean oh my god don't shit your pants
 * when you look at it kind of crappy. The test methods can only be ran in isolation and sometiems
 * pass, sometimes don't due to timing issues. Oh yeah and you need rabbitmq running for them to 
 * work too. 
 * 
 * I'll probably try to get rid of this and replace it with better, but for now it's all I got. ;)
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class Tomfoolery {
	Cache c2 = null;
	@Test
	public void replicatePut() throws InterruptedException{
		CacheManager manager = new CacheManager();
		CacheManager manager2 = new CacheManager();

		Cache c = manager.getCache("sampleCacheAsync");
		c2 = manager2.getCache("sampleCacheAsync");
		c.put(new Element("crap", "v"));
		
		Thread.sleep(1000); // replace with custom queue that waits
		assertThat(c2.getQuiet("crap").getValue(), equalTo((Serializable)"v"));
	}
	@Test
	public void replicateRemoval() throws InterruptedException{
		CacheManager manager = new CacheManager();
		CacheManager manager2 = new CacheManager();

		Cache c = manager.getCache("sampleCacheAsync");
		c2 = manager2.getCache("sampleCacheAsync");
		Element element = new Element("crap", "v");
		c.put(element);
		c2.put(element);
		
		c.remove(element.getKey());
		
		Thread.sleep(2000);
		assertFalse(c2.isKeyInCache(element.getKey()));
	}
	public static void main(String[] args) {
		CacheManager manager = new CacheManager();
		manager.getCache("sampleCacheAsync").removeAll();
	}

	@Test
	public void removeAll(){
		CacheManager manager = new CacheManager();
		manager.getCache("sampleCacheAsync").removeAll();
		System.out.println(manager.getCache("sampleCacheAsync"));
	}
	
	@Test
	public void daemon() throws InterruptedException{
		CacheManager manager = new CacheManager();
		Cache cache = manager.getCache("sampleCacheAsync");
		while(true){
			System.out.println(cache.getSize());
			Thread.sleep(2000);
		}
	}
	@Test
	public void removeAll2(){
		CacheManager manager = new CacheManager();
		Cache c = manager.getCache("sampleCacheAsync");
		c.removeAll();
	}
	@Test
	public void addATon() throws InterruptedException{
		CacheManager manager = new CacheManager();
		Cache c = manager.getCache("sampleCacheAsync");
		for(int i =0; i < 10000;i++){
			c.put(new Element("key"+i, i));
		}
		
	}
}
