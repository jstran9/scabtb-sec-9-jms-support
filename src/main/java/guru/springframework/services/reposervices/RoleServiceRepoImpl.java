package guru.springframework.services.reposervices;

import guru.springframework.domain.security.Role;
import guru.springframework.exceptions.NotFoundException;
import guru.springframework.repositories.RoleRepository;
import guru.springframework.services.RoleService;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("springdatajpa")
public class RoleServiceRepoImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceRepoImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<?> listAll() {
        List<Role> roles = new ArrayList<>();
        roleRepository.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    public Role getById(Integer id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(!roleOptional.isPresent()) {
            throw new NotFoundException("role with id " + id + " could not be found");
        }

        return roleOptional.get();
    }

    @Override
    public Role saveOrUpdate(Role domainObject) {
        return roleRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }
}
