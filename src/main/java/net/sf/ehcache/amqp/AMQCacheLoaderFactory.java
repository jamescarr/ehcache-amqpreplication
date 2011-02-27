/**
 *  Copyright 2011 James Carr
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sf.ehcache.amqp;

import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.loader.CacheLoader;
import net.sf.ehcache.loader.CacheLoaderFactory;

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
