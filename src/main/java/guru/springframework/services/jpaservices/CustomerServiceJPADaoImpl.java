package guru.springframework.services.jpaservices;

import guru.springframework.domain.Customer;
import guru.springframework.services.CustomerService;
import guru.springframework.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by jt on 12/14/15.
 */
@Service
@Profile("jpadao")
public class CustomerServiceJPADaoImpl extends AbstractJpaDaoService implements CustomerService {

    private EncryptionService encryptionService;

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public List<Customer> listAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("from Customer", Customer.class).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Customer getById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Customer saveOrUpdate(Customer domainObject) {
        EntityManager em = emf.createEntityManager();

        Customer savedCustomer;
        try {
            em.getTransaction().begin();

            if (domainObject.getUser() != null && domainObject.getUser().getPassword() != null) {
                domainObject.getUser().setEncryptedPassword(
                        encryptionService.encryptString(domainObject.getUser().getPassword()));
            }

            savedCustomer = em.merge(domainObject);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return savedCustomer;
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(em.find(Customer.class, id));
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
