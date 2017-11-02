package guru.springframework.controllers;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StaleObjectStateException.class)
    public ModelAndView handleNumberFormat(Exception exception){

        log.error("DB related concurrent modification exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("dbError");
        modelAndView.addObject("exception", "You tried to modify something at the same time as another user.\nPlease try again in a little bit!");

        return modelAndView;
    }
}
