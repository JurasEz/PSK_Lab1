package lt.vu.interceptors;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Marks a bean or method for logging via LoggingInterceptor.
 */
@InterceptorBinding
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {}
