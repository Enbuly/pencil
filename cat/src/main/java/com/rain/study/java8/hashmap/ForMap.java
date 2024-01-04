package com.rain.study.java8.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * 遍历hashmap
 *
 * @author zhangzhenyan
 * 2024-01-04
 **/
public class ForMap {
    public static void main(String[] args) {

        // HashMap 是一个无序的数据结构。它存储键值对，但是不保持任何顺序。
        // 当你遍历或迭代一个 HashMap 时，键值对的返回顺序并不保证与它们
        // 被插入的顺序相同，也不保证随着时间的推移保持一致。
        Map<String, String> map = new HashMap<>();
        map.put("2", "kafka");
        map.put("1", "Java");
        map.put("4", "MyBatis");
        map.put("5", "redis");
        map.put("3", "Spring");

        map.forEach((key, value) -> {
            System.out.print("key is " + key + ",");
            System.out.print("value is " + value);
            System.out.println();
        });
    }
}
