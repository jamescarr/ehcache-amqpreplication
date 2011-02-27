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

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Description Here.
 * 
 * @author James R. Carr <james.r.carr@gmail.com>
 */
public class ObjectMapper {
	public static <T> T createFrom(Class<T> class1, Properties properties) {
		T object;
		try {
			object = class1.newInstance();
			Enumeration<String> props = (Enumeration<String>) properties
					.propertyNames();
			for (Method m : object.getClass().getDeclaredMethods()) {
				if (m.getName().contains("set")) {
					String key = m.getName().replace("set", "").toLowerCase();
					if (properties.containsKey(key)) {
						Class<?> type = m.getParameterTypes()[0];
						m.invoke(object, convert(type, properties.get(key)));
					}
				}
			}
			return object;
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
	}

	private static Object convert(Class<?> type, Object object) {
		if (type.equals(int.class)) {
			return Integer.parseInt((String) object);
		}
		return object;
	}

}
