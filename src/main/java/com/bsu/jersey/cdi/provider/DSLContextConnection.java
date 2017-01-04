package com.bsu.jersey.cdi.provider;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * jooq连接注解
 * Created by fengchong on 2016/12/22.
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD,FIELD,PARAMETER,TYPE})
public @interface DSLContextConnection {
}
