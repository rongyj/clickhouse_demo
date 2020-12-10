package com.github.clickhouse.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liulv
 * @since 1.0.0
 * <p>
 * 说明： 特别注意
 */
@Component
@ConfigurationProperties(prefix = "clickhouse")
@Data
public class JdbcParamConfig {
    private String driverClassName;
    private String url;
    private String password;
    private Integer initialSize;
    private Integer maxActive;
    private Integer minIdle;
    private Integer maxWait;
}