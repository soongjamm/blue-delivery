package com.bluedelivery.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() * (1 + 10);

    @Bean(name = "defaultThreadPool")
    public Executor defaultThreadPool() {
        new TaskExecutorBuilder()
                .corePoolSize(DEFAULT_POOL_SIZE)
                .maxPoolSize(DEFAULT_POOL_SIZE)
                .threadNamePrefix("default-")
                .build();
        return AsyncConfigurer.super.getAsyncExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new LoggingAsyncExceptionHandler();
    }


    @Slf4j
    public static class LoggingAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.error("Exception Occurred during invoking {}.{} | params={}",
                    method.getDeclaringClass(), method.getName(), objects, throwable);
        }
    }

}
