package org.lambico.datatest.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestData {
    String resource() default "";
    TestDataType type() default TestDataType.JSON;
}
