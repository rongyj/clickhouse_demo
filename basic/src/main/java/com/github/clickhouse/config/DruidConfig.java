package com.github.clickhouse.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author liulv
 * @since 1.0.0
 * <p>
 * 说明：
 */
@Configuration
public class DruidConfig {

    @Resource
    private JdbcParamConfig jdbcParamConfig ;

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(jdbcParamConfig.getUrl());
        datasource.setPassword(jdbcParamConfig.getPassword());
        datasource.setDriverClassName(jdbcParamConfig.getDriverClassName());
        int initialSize = jdbcParamConfig.getInitialSize();
        datasource.setInitialSize(initialSize);
        int minIdle = jdbcParamConfig.getMinIdle();
        datasource.setMinIdle(minIdle);
        int maxActive = jdbcParamConfig.getMaxActive();
        datasource.setMaxActive(maxActive);
        int maxWait = jdbcParamConfig.getMaxWait();
        datasource.setMaxWait(maxWait);
        return datasource;
    }


}
