package ch.avsar.loggr.repository;

import ch.avsar.loggr.domain.Project;
import ch.avsar.loggr.domain.WorkLog;
import ch.avsar.loggr.domain.enumeration.WorkLogType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Spring Data JPA repository for the WorkLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    @Query("select work_log from WorkLog work_log where work_log.creator.login = ?#{principal.username}")
    List<WorkLog> findByCreatorIsCurrentUser();

    /**
     * Looks for all work logs of a specific type.
     *
     * @param workLogType type of worklogs
     * @return list of worklogs with given type
     */
    List<WorkLog> findAllByType(WorkLogType workLogType);
}
