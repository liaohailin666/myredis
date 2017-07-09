package com.mytaotao.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService{
	
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	
	@Autowired
	private ShardedJedisPool shardedJedisPool;

//	public void set(String key,String value){
//        ShardedJedis shardedJedis = null;
//        try {
//            shardedJedis = shardedJedisPool.getResource();
//            shardedJedis.set(key, value);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (null != shardedJedis) {
//                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
//                shardedJedis.close();
//            }
//        }
//	}
//	public void get(String key){
//        ShardedJedis shardedJedis = null;
//        try {
//            shardedJedis = shardedJedisPool.getResource();
//            shardedJedis.get(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (null != shardedJedis) {
//                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
//                shardedJedis.close();
//            }
//        }
//	}
	
	//优化实现,get/set
	private <T> T excuce(Function<T, ShardedJedis> fun){
		ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();
            return fun.callback(shardedJedis);
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
	} 
	
	public String set(final String key,final String value){
		return this.excuce(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return e.set(key, value);
			}
		});
	}
	
	public String get(final String key){
		return this.excuce(new Function<String, ShardedJedis>() {
			@Override
			public String callback(ShardedJedis e) {
				return e.get(key);
			}
		});
	}
	
	public Long del(final String key){
		return this.excuce(new Function<Long, ShardedJedis>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.del(key);
			}
		});
	}

	public Long incr(final String key){
		return this.excuce(new Function<Long, ShardedJedis>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.incr(key);
			}
		});
	}
	
	public Long append(final String key,final String value){
		return this.excuce(new Function<Long, ShardedJedis>() {
			@Override
			public Long callback(ShardedJedis e) {
				return e.append(key, value);
			}
		});
	}
}
