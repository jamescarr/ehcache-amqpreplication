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

import java.util.Collection;
import java.util.Map;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.loader.CacheLoader;

/**
 * Description Here.
 *
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class AMQCacheLoader implements CacheLoader {

	public AMQCacheLoader(Ehcache cache) {
	}

	public Object load(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	public Map loadAll(Collection keys) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object load(Object key, Object argument) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map loadAll(Collection keys, Object argument) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public CacheLoader clone(Ehcache cache) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void dispose() throws CacheException {
		// TODO Auto-generated method stub
		
	}

	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}
