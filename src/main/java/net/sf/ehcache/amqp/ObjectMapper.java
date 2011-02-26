package net.sf.ehcache.amqp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Properties;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;
import net.sf.ehcache.util.PropertyUtil;

import com.rabbitmq.client.ConnectionFactory;

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
