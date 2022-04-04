package com.su.solid.annotation;

import com.su.solid.thread_type.ThreadType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SolidView {
    int bindId();
    ThreadType threadType() default ThreadType.THREAD;
}
