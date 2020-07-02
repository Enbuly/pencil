package com.rain.api.apple.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * user表model
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "User", description = "用户信息")
public class User implements Serializable {

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

    /**
     * 用户工资
     **/
    @Excel(name = "用户工资")
    private double salary;

    /**
     * 用户状态
     **/
    @Excel(name = "用户状态", replace = {"正常_0", "删除_1"})
    private int status;

    /**
     * 用户电话
     **/
    @Excel(name = "用户电话")
    private String phone;

    /**
     * 用户密码
     **/
    @Excel(name = "用户密码")
    private String password;
}
