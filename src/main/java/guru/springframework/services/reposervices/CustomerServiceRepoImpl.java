package guru.springframework.services.reposervices;

import guru.springframework.commands.CustomerForm;
import guru.springframework.converters.CustomerFormToCustomer;
import guru.springframework.domain.Customer;
import guru.springframework.exceptions.DBFailureException;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.services.CustomerService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile({"springdatajpa"})
public class CustomerServiceRepoImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CustomerFormToCustomer customerFormToCustomer;

    public CustomerServiceRepoImpl(CustomerRepository customerRepository, CustomerFormToCustomer customerFormToCustomer) {
        this.customerRepository = customerRepository;
        this.customerFormToCustomer = customerFormToCustomer;
    }

    @Override
    public List<?> listAll() {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        return customers;
    }

    @Override
    public Customer getById(Integer id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            throw new NotFoundException("could not find customer with id: " + id);
        }
        return customerOptional.get();
    }

    @Override
    public Customer saveOrUpdate(Customer domainObject) {
        return customerRepository.save(domainObject);
    }

    @Override
    public Customer saveOrUpdateCustomerForm(CustomerForm customerForm) {
        Customer newCustomer = customerFormToCustomer.convert(customerForm);

        if (newCustomer.getUser().getId() != null) {
            Customer existingCustomer = getById(newCustomer.getId());

            newCustomer.getUser().setEnabled(existingCustomer.getUser().getEnabled());
        }

        return saveOrUpdate(newCustomer);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (!optionalCustomer.isPresent()) {
            throw new DBFailureException("unable to delete customer with " + id);
        }
        customerRepository.deleteById(id);
    }
}
