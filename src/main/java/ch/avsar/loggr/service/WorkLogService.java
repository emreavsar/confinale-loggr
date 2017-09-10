package ch.avsar.loggr.service;

import ch.avsar.loggr.service.dto.WorkLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     *  Get all the workLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<WorkLogDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" workLog.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WorkLogDTO findOne(Long id);

    /**
     *  Delete the "id" workLog.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
