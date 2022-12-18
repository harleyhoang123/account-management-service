package vn.edu.fpt.account.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.account.entity._Role;

import java.util.List;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 20/11/2022 - 09:18
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface RoleRepository extends MongoRepository<_Role, String> {

    Optional<_Role> findByRoleName(String roleName);

    List<_Role> findAllByRoleNameIn(List<String> roleName);
}
