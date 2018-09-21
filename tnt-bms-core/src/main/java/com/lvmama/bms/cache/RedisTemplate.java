package com.lvmama.bms.cache;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Set;

public class RedisTemplate {

    private String namespace;

    private JedisPool jedisPool;

    public RedisTemplate(Config config) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(config.getParameter(ExtConfig.REDIS_MAX_TOTAL)));
        jedisPool = new JedisPool(poolConfig,
                config.getParameter(ExtConfig.REDIS_ADDRESS),
                Integer.parseInt(config.getParameter(ExtConfig.REDIS_PORT)));
        namespace = config.getParameter(ExtConfig.REDIS_NAME_SPACE);
    }

    public Jedis getSouce() {
        if (jedisPool == null) {
            throw new IllegalArgumentException("redis is not init");
        }
        return jedisPool.getResource();
    }

    public Long incr(String... keyParts) {
        String cacheKey = getCacheKey(keyParts);
        return incr(cacheKey);
    }

    public Long incr(final String key) {
        return doInvoke(new Operation<Long>() {
            @Override
            public Long doOperation(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public void set(String value, String... keyParts) {
        String cacheKey = getCacheKey(keyParts);
        set(cacheKey, value);
    }

    public void set(final String key, final String value) {
        doInvoke(new Operation<Void>() {
            @Override
            public Void doOperation(Jedis jedis) {
                jedis.set(key, value);
                return null;
            }
        });
    }


    public void setex(String value, Integer seconds, String... keyParts) {
        String cacheKey = getCacheKey(keyParts);
        setex(cacheKey, value, seconds);
    }

    public void setex(final String key, final String value, final Integer seconds) {
        doInvoke(new Operation<Object>() {
            @Override
            public Object doOperation(Jedis jedis) {
                jedis.setex(key, seconds, value);
                return null;
            }
        });
    }

    public String get(String... keyParts) {
        String cacheKey = getCacheKey(keyParts);
        return get(cacheKey);
    }

    public String get(final String key) {
        return doInvoke(new Operation<String>() {
            @Override
            public String doOperation(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public void delete(final String... keys) {
        doInvoke(new Operation<Void>() {
            @Override
            public Void doOperation(Jedis jedis) {
                jedis.del(keys);
                return null;
            }
        });
    }

    public void expire(Integer time, String... keyParts) {
        String cacheKey = getCacheKey(keyParts);
        expire(cacheKey, time);
    }

    public void expire(final String key, final Integer time) {
        doInvoke(new Operation<Void>() {
            @Override
            public Void doOperation(Jedis jedis) {
                jedis.expire(key, time);
                return null;
            }
        });
    }

    public Long incr(int expire, String... keyParts) {
        return incr(getCacheKey(keyParts), expire);
    }

    public Long incr(final String key, final int expire) {
        return doInvoke(new Operation<Long>() {
            @Override
            public Long doOperation(Jedis jedis) {
                Long count = jedis.incr(key);
                jedis.expire(key, expire);
                return count;
            }
        });
    }

    public Long llen(final String key) {
        return doInvoke(new Operation<Long>() {
            @Override
            public Long doOperation(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }

    public void rpush(String[] keyParts, String... values) {
        String cacheKey = getCacheKey(keyParts);
        rpush(cacheKey, values);
    }

    public void rpush(final String key, final String... values) {
        doInvoke(new Operation<Void>() {
            @Override
            public Void doOperation(Jedis jedis) {
                jedis.rpush(key, values);
                return null;
            }
        });
    }


    public String lpop(String... keyParts) {
        String cacheKey = getCacheKey(keyParts);
        return lpop(cacheKey);
    }

    public String lpop(final String key) {
        return doInvoke(new Operation<String>() {
            @Override
            public String doOperation(Jedis jedis) {
                return jedis.lpop(key);
            }
        });
    }

    public Set<String> keys(final String pattern) {
        return doInvoke(new Operation<Set<String>>() {
            @Override
            public Set<String> doOperation(Jedis jedis) {
                return jedis.keys(pattern);
            }
        });
    }

    public Boolean flushAll() {
        return doInvoke(new Operation<Boolean>() {
            @Override
            public Boolean doOperation(Jedis jedis) {
                return "OK".equals(jedis.flushAll());
            }
        });
    }

    public Long pttl(final String key) {
        return doInvoke(new Operation<Long>() {
            @Override
            public Long doOperation(Jedis jedis) {
                return jedis.pttl(key);
            }
        });
    }

    public interface Operation<T> {
        T doOperation(Jedis jedis);
    }

    public <T> T doInvoke(Operation<T> operation) {
        Jedis jedis = jedisPool.getResource();
        try {
            return operation.doOperation(jedis);
        } finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public interface Multi {
        List<Object> doMulti(Transaction transaction);
    }

    public List<Object> multi(Multi multi) {

        Jedis jedis = jedisPool.getResource();

        try {
            Transaction transaction = jedis.multi();
            return multi.doMulti(transaction);
        } finally {
            if(jedis!=null) {
                jedis.close();
            }
        }

    }


    public interface Batch {
        List<Object> doBatch(Pipeline pipeline);
    }

    public List<Object> batch(Batch batch) {
        Jedis jedis = jedisPool.getResource();
        try {
            return batch.doBatch(jedis.pipelined());
        } finally {
            if(jedis!=null) {
                jedis.close();
            }
        }
    }

    public String getCacheKey(String... keyParts) {

        String cacheKey = namespace;

        for(String part : keyParts) {
            cacheKey+= "_" + part;
        }

        return cacheKey;

    }

    public String[] getKeyParts(String cacheKey) {
        return cacheKey.split("_");
    }

    public <T> T evalScript(final String script, final int keyCount, final String... params) {

        return doInvoke(new Operation<T>() {
            @Override
            public T doOperation(Jedis jedis) {
                return (T) jedis.eval(script, keyCount, params);
            }
        });

    }

    public Long increx(String key, int timeout) {

        StringBuilder script = new StringBuilder();
        script.append("local current = redis.call(\"incr\",KEYS[1])\n");
        script.append("if tonumber(current) == 1 then\n");
        script.append("redis.call(\"expire\", KEYS[1], KEYS[2])\n");
        script.append("end\n");
        script.append("return current\n");

        Long result = evalScript(script.toString(), 2, key, String.valueOf(timeout));

        return result;

    }




}
