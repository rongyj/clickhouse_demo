package com.github.clickhouse.entity;

import lombok.Data;

/**
 * @author liulv
 * @since 1.0.0
 * <p>
 * 说明：
 */
@Data
public class UserInfo {
    private Integer id ;
    private String userName ;
    private String passWord ;
    private String phone ;
    private String email ;
    private String createDay ;
}
