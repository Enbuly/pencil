package com.rain.dao;

import com.rain.api.apple.model.User;
import com.rain.api.apple.model.vo.UserKey;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        List<User> users = userMapper.select(new User());
        for (User user : users) {
            System.out.println(user.toString());
        }

        //分组
        System.out.println("分组");
        Map<String, List<User>> listMap = users.stream().collect(Collectors.groupingBy(User::getId));
        for (Map.Entry<String, List<User>> entry : listMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
        }

        //根据多个字段分组
        System.out.println("根据多个字段分组");
        Map<UserKey, List<User>> map = users.stream().collect(Collectors.groupingBy(o ->
                new UserKey(o.getId(), o.getName())
        ));
        for (Map.Entry<UserKey, List<User>> entry : map.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
        }
    }

    @Test
    public void testMap() {
        Map<Integer, User> map = userMapper.selectMap();
        for (Map.Entry<Integer, User> entry : map.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
        }
    }

    @Test
    public void testRedisCluster() {
        ArrayList<RedisURI> list = new ArrayList<>();
        list.add(RedisURI.create("redis://127.0.0.1:7000"));
        list.add(RedisURI.create("redis://127.0.0.1:7001"));
        list.add(RedisURI.create("redis://127.0.0.1:7002"));
        list.add(RedisURI.create("redis://127.0.0.1:7003"));
        list.add(RedisURI.create("redis://127.0.0.1:7004"));
        list.add(RedisURI.create("redis://127.0.0.1:7005"));
        RedisClusterClient client = RedisClusterClient.create(list);
        StatefulRedisClusterConnection<String, String> connect = client.connect();
        RedisAdvancedClusterCommands<String, String> commands = connect.sync();
        commands.set("test2", "zzy");
        String str = commands.get("test2");
        System.out.println(str);
        connect.close();
        client.shutdown();
    }
}