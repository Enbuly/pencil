package com.rain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rain.api.apple.model.User;
import com.rain.api.apple.model.vo.UserVo;
import com.rain.service.UserService;
import com.rain.util.EasyPoiUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * CurdController
 *
 * @author lazy cat
 * @since 2020-01-16
 **/
@Api(description = "增删改查导入导出demo")
@RestController
@RequestMapping(value = "/curd")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping(value = "/list")
    public List<User> list(User user) {
        return userService.select(user);
    }

    @GetMapping(value = "/page")
    public IPage<User> page(@RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
                            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return userService.selectPage(pageNumber, pageSize);
    }

    @PostMapping(value = "update")
    public int update(@RequestBody User user) {
        return userService.updateUserById(user);
    }

    @PostMapping(value = "insert")
    public int insert(@RequestBody User user) throws Exception {
        return userService.insertUser(user);
    }

    @GetMapping(value = "/selectUser")
    public List<UserVo> selectUser() {
        return userService.selectUser();
    }

    //使用谷歌浏览器导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<User> list = userService.select(new User());
        EasyPoiUtil.exportExcel(list, "用户报表", "用户信息", User.class, "用户信息.xls", response);
    }

    //使用谷歌浏览器导出
    @GetMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId("1001");
        user.setName("zzy");
        user.setPhone("15602227266");
        user.setPassword("120157229");
        user.setSalary(12000);
        user.setStatus(0);
        list.add(user);
        EasyPoiUtil.exportExcel(list, "用户报表模版", "用户信息", User.class, "用户报表模版.xls", response);
    }

    @PostMapping("/importExcel")
    public int importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<User> list = EasyPoiUtil.importExcel(file, 1, 1, User.class);
        return userService.batchInsertUser(list);
    }

}
