package com.dazong.mq.annotation;

import com.dazong.mq.constant.SubscribeType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Subscribe {

	String topic() default "";

	String queue() default "";

	String name();

	SubscribeType type() default SubscribeType.ACTIVEMQ;
}
