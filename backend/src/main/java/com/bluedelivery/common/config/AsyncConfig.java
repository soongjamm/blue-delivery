package com.bluedelivery.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@EnableAsync
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class AsyncConfig {

    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors() * (1 + 10);

    @Bean(name = "defaultExecutor")
    public Executor defaultExecutor() {
        return new TaskExecutorBuilder()
                .corePoolSize(DEFAULT_POOL_SIZE)
                .maxPoolSize(DEFAULT_POOL_SIZE)
                .threadNamePrefix("defaultExecutor-")
                .build();
    }

    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurer() {
            @Override
            public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
                return new LoggingAsyncExceptionHandler();
            }
        };
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
