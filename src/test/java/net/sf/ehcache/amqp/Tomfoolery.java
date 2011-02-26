package net.sf.ehcache.amqp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.statistics.CacheUsageListener;

import org.junit.Test;
import org.mockito.Mockito;


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
	
	@Test
	public void replicateRemoveAll() throws InterruptedException{
		CacheManager manager = new CacheManager();
		CacheManager manager2 = new CacheManager();

		Cache c = manager.getCache("sampleCacheAsync");
		c2 = manager2.getCache("sampleCacheAsync");
		for(int i =0; i < 5000;i++){
			c.put(new Element("key"+i, i));
		}
		Thread.sleep(1000);
		assertThat(c2.getSize(), equalTo(5000));
//		c.registerCacheUsageListener(new CacheUsageListener() {
//			
//			public void notifyXaRollback() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyXaCommit() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyTimeTakenForGet(long millis) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyStatisticsEnabledChanged(boolean enableStatistics) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyStatisticsCleared() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyStatisticsAccuracyChanged(int statisticsAccuracy) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyRemoveAll() {
//				// TODO Auto-generated method stub
//				System.out.println("remove all");
//			}
//			
//			public void notifyCacheSearch(long executeTime) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheMissedWithNotFound() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheMissedWithExpired() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheMissOnDisk() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheMissOffHeap() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheMissInMemory() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheHitOnDisk() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheHitOffHeap() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheHitInMemory() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheElementUpdated() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheElementRemoved() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheElementPut() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheElementExpired() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void notifyCacheElementEvicted() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			public void dispose() {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		c2.removeAll();
//		Thread.sleep(100);
//		assertThat(c.getSize(), equalTo(0));
	}
}
