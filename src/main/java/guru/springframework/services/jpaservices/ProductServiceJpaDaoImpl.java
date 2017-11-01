package guru.springframework.services.jpaservices;

import guru.springframework.domain.Product;
import guru.springframework.services.ProductService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by jt on 12/9/15.
 */
@Service
@Profile("jpadao-dontuse") // this service will no longer be used.
public class ProductServiceJpaDaoImpl extends AbstractJpaDaoService implements ProductService {

    @Override
    public List<Product> listAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("from Product", Product.class).getResultList();
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Override
    public Product getById(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Product.class, id);
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Override
    public Product saveOrUpdate(Product domainObject) {
        EntityManager em = emf.createEntityManager();

        Product savedProduct;
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
            em.remove(em.find(Product.class, id));
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
