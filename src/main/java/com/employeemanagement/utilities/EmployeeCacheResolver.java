package com.employeemanagement.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;
import com.google.common.cache.CacheBuilder;

@Component
@Qualifier("employeeCacheResolver")
public class EmployeeCacheResolver implements CacheResolver {

	@Autowired
	CacheManager cacheManager;

	@Value("${spring.cache.ttl}")
	private int cacheTTL;

	@Value("${spring.cache.prefix}")
	private String employeeCachePrefix;

	@Override
	public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
		Collection<Cache> caches = new ArrayList<>();
		Cache cache;
		if ((cache = cacheManager.getCache(employeeCachePrefix)) != null) {
			caches.add(cache);
		} else {
			cache = new ConcurrentMapCache(employeeCachePrefix,
					CacheBuilder.newBuilder().expireAfterWrite(cacheTTL, TimeUnit.MINUTES).build().asMap(), false);
			caches.add(cache);
		}
		return caches;
	}

}