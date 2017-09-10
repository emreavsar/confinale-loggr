package ch.avsar.loggr.service;

import ch.avsar.loggr.domain.Project;
import ch.avsar.loggr.domain.User;
import ch.avsar.loggr.domain.WorkLog;
import ch.avsar.loggr.service.dto.WorkLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing WorkLog.
 */
public interface WorkLogService {

    /**
     * Save a workLog.
     *
     * @param workLogDTO the entity to save
     * @return the persisted entity
     */
    WorkLogDTO save(WorkLogDTO workLogDTO);

    /**
     * Get all the workLogs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WorkLogDTO> findAll(Pageable pageable);

    /**
     * Get the "id" workLog.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WorkLogDTO findOne(Long id);

    /**
     * Delete the "id" workLog.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Returns a map of all projects (key) and all the worklogs for it (value).
     *
     * @return map of all projects and their worklogs.
     */
    Map<Project, List<WorkLog>> getStatisticPerProject();

    /**
     * Returns a map of all employees (key) and all the worklogs for it (value).
     *
     * @return map of all projects and their worklogs.
     */
    Map<User, List<WorkLog>> getStatisticPerEmployee();
}
