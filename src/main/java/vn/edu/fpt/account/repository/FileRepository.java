package vn.edu.fpt.account.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.account.entity._File;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 06/12/2022 - 11:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface FileRepository extends MongoRepository<_File, String> {
}
