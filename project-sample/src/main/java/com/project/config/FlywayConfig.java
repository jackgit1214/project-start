package com.project.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author lilj
 * @date 2021/05/31
 **/
@Configuration
@Slf4j
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .baselineOnMigrate(true)
                .load();
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            flyway.repair();
            log.error("Flyway配置加载出错", e);
        }
    }
}