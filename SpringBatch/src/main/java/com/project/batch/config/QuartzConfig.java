package com.project.batch.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuartzConfig implements Lifecycle {

    final DataSource dataSource;
    final PlatformTransactionManager transactionManager;
    final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        log.info("[INIT] QuartzConfig...");
    }

    @Override
    public void start() {
        log.info("[START] {}...", this.getClass().getSimpleName());
    }

    @Override
    public void stop() {
        log.info("[STOP] {}...", this.getClass().getSimpleName());
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
