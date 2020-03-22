package com.carson.cachedemo;


import com.carson.cachedemo.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CachedemoApplicationTests {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @PostConstruct
    public void init(){
        redisTemplate.opsForValue().set("myKey","myValue");
        //redisUtil.del("ok");
    }

    @Test
    public void set(){


        System.out.println(redisTemplate.opsForValue().get("myKey"));
        System.out.println(redisUtil.get("ok")+"-"+redisUtil.getExpire("ok"));
        if( redisUtil.setnx("ok","123")){
            System.out.println(1111);
            redisUtil.setnx("ok","321");
        }

    }


}
