package guru.springframework.services.security;

import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Aspect
@Component // register this class as a spring component (a bean).
public class LoginAspect {

    private LoginFailureEventPublisher loginFailureEventPublisher;
    private LoginSuccessEventPublisher successEventPublisher;

    @Autowired
    public void setPublisher(LoginFailureEventPublisher publisher) {
        this.loginFailureEventPublisher = publisher;
    }

    @Autowired
    public void setSuccessEventPublisher(LoginSuccessEventPublisher successEventPublisher) {
        this.successEventPublisher = successEventPublisher;
    }

    // take in any data type (via the wild card).
    @Pointcut("execution(* org.springframework.security.authentication.AuthenticationProvider.authenticate(..))")
    public void doAuthenticate(){

    }

    // capture on the way in the authentication object that is being passed in (it will be inspected).
    @Before("guru.springframework.services.security.LoginAspect.doAuthenticate() && args(authentication)")
    public void logBefore(Authentication authentication){

        System.out.println("This is before the Authenticate Method: authentication: " + authentication.isAuthenticated());
    }

    // runs after the doAuthenticate method.
    // returning: pass back an authenticated object.
    @AfterReturning(value = "guru.springframework.services.security.LoginAspect.doAuthenticate()",
            returning = "authentication")
    public void logAfterAuthenticate( Authentication authentication){
        System.out.println("This is after the Authenticate Method authentication: " + authentication.isAuthenticated());
        successEventPublisher.publishEvent(new LoginSuccessEvent(authentication));
    }

    @AfterThrowing("guru.springframework.services.security.LoginAspect.doAuthenticate() && args(authentication)")
    public void logAuthenicationException(Authentication authentication){
        String userDetails = (String) authentication.getPrincipal();
        // log the user name.
        System.out.println("Login failed for user: " + userDetails);

        // after the authentication fails this logging will occur. (afterthrowing annotation).
        // remember this is decoupled as this uses spring's application event system to display this message.
        loginFailureEventPublisher.publish(new LoginFailureEvent(authentication));
    }
}
