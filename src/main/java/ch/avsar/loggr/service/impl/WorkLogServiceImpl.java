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
import ch.avsar.loggr.service.mapper.ProjectMapper;
import ch.avsar.loggr.service.mapper.WorkLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
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

    private final ProjectMapper projectMapper;

    private final UserService userService;

    public WorkLogServiceImpl(WorkLogRepository workLogRepository, WorkLogMapper workLogMapper, ProjectMapper projectMapper, UserService userService) {
        this.workLogRepository = workLogRepository;
        this.workLogMapper = workLogMapper;
        this.projectMapper = projectMapper;
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
     * Get all the getWorkLogs.
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
    public Map<String, Double> getStatisticPerProject() {
        log.debug("Request statistic per project.");
        Map<String, Double> statistics = new HashMap<>();

        // unfortunately it is not possible to calculate on times with jpa queries in repository, so first get all bookedHours and then map them with calculating the difference
        List<WorkLog> allWorkLogs = workLogRepository.findAll();

        Map<Project, List<WorkLog>> projectWorkLogs = allWorkLogs
            .stream()
            .collect(Collectors.groupingBy(WorkLog::getProject));

        projectWorkLogs
            .forEach((project, workLogs) -> {
                Double bookedHoursInProject = workLogs
                    .stream()
                    .mapToDouble(workLog -> betweenWithFraction(workLog.getStartDate(), workLog.getEndDate()))
                    .sum();
                statistics.put(project.getName(), bookedHoursInProject);
            });

        return statistics;
    }

    private static double betweenWithFraction(Temporal startInclusive, Temporal endExclusive) {
        Duration duration = Duration.between(startInclusive, endExclusive);
        long conversion = Duration.of(1, ChronoUnit.HOURS).toNanos();
        return (double) duration.toNanos() / conversion;
    }

    @Override
    public Map<String, Double> getStatisticPerEmployee() {
        log.debug("Request statistic per employee.");
        Map<String, Double> statistics = new HashMap<>();

        // unfortunately it is not possible to calculate on times with jpa queries in repository, so first get all bookedHours and then map them with calculating the difference
        List<WorkLog> allWorkLogs = workLogRepository.findAll();

        Map<User, List<WorkLog>> employeeWorkLogs = allWorkLogs
            .stream()
            .collect(Collectors.groupingBy(WorkLog::getCreator));

        employeeWorkLogs
            .forEach((user, workLogs) -> {
                Double bookedHoursOfEmployee = workLogs
                    .stream()
                    .mapToDouble(workLog -> betweenWithFraction(workLog.getStartDate(), workLog.getEndDate()))
                    .sum();
                statistics.put(user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")", bookedHoursOfEmployee);
            });

        return statistics;
    }

    private void checkPermissions(Long creatorId) {
        User currentLoggedInUser = userService.getUserWithAuthorities();
        if (!currentLoggedInUser.getId().equals(creatorId)) {
            boolean isManager = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MANAGER);
            boolean isAdmin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
            if (!isManager && !isAdmin) {
                throw new AccessDeniedException("Saving bookedHours of others is only permitted to users with manager or admin role.");
            }
        }
    }
}
