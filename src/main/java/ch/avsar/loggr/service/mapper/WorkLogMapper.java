package ch.avsar.loggr.service.mapper;

import ch.avsar.loggr.domain.*;
import ch.avsar.loggr.service.dto.WorkLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WorkLog and its DTO WorkLogDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class, })
public interface WorkLogMapper extends EntityMapper <WorkLogDTO, WorkLog> {

    @Mapping(source = "creator.id", target = "creatorId")

    @Mapping(source = "project.id", target = "projectId")
    WorkLogDTO toDto(WorkLog workLog);

    @Mapping(source = "creatorId", target = "creator")

    @Mapping(source = "projectId", target = "project")

    WorkLog toEntity(WorkLogDTO workLogDTO);
    default WorkLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkLog workLog = new WorkLog();
        workLog.setId(id);
        return workLog;
    }
}
