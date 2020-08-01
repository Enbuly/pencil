package com.rain.controller;

import com.rain.api.flower.Book;
import com.rain.service.GiraffeService;
import com.rain.service.ServiceHi;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * HiController
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@RestController
public class HiController {

    @Resource
    private ServiceHi serviceHi;

    @Resource
    private GiraffeService giraffeService;

    @GetMapping(value = "/hi")
    public String sayHi(@RequestParam String name) {
        return serviceHi.sayHiFromClientOne(name);
    }

    @PostMapping("giraffe")
    public int giraffe(@RequestBody Book book) {
        return giraffeService.giraffe(book.getId(), book.getCount());
    }

    @PostMapping("distributedAffair")
    public void distributedAffair() {
        giraffeService.distributedAffair();
    }
}
