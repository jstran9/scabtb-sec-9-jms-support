package guru.springframework.services.jpaservices;

import guru.springframework.domain.Order;
import guru.springframework.services.OrderService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by jt on 12/16/15.
 */
@Service
@Profile("jpadao")
public class OrderServiceJpaDaoImpl extends AbstractJpaDaoService implements OrderService {

    @Override
    public List<Order> listAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("from Order", Order.class).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Order getById(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Order.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Order saveOrUpdate(Order domainObject) {
        EntityManager em = emf.createEntityManager();

        Order savedProduct;
        try {
            em.getTransaction().begin();
            savedProduct = em.merge(domainObject);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return savedProduct;
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(em.find(Order.class, id));
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
