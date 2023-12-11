package com.rain.controller;

import com.alibaba.excel.EasyExcel;
import com.rain.api.apple.model.User;
import com.rain.dao.BananaMapper;
import com.rain.easyExcel.UserListener;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * easyExcel controller
 *
 * @author lazy cat
 * 2020-08-27
 **/
@Api(description = "演示了easy excel的导入导出(大数据的导入导出)")
@RestController
@RequestMapping(value = "/easy")
public class EasyExcelController {

    private static final Logger logger = LoggerFactory.getLogger(EasyExcelController.class);

    @Resource
    private BananaMapper bananaMapper;

    @PostMapping("upload")
    public String upload(MultipartFile file) throws IOException {
        long a = new Date().getTime();
        EasyExcel.read(file.getInputStream(), User.class, new UserListener(bananaMapper)).sheet().doRead();
        long b = new Date().getTime();
        logger.info("一共耗时:{}秒", (int) ((b - a) / 1000));
        return "success!";
    }

    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        String fileName = URLEncoder.encode("用户报表", "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), User.class).sheet("数据").doWrite(bananaMapper.select(new User()));
    }
}
