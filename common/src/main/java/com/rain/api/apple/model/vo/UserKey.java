package com.rain.api.apple.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 根据多个字段分组
 *
 * @author lazycat
 * 2020-07-22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserKey {

    /**
     * 用户id
     **/
    @Excel(name = "id")
    private String id;

    /**
     * 用户名字
     **/
    @Excel(name = "姓名")
    private String name;
}
