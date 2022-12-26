package vn.edu.fpt.account.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.account.entity.OTPHistory;

import java.util.List;
import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 26/12/2022 - 15:24
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface OTPHistoryRepository extends MongoRepository<OTPHistory, String> {

    List<OTPHistory> findByAccountIdAndEnable(String accountId, Boolean enable);
}
