package annotations.example2.annotations;

import java.lang.annotation.*;

public class Annotations {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ScanPackages {
        String[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ScheduledExecutorClass {
    }

    @Target(ElementType.METHOD)
    @Repeatable(ExecutionSchedules.class)
    public @interface ExecuteOnSchedule {
        int delaySeconds() default 0;
        int periodSeconds();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExecutionSchedules {
        ExecuteOnSchedule[] value();
    }
}
