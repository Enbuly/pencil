package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.api.apple.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis controller
 * <p>
 * 笔记:
 * redis的key都是string类型的
 * 而value有string、list、hash、set、sorted set
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Api(description = "redis使用demo，redis+lua使用，redis+lua布隆过滤器")
@RestController
@RequestMapping(value = "/redis")
@Loggable(loggable = true)
public class RedisController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //paperNewCount现在卖出去的数量
    //paperMaxCount卖出去的最大数量
    private static final String GET_COUPON_CODE =
            "local values = redis.call('hmget',KEYS[1],'paperNewCount','paperMaxCount');\n" +
                    "if tonumber(values[1]) < tonumber(values[2]) then \n" +
                    "  redis.call('hincrby',KEYS[1],'paperNewCount',1);\n" +
                    "  return true;\n" +
                    "else\n " +
                    "  return false;\n" +
                    "end\n";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @ApiOperation("存储string数据结构-简单的string类型")
    @PostMapping(value = "saveString")
    public String saveString(String key, String value) {
        String keys = StringUtils.join(new String[]{"user", "string", key}, ":");
        stringRedisTemplate.opsForValue().set(keys, value);
        stringRedisTemplate.expire(keys, 5, TimeUnit.MINUTES);
        return stringRedisTemplate.opsForValue().get(keys);
    }

    @ApiOperation("存储string数据结构-根据用户id存储用户信息")
    @PostMapping(value = "saveObject1")
    public User saveObject(@RequestBody User user) {
        String userKey = StringUtils.join(new String[]{"user", "model", user.getId()}, ":");
        redisTemplate.opsForValue().set(userKey, user, 5, TimeUnit.MINUTES);
        return (User) redisTemplate.opsForValue().get(userKey);
    }

    @ApiOperation("存储hash数据结构-根据用户id存储用户信息")
    @PostMapping(value = "saveObject2")
    public User saveHash(@RequestBody User user) {
        String userKey = StringUtils.join(new String[]{"user", "token"}, ":");
        redisTemplate.opsForHash().put(userKey, user.getId(), user);
        redisTemplate.expire(userKey, 5, TimeUnit.MINUTES);
        return (User) redisTemplate.opsForHash().get(userKey, user.getId());
    }

    @ApiOperation("boundValueOps存储list-需要遍历list才会存储list->监控，否则用hash")
    @PostMapping("/boundValueOps")
    public List<User> saveList() {
        String listKey = StringUtils.join(new String[]{"user", "list"}, ":");
        List<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setName("zzy");
        user1.setPassword("120157229");
        User user2 = new User();
        user2.setName("zzx");
        user2.setPassword("13828831312");
        list.add(user1);
        list.add(user2);

        redisTemplate.boundValueOps(listKey).set(list, 5, TimeUnit.MINUTES);
        List<User> users = (List<User>) redisTemplate.boundValueOps(listKey).get();
        if (!CollectionUtils.isEmpty(users)) for (User user : users)
            System.out.println(user.toString());
        return users;
    }

    @ApiOperation("opsForList存储list-需要遍历list才会存储list->监控，否则用hash")
    @PostMapping("/opsForList")
    public List<User> saveList2() {
        String listKey = StringUtils.join(new String[]{"user", "list2"}, ":");
        User user1 = new User();
        user1.setId("100001");
        user1.setName("zzz");
        user1.setPassword("120157");
        user1.setPhone("15602227266");
        User user2 = new User();
        user2.setId("100002");
        user2.setName("xxx");
        user2.setPassword("120157");
        user2.setPhone("13828831312");

        redisTemplate.opsForList().rightPush(listKey, user1);
        redisTemplate.opsForList().rightPush(listKey, user2);
        redisTemplate.expire(listKey, 5, TimeUnit.MINUTES);
        List<User> users = (List<User>) (List) redisTemplate.opsForList().range(listKey, 0, -1);
        if (!CollectionUtils.isEmpty(users)) for (User user : users)
            System.out.println(user.toString());
        return users;
    }

    @ApiOperation("测试redis的setNx命令")
    @GetMapping(value = "setNx")
    public Boolean setNx() {
        return stringRedisTemplate.opsForValue().setIfAbsent("lock", "1", 20, TimeUnit.SECONDS);
    }

    @ApiOperation("redis+lua解决超卖问题-给商品设置数量-生产上可以用定时任务")
    @PostMapping(value = "setPaperCount")
    public void setPaperCount() {
        redisTemplate.opsForHash().put("paperCount", "paperNewCount", 0);
        redisTemplate.opsForHash().put("paperCount", "paperMaxCount", 30);
        redisTemplate.expire("paperCount", 5, TimeUnit.MINUTES);
    }

    @ApiOperation("redis+lua解决超卖问题-使用前先调用setPaperCount接口")
    @PostMapping(value = "paperCount")
    public Boolean paperCount() {
        List<String> list = new ArrayList<>();
        list.add("paperCount");
        RedisScript<Boolean> REDIS_SCRIPT = new DefaultRedisScript<>(GET_COUPON_CODE, Boolean.class);
        return redisTemplate.execute(REDIS_SCRIPT, list);
    }

    @ApiOperation("redis+lua测试布隆过滤器添加")
    @GetMapping(value = "redisIdAdd")
    public boolean redisIdAdd(String value) {
        DefaultRedisScript<Boolean> bloom = new DefaultRedisScript<>();
        bloom.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilterAdd.lua")));
        bloom.setResultType(Boolean.class);
        List<String> keyList = new ArrayList<>();
        keyList.add("isBloom");
        keyList.add(value);
        Boolean result = redisTemplate.execute(bloom, keyList);
        if (null != result) return result;
        else return false;
    }

    @ApiOperation("redis+lua测试布隆过滤器是否存在")
    @GetMapping(value = "redisIdExists")
    public boolean redisIdExists(String value) {
        DefaultRedisScript<Boolean> bloom = new DefaultRedisScript<>();
        bloom.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilterExist.lua")));
        bloom.setResultType(Boolean.class);
        List<String> keyList = new ArrayList<>();
        keyList.add("isBloom");
        keyList.add(value);
        Boolean result = redisTemplate.execute(bloom, keyList);
        if (null != result) return result;
        else return false;
    }
}
