package net.sf.ehcache.amqp;

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.loader.CacheLoader;
import net.sf.ehcache.loader.CacheLoaderFactory;
import net.sf.ehcache.util.PropertyUtil;

/**
 * Factory to provide a loader which loads up any items that 
 * are not already in the cache. We'll figure out later how to 
 * handle a large number of them.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheLoaderFactory extends CacheLoaderFactory{

	@Override
	public CacheLoader createCacheLoader(Ehcache cache, Properties properties) {
		
		return new AMQCacheLoader(cache);
	}

}
