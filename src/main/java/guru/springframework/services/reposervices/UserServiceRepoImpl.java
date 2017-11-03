package guru.springframework.services.reposervices;

import guru.springframework.domain.User;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.UserRepository;
import guru.springframework.services.UserService;
import guru.springframework.services.security.EncryptionService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("springdatajpa")
public class UserServiceRepoImpl implements UserService {

    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private EncryptionService encryptionService;

    public UserServiceRepoImpl(UserRepository userRepository, CustomerRepository customerRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    public List<?> listAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User getById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new NotFoundException("User with id " + id + " could not be found.");
        }

        return userOptional.get();
    }

    @Override
    public User saveOrUpdate(User domainObject) {
        if(domainObject.getPassword() != null){
            domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
        }
        return userRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {

        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()) {
            throw new NotFoundException("User with id " + id + " could not be found and will not be deleted!");
        }
        customerRepository.delete(userOptional.get().getCustomer());
        userRepository.deleteById(id);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }
}
