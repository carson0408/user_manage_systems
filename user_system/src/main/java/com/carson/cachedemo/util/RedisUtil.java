package com.carson.cachedemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * ClassName RedisUtil
 *
 * @author zhanghangfeng5
 * @description
 * @Version V1.0
 * @createTime
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public static final String LOCK_PREFIX = "redis_lock";
    //加锁失效时间，毫秒
    public static final int LOCK_EXPIRE = 300; // ms
    public boolean lock(String key){
        String lock = LOCK_PREFIX + key;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));
                    // 如果锁已经过期
                    if (expireTime < System.currentTimeMillis()) {
                        // 重新加锁，防止死锁
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * 指定缓存失效时间
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key,long time){
        try{
            if(time>0){
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据key获取过期时间
     * @param key
     * @return  时间(s)，返回0表示永久有效
     */
    public long getExpire(String key){
        return redisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 删除缓存，key可以传一个值或多个
     * @param key
     */
    public void del(String... key){
        if(key!=null&&key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    //针对string(字符串)类型
    //示例:set a "hello"
    //get a
    public boolean set(String key,Object value){
        try{
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }


    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 分布式锁
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key,Object value){
        try{
            return redisTemplate.opsForValue().setIfAbsent(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }




    //针对hash类型
    //Redis Hash是一个键值(key=>value)对集合，是一个string类型的field和value的映射表
    //hash特别适合用于存储对象
    //示例: hmset user name "张三" age "15"
    //hmget user name
    //可以把上述语句user当作对象名，key-value当作属性名与值
    //参考jdk中的map，user相当于map对象名，

    /**
     * key表示hash对象名，item指属性名，求属性值
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key,String item){
        return redisTemplate.opsForHash().get(key,item);
    }


    /**
     * hashset并设置时间，设置多个键值对
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<String,Object> map, long time){
        try{
            redisTemplate.opsForHash().putAll(key,map);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 单键值对hash对存储，并设置有效时间
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    public boolean hset(String key,String item,Object value,long time){
        try{
            redisTemplate.opsForHash().put(key,item,value);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除hash表中的item对应的键值对，可以一个或多个
     * @param key
     * @param item
     */
    public void hdel(String key,Object...item){
        redisTemplate.opsForHash().delete(key,item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key,String item){
        return redisTemplate.opsForHash().hasKey(key,item);
    }






    /**
     * 单个键值对缓存
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hmset(String key,String item,Object value){
        try{
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 获取hashkey的所有键值对
     * @param key
     * @return
     */
    public Map<Object,Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }





    //列表(List)-->双向链表
    //列表就是一个简单的字符串列表，每次往列表中添加元素。
    //lpush mylist a
    //lpush mylist b        ---->roush mylist c
    //lrange mylist 0 1
    //参考jdk中的list

    /**
     * 获取list中的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lget(String key, long start, long end){
        try{
            return redisTemplate.opsForList().range(key,start,end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取key对应的list的长度
     * @param key
     * @return
     */
    public long lgetsize(String key){
        try{
            return redisTemplate.opsForList().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /***
     * 从右侧放入缓存进list
     * @param key
     * @param value
     * @return
     */
    public boolean lrset(String key,Object value){
        try{
            redisTemplate.opsForList().rightPush(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从左边将元素放入list
     * @param key
     * @param value
     * @return
     */
    public boolean llset(String key,Object value){
        try{
            redisTemplate.opsForList().leftPush(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list从右侧放入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean lrset(String key,List<Object> value){
        try{
            redisTemplate.opsForList().rightPushAll(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list从右边放入缓存并设置有效时间
     * @param key
     * @param time
     * @param value
     * @return
     */
    public boolean lrset(String key,long time,List<Object> value){
        try{
            redisTemplate.opsForList().rightPushAll(key,value);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从左侧放入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean llset(String key,List<Object> value){
        try{
            redisTemplate.opsForList().leftPushAll(key,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从左边放入设置有效时间
     * @param key
     * @param time
     * @param value
     * @return
     */
    public boolean llset(String key,long time,List<Object> value){
        try{
            redisTemplate.opsForList().leftPushAll(key,value);
            if(time>0){
                expire(key,time);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lupdatevalue(String key,long index,Object value){
        try{
            redisTemplate.opsForList().set(key,index,value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除count个value
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lremove(String key,long count,Object value){
        try{
            return redisTemplate.opsForList().remove(key,count,value);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }



    //set无序集合
    //sadd key member
    //sadd myset redis
    //sadd myset ok
    //smembers myset
    //参考jdk集合中的set

    /**
     * 根据key获取set中的所有值，key为无序表名
     * @param key
     * @return
     */
    public Set<Object> sget(String key){
        try{
            return redisTemplate.opsForSet().members(key);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据value从一个set中查询，是否存在
     * @param key
     * @param value
     * @return
     */
    public boolean sHasKey(String key,Object value){
        try{
            return redisTemplate.opsForSet().isMember(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存，返回成功放入个数
     * @param key
     * @param values
     * @return
     */
    public long sadd(String key,Object...values){
        try{
            return redisTemplate.opsForSet().add(key,values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 移除值为value的，可以是多个
     * @param key
     * @param values
     * @return
     */
    public long sremove(String key,Object...values) {
        try{
            return redisTemplate.opsForSet().remove(key, values);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 给名为key的set增加元素并设置过期时间
     * @param key
     * @param time
     * @param values
     * @return
     */
    public long saddAndExpire(String key,long time,Object...values){
        try{
            long count = redisTemplate.opsForSet().add(key,values);
            if(time>0){
                expire(key,time);
            }
            return count;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取key对应的无序列表的长度
     * @param key
     * @return
     */
    public long sgetsize(String key){
        try{
            return redisTemplate.opsForSet().size(key);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    //zset有序集合
    //zset每个元素都会关联一个double类型的分数。redis通过分数来为集合中的成员从小到大的排序
    //zadd key score member
    //zadd myzset 0 a
    //zadd myzset 1 b

    /**
     * 给key的有序列表添加元素value，并根据元素的score排序
     * @param key
     * @param value
     * @param score
     * @return
     */
    public boolean zsetadd(String key,Object value,double score){
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 增加元素并设置有效时间
     * @param key
     * @param value
     * @param time
     * @param score
     * @return
     */
    public boolean zsetadd(String key,Object value,long time,double score){
        try{
            boolean add = redisTemplate.opsForZSet().add(key,value,score);
            if(time>0){
                expire(key,time);
            }
            return add;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取一定区间内的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zsetrange(String key,long start,long end){
        try{
            return redisTemplate.opsForZSet().range(key,start,end);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除指定元素
     * @param key
     * @param value
     * @return
     */
    public long zsetremove(String key,Object...value){
        try{
            return redisTemplate.opsForZSet().remove(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取指定元素的排位
     * @param key
     * @param value
     * @return
     */
    public long zsetrank(String key,Object value){
        try{
            return redisTemplate.opsForZSet().rank(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

}

