package com.lvmama;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RedisTest {
    public static void main(String[] args){
        test2();
    }

    public static void test2() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        List<JedisShardInfo> infos = new ArrayList<>();
        JedisShardInfo jedisShardInfo = new JedisShardInfo("10.200.6.197",6377);
        infos.add(jedisShardInfo);
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, infos);

        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        System.out.println(shardedJedis.get("testa"));

        shardedJedis.incrBy("testa", 0);
        System.out.println(shardedJedis.get("testa"));
    }

    public static void test1() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        JedisPool jedisPool = new JedisPool(poolConfig,
                "10.200.6.197",
                6377);

        System.out.println(jedisPool.getResource().get("testa"));

        jedisPool.getResource().incrBy("testa", 0);
        System.out.println(jedisPool.getResource().get("testa"));
    }

    private static RedisTemplate redisTemplate;

    @BeforeClass
    public static void setUp() {

        Config config = new Config();
        config.setParameter("redis.address", "127.0.0.1");
        config.setParameter("redis.port", "6379");
        config.setParameter("redis.namespace", "tnt_bms");
        config.setParameter("redis.maxTotal", "10");

        redisTemplate = new RedisTemplate(config);

    }

    @Test
    public void testMulti() {

        redisTemplate.multi(new RedisTemplate.Multi() {
            @Override
            public List<Object> doMulti(Transaction transaction) {
                transaction.incr("Temp1");
                transaction.incr("Temp2");
                List<Object> results = transaction.exec();
                transaction.incr("Temp3");
                for (Object result : results) {
                    System.out.println(String.valueOf(result));
                }
                return null;
            }
        });

    }

    @Test
    public void testLua() {

        StringBuilder script = new StringBuilder();
        script.append("local current = redis.call(\"incr\",KEYS[1])\n");
        script.append("if tonumber(current) == 1 then\n");
        script.append("redis.call(\"expire\",KEYS[1],100)\n");
        script.append("end\n");
        script.append("return current\n");

        Long result = redisTemplate.evalScript(script.toString(), 1, "rate10");
        System.out.println(result);


    }

}
