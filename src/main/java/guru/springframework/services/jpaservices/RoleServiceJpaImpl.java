package guru.springframework.services.jpaservices;

import guru.springframework.domain.security.Role;
import guru.springframework.services.RoleService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by jt on 12/18/15.
 */
@Service
@Profile("jpadao")
public class RoleServiceJpaImpl extends AbstractJpaDaoService implements RoleService {

    @Override
    public List<?> listAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery("from Role", Role.class).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Role getById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Role saveOrUpdate(Role domainObject) {
        EntityManager em = emf.createEntityManager();

        Role saveRole;
        try {
            em.getTransaction().begin();

            saveRole = em.merge(domainObject);
            em.getTransaction().commit();
        } finally {
            if(em != null) {
                em.close();
            }
        }

        return saveRole;
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.remove(em.find(Role.class, id));
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
