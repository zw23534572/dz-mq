package com.dazong.mq.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Subscribe {

	String topic();

	String name();

	String type() default "activemq";
}
