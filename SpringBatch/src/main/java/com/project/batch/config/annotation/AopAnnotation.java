package com.project.batch.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface AopAnnotation {
    String  name()    default ""   ; 	    // default Lock name call class.method name { MethodUtil.getClassAndMethodName() }
    String  lockedBy()  default ""   ;      // lock 요청한 이름.
    long    waiting()  default 0 ; 		// waiting  time for lock [MILLESECONDS]
    long    deadLine() default 0 ; 		// deadLine time for lock [MILLESECONDS], default 1 minute
    boolean alone()   default false;        // 단독으로 실행되는 여부 ( 공동 갱신 주기(update) 이외에 별도 관
}
