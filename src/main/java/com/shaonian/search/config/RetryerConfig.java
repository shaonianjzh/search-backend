package com.shaonian.search.config;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 少年
 */

@Configuration
public class RetryerConfig {


    @Bean
    public Retryer retryer(){
        // 定义重试器
        return RetryerBuilder.newBuilder()
                // 如果结果为空则重试
                .retryIfResult(Predicates.isNull())
                // 发生IO异常则重试
                .retryIfExceptionOfType(IOException.class)
                // 发生运行时异常则重试
                .retryIfRuntimeException()
                // 等待
                .withWaitStrategy(WaitStrategies.incrementingWait(10, TimeUnit.SECONDS, 10, TimeUnit.SECONDS))
                // 允许执行4次（首次执行 + 最多重试3次）
                .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                .build();
    }

}
