package ch.avsar.loggr.repository;

import ch.avsar.loggr.domain.WorkLog;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the WorkLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    @Query("select work_log from WorkLog work_log where work_log.creator.login = ?#{principal.username}")
    List<WorkLog> findByCreatorIsCurrentUser();

}
