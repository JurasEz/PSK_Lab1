package lt.vu.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Logs entry/exit of any @Loggable bean or method.
 */
@Loggable
@Interceptor
public class LoggingInterceptor {

    @AroundInvoke
    public Object wrap(InvocationContext ctx) throws Exception {
        System.out.println(" [LOG] Entering " + ctx.getMethod());
        try {
            return ctx.proceed();
        } finally {
            System.out.println(" [LOG] Exiting  " + ctx.getMethod());
        }
    }
}
