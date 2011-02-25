package net.sf.ehcache.amqp;

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.loader.CacheLoader;
import net.sf.ehcache.loader.CacheLoaderFactory;
import net.sf.ehcache.util.PropertyUtil;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheLoaderFactory extends CacheLoaderFactory{

	@Override
	public CacheLoader createCacheLoader(Ehcache cache, Properties properties) {
		String vhost = PropertyUtil.extractAndLogProperty("vhost", properties);
		String username = PropertyUtil.extractAndLogProperty("vhost", properties);
		String password = PropertyUtil.extractAndLogProperty("vhost", properties);
		String host = PropertyUtil.extractAndLogProperty("host", properties);
		String port = PropertyUtil.extractAndLogProperty("port", properties);
		
		return null;
	}

}
