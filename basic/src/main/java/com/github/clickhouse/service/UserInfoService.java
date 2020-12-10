package com.github.clickhouse.service;

import com.github.clickhouse.entity.UserInfo;

import java.util.List;

/**
 * @author liulv
 * @since 1.0.0
 * <p>
 * 说明：
 */
public interface UserInfoService {

    public void saveData(UserInfo userInfo);

    public UserInfo selectById(Integer id);

    public List<UserInfo> selectList();
}
