package vn.edu.fpt.account.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.account.entity.OttHistory;

import java.util.Optional;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 25/12/2022 - 01:05
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Repository
public interface OttHistoryRepository extends MongoRepository<OttHistory, String> {

    Optional<OttHistory> findOttHistoriesByOtt(String ott);
}
