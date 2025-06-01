package org.yaroslaavl.userservice.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

    void setToken(String key, String value, long ttl, TimeUnit timeUnit);

    void deleteToken(String key);

    String hasToken(String key);
}
