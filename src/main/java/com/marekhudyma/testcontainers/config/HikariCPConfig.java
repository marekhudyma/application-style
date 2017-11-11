package com.marekhudyma.testcontainers.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Isolation;

import javax.sql.DataSource;

@Configuration
public class HikariCPConfig {

    @Value("${db_url}")
    private String url;

    @Value("${db_username}")
    private String username;

    @Value("${db_password}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        // for types not known by hibernate (eg. enums)
        hikariConfig.addDataSourceProperty("stringtype", "unspecified");
        hikariConfig.setTransactionIsolation("TRANSACTION_" + Isolation.READ_COMMITTED.toString());
        return new HikariDataSource(hikariConfig);
    }
}
