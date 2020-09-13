package org.marvelousness.springboot.oms.bean.redis;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Redis 工具类
 *
 * @author 981247127@qq.com
 * @time 2020-08-15 16:12
 */
@Component
public final class RedisClient {
	@Autowired
	private RedisTemplate<String, ?> redisTemplate;

	@Value("${spring.redis.database:0}")
	private Integer databaseIndex = 0;

	@Value("${spring.redis.prefix:}")
	private String prefix;
	/**
	 * 最大的数据库索引
	 */
	protected final Integer maxDatabaseIndex = 15;

	private int getDatabasesIndex(Integer dbindex) {
		int index = databaseIndex == null ? 0 : databaseIndex.intValue();
		if (dbindex != null && dbindex > -1 && dbindex < maxDatabaseIndex) {
			return dbindex;
		}
		System.out.println("redis.dbindex:" + index);
		return index;
	}

	// TODO common

	/**
	 * Set the cache expire time of Specify a key
	 * 
	 * @param key
	 * @param time(s)
	 * @return
	 */
	public boolean setExpire(String key, long time) {
		try {
			if (time > 0) {
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	/**
	 * Get the cache expire time of Specify a key.
	 * <ol>
	 * <li>if this key is null, return -1.</li>
	 * <li>return 0, indicating permanent validity.</li>
	 * </ol>
	 * 
	 * @param key
	 * @return
	 */
	public long getExpire(String key) {
		if (key == null) {
			return -1;
		}
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * Determine the existence of a specified key
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Delete the cache of the specified key
	 * 
	 * @param keys
	 */
	public void del(String... keys) {
		if (keys != null && keys.length > 0) {
			del(Arrays.asList(keys));
		}
	}

	/**
	 * Delete the cache of the specified key, The number of keys that were removed. null when used in pipeline / transaction.
	 * 
	 * @param keys
	 * @return The number of keys that were removed. null when used in pipeline / transaction.
	 */
	public Long del(List<String> keys) {
		return del(keys, null);
	}

	/**
	 * Delete the cache of the specified key, The number of keys that were removed. null when used in pipeline / transaction.
	 * 
	 * @param keys
	 * @param dbindex
	 * @return The number of keys that were removed. null when used in pipeline / transaction.
	 */
	public Long del(List<String> keys, Integer dbindex) {
		if (keys == null || keys.isEmpty()) {
			return 0L;
		}
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(getDatabasesIndex(dbindex));
				RedisSerializer<String> keySerializer = getKeySerializer();
				Long total = 0L;
				for (String key : keys) {
					Set<byte[]> set = connection.keys(keySerializer.serialize(key));
					for (byte[] k : set) {
						total += connection.del(k);
					}
				}
				return total;
			}
		});
	}

	/**
	 * 获取值序列化对象
	 * 
	 * @author xiaolinzi
	 * @mail 981247127@qq.com
	 * @time 2018-12-20 11:47
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private synchronized RedisSerializer<Object> getValueSerializer() {
		return (RedisSerializer<Object>) redisTemplate.getValueSerializer();
	}

	/**
	 * 获取值序列化对象
	 * 
	 * @author xiaolinzi
	 * @mail 981247127@qq.com
	 * @time 2018-12-20 11:47
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private synchronized RedisSerializer<String> getKeySerializer() {
		return (RedisSerializer<String>) redisTemplate.getKeySerializer();
	}

	// TODO Simple Object Data

	/**
	 * Get cached data
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return get(key, Object.class, -1);
	}

	/**
	 * Get cached data
	 * 
	 * @param key
	 * @return
	 */
	public <T> T get(String key, Class<T> classz) {
		return get(key, classz, -1);
	}

	/**
	 * Get cached data
	 * 
	 * @param key
	 * @return
	 */
	public <T> T get(String key, Class<T> clazz, final int dbindex) {
		T object = redisTemplate.execute(new RedisCallback<T>() {
			@Override
			@SuppressWarnings("unchecked")
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(getDatabasesIndex(dbindex));
				RedisSerializer<Object> valueSerializer = getValueSerializer();
				RedisSerializer<String> keySerializer = getKeySerializer();
				if (valueSerializer != null && keySerializer != null) {
					try {
						return (T) valueSerializer.deserialize(connection.get(keySerializer.serialize(key)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		return object;
	}

	/**
	 * Set cached data and returns whether the operation is successful. <br />
	 * If you need to set the expire time, use <b>set(String, Object, long)</b>
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, Object value) {
		return set(key, value, -1, 0);
	}

	/**
	 * Set cached data and expire time. <br />
	 * and returns whether the operation is successful. <br />
	 * If time is less than or equal to 0, indicating permanent validity
	 * 
	 * @param key
	 * @param value
	 * @param time(s)
	 * @return
	 */
	public boolean set(String key, Object value, long seconds) {
		return set(key, value, -1, seconds);
	}

	/**
	 * Set cached data and expire time. <br />
	 * and returns whether the operation is successful. <br />
	 * If time is less than or equal to 0, indicating permanent validity
	 * 
	 * @param key
	 * @param value
	 * @param time(s)
	 * @return
	 */
	// public boolean set(String key, Object value, int dbindex) { return set(key, value, dbindex, 0);}

	/**
	 * Set cached data and expire time. <br />
	 * and returns whether the operation is successful. <br />
	 * If time is less than or equal to 0, indicating permanent validity
	 * 
	 * @param key
	 * @param value
	 * @param time(s)
	 * @return
	 */
	public boolean set(String key, Object value, int dbindex, long seconds) {
		try {
			redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					connection.select(getDatabasesIndex(dbindex));
					RedisSerializer<Object> valueSerializer = getValueSerializer();
					RedisSerializer<String> keySerializer = getKeySerializer();
					if (seconds > 0) {
						connection.setEx(keySerializer.serialize(key), seconds, valueSerializer.serialize(value));
					} else {
						connection.set(keySerializer.serialize(key), valueSerializer.serialize(value));
					}
					return true;
				}
			}, true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}