package com.employeemanagement.utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheUtils {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${spring.cache.ttl}")
	private int cacheTTL;
	
	private Logger logger = LogManager.getLogger(getClass());
	
	public void putValueInCache(String redisKey, List<?> value) {
		try {
			redisTemplate.opsForValue().set(redisKey, value);
			redisTemplate.expire(redisKey, cacheTTL, TimeUnit.MINUTES);
		} catch (Exception ex) {
			logger.error("Exception while putResultInCache: " + ex.getMessage());
		}
	}
}
