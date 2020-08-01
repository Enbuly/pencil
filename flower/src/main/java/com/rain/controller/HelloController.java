package com.rain.controller;

import com.rain.model.Book;
import com.rain.service.FlowerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * hello服务
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@RestController
public class HelloController {

    @Value("${server.port}")
    String port;

    @Resource
    private FlowerService flowerService;

    @GetMapping("/hi")
    public String home(@RequestParam String name) {
        //System.out.println("come eureka client");
        return "hi " + name + " ,i am from port:" + port;
    }

    @PostMapping("flower")
    public int flower(@RequestBody Book book) {
        return flowerService.flower(book.getId(), book.getCount());
    }
}
