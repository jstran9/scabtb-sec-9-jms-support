package guru.springframework.services.reposervices;

import guru.springframework.domain.Order;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.OrderRepository;
import guru.springframework.services.OrderService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("springdatajpa")
public class OrderServiceRepoImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceRepoImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<?> listAll() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

    @Override
    public Order getById(Integer id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException("order with ID: " + id + " could not be found");
        }

        return orderOptional.get();
    }

    @Override
    public Order saveOrUpdate(Order domainObject) {
        return orderRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }
}
