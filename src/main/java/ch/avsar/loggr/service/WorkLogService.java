package ch.avsar.loggr.service;

import ch.avsar.loggr.service.dto.ProjectDTO;
import ch.avsar.loggr.service.dto.UserDTO;
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
     * Get all the getWorkLogs.
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
     * Returns statistics with projectname and all bookedHours in hours for that project.
     *
     * @return map of projectname => booked hours.
     */
    Map<String, Double> getStatisticPerProject();


    /**
     * Returns statistics with employee and all bookedHours in hours for that project.
     *
     * @return map of employeename => booked hours.
     */
    Map<String, Double> getStatisticPerEmployee();
}
