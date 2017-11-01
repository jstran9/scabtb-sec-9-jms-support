package guru.springframework.services.reposervices;

import guru.springframework.domain.Customer;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.services.CustomerService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile({"springdatajpa"})
public class CustomerServiceRepoImpl implements CustomerService {

    private CustomerRepository customerRepository;

    public CustomerServiceRepoImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }
}
