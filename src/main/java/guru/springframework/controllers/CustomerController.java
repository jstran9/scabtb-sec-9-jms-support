package guru.springframework.controllers;

import guru.springframework.commands.CustomerForm;
import guru.springframework.commands.validators.CustomerFormValidator;
import guru.springframework.converters.CustomerToCustomerForm;
import guru.springframework.domain.Customer;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.services.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.Binding;
import javax.validation.Valid;

/**
 * Created by jt on 11/15/15.
 */
@RequestMapping("/customer")
@Controller
@Slf4j
public class CustomerController {

    private CustomerService customerService;
    private CustomerToCustomerForm customerToCustomerForm;
    private Validator customerFormValidator;

    public CustomerController(CustomerService customerService, CustomerToCustomerForm customerToCustomerForm,
                              @Qualifier("customerFormValidator") Validator customerFormValidator) {
        this.customerService = customerService;
        this.customerToCustomerForm = customerToCustomerForm;
        this.customerFormValidator = customerFormValidator;
    }

    @RequestMapping({"/list", "/"})
    public String listCustomers(Model model){
        model.addAttribute("customers", customerService.listAll());
        return "customer/list";
    }

    @RequestMapping("/show/{id}")
    public String showCustomer(@PathVariable Integer id, Model model){
        model.addAttribute("customer", customerService.getById(id));
        return "customer/show";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Customer customer = customerService.getById(id);

        model.addAttribute("customerForm", customerToCustomerForm.convert(customer));
        return "customer/customerform";
    }

    @RequestMapping("/new")
    public String newCustomer(Model model){
        model.addAttribute("customerForm", new CustomerForm());
        return "customer/customerform";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveOrUpdate(@Valid CustomerForm customerForm, BindingResult bindingResult){

        customerFormValidator.validate(customerForm, bindingResult);

        if(bindingResult.hasErrors()) {
            return "customer/customerForm";
        }

        Customer newCustomer = customerService.saveOrUpdateCustomerForm(customerForm);
        return "redirect:customer/show/" + newCustomer.getId();
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        customerService.delete(id);
        return "redirect:/customer/list";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public ModelAndView handleCustomerNotFound(Exception exception){
        log.error("Customer could not be found exception! " + exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("genericError");
        return modelAndView;
    }

}
