package org.lambico.datatest.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JpaTest {
    Class<?>[] entities() default {};
    Class<?>[] loadEntities() default {};
    Property[] properties() default {};
    boolean useMerge() default false;
    int flushWindowSize() default -1;
}
