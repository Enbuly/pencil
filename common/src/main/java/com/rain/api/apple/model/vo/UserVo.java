package com.rain.api.apple.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * user 返回包
 *
 * @author zhangzhenyan
 * 2020-07-03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVo {

    private String status;

    private String namesSalary;

    private String phonePassword;
}
