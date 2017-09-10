package ch.avsar.loggr.service.impl;

import ch.avsar.loggr.service.WorkLogService;
import ch.avsar.loggr.domain.WorkLog;
import ch.avsar.loggr.repository.WorkLogRepository;
import ch.avsar.loggr.service.dto.WorkLogDTO;
import ch.avsar.loggr.service.mapper.WorkLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WorkLog.
 */
@Service
@Transactional
public class WorkLogServiceImpl implements WorkLogService{

    private final Logger log = LoggerFactory.getLogger(WorkLogServiceImpl.class);

    private final WorkLogRepository workLogRepository;

    private final WorkLogMapper workLogMapper;
    public WorkLogServiceImpl(WorkLogRepository workLogRepository, WorkLogMapper workLogMapper) {
        this.workLogRepository = workLogRepository;
        this.workLogMapper = workLogMapper;
    }

    /**
     * Save a workLog.
     *
     * @param workLogDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WorkLogDTO save(WorkLogDTO workLogDTO) {
        log.debug("Request to save WorkLog : {}", workLogDTO);
        WorkLog workLog = workLogMapper.toEntity(workLogDTO);
        workLog = workLogRepository.save(workLog);
        return workLogMapper.toDto(workLog);
    }

    /**
     *  Get all the workLogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkLogs");
        return workLogRepository.findAll(pageable)
            .map(workLogMapper::toDto);
    }

    /**
     *  Get one workLog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WorkLogDTO findOne(Long id) {
        log.debug("Request to get WorkLog : {}", id);
        WorkLog workLog = workLogRepository.findOne(id);
        return workLogMapper.toDto(workLog);
    }

    /**
     *  Delete the  workLog by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkLog : {}", id);
        workLogRepository.delete(id);
    }
}
