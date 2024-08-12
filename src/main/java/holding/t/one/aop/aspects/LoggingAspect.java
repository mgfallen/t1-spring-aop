package holding.t.one.aop.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);
    @Before("execution(* holding.t.one.aop.service.UserService.*(..))")
    public void logBeforeUserServiceMethods() {
        logger.info("A method from UserService is called.");
    }

    @Before("execution(* holding.t.one.aop.service.OrderService.*(..))")
    public void logBeforeOrderServiceMethods() {
        logger.info("A method from OrderService is called.");
    }

}
