package ch.avsar.loggr.service.impl;

import ch.avsar.loggr.domain.Project;
import ch.avsar.loggr.domain.User;
import ch.avsar.loggr.security.AuthoritiesConstants;
import ch.avsar.loggr.security.SecurityUtils;
import ch.avsar.loggr.service.UserService;
import ch.avsar.loggr.service.WorkLogService;
import ch.avsar.loggr.domain.WorkLog;
import ch.avsar.loggr.repository.WorkLogRepository;
import ch.avsar.loggr.service.dto.WorkLogDTO;
import ch.avsar.loggr.service.mapper.WorkLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing WorkLog.
 */
@Service
@Transactional
public class WorkLogServiceImpl implements WorkLogService {

    private final Logger log = LoggerFactory.getLogger(WorkLogServiceImpl.class);

    private final WorkLogRepository workLogRepository;

    private final WorkLogMapper workLogMapper;

    private final UserService userService;

    public WorkLogServiceImpl(WorkLogRepository workLogRepository, WorkLogMapper workLogMapper, UserService userService) {
        this.workLogRepository = workLogRepository;
        this.workLogMapper = workLogMapper;
        this.userService = userService;
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

        if (workLogDTO.getCreatorId() == null) {
            workLogDTO.setCreatorId(userService.getUserWithAuthorities().getId());
        }
        checkPermissions(workLogDTO.getCreatorId());

        workLog = workLogRepository.save(workLog);
        return workLogMapper.toDto(workLog);
    }

    /**
     * Get all the workLogs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WorkLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkLogs");
        return workLogRepository.findAll(pageable)
            .map(workLogMapper::toDto);
    }

    /**
     * Get one workLog by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WorkLogDTO findOne(Long id) {
        log.debug("Request to get WorkLog : {}", id);
        WorkLog workLog = workLogRepository.findOne(id);
        return workLogMapper.toDto(workLog);
    }

    /**
     * Delete the  workLog by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkLog : {}", id);
        checkPermissions(id);
        workLogRepository.delete(id);
    }

    @Override
    public Map<Project, List<WorkLog>> getStatisticPerProject() {
        log.debug("Request statistic per project");

        // TODO: 10.09.17 this can be done via repository group by methods, but since this is a prototype this is ok (performance)
        List<WorkLog> allWorkLogs = workLogRepository.findAll();
        return allWorkLogs.stream().collect(Collectors.groupingBy(WorkLog::getProject));
    }

    private void checkPermissions(Long creatorId) {
        User currentLoggedInUser = userService.getUserWithAuthorities();
        if (!currentLoggedInUser.getId().equals(creatorId)) {
            boolean isManager = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MANAGER);
            boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
            if (!isManager && !isAdmin) {
                throw new AccessDeniedException("Saving worklogs of others is only permitted to users with manager or admin role.");
            }
        }
    }
}
