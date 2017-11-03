package guru.springframework.services.jpaservices;

import guru.springframework.domain.User;
import guru.springframework.services.UserService;
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
public class UserServiceJpaDaoImpl extends AbstractJpaDaoService implements UserService {

    private EncryptionService encryptionService;

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public List<?> listAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("from User", User.class).getResultList();
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Override
    public User getById(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(User.class, id);
        } finally {
            if(em != null) {
                em.close();
            }
        }
    }

    @Override
    public User saveOrUpdate(User domainObject) {
        EntityManager em = emf.createEntityManager();

        User saveduser;
        try {
            em.getTransaction().begin();

            if(domainObject.getPassword() != null){
                domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
            }

            saveduser = em.merge(domainObject);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return saveduser;
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(em.find(User.class, id));
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public User findByUserName(String userName) {
        EntityManager em = emf.createEntityManager();

        return em.createQuery("from User where username = :userName", User.class).getSingleResult();
    }
}
