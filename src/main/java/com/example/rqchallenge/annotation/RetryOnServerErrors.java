package com.example.rqchallenge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Retryable(
    exceptionExpression =
        "#root instanceOf T(org.springframework.web.client.HttpServerErrorException)",
    backoff = @Backoff(delay = 2000L))
public @interface RetryOnServerErrors {}
