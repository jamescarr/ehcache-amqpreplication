package net.sf.ehcache.amqp;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

public class TestHelper {
	public static final String SIMPLE_CACHE  = "cacheA";
	public static CacheManager inMemoryCacheManager() {
		Configuration configuration = new Configuration();
		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setName(SIMPLE_CACHE);
		cacheConfiguration.setMaxElementsInMemory(100);
		configuration.addCache(cacheConfiguration);
		return new CacheManager(configuration);
	}
}
