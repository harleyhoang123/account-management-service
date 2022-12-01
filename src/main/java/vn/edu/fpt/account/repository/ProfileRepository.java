package vn.edu.fpt.account.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.account.entity.Profile;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 01/12/2022 - 18:44
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface ProfileRepository extends MongoRepository<Profile, String> {
}
