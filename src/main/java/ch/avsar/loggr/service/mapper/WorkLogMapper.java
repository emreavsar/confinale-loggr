package ch.avsar.loggr.service.mapper;

import ch.avsar.loggr.domain.*;
import ch.avsar.loggr.service.dto.WorkLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WorkLog and its DTO WorkLogDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class, })
public interface WorkLogMapper extends EntityMapper <WorkLogDTO, WorkLog> {

    @Mapping(source = "project.id", target = "projectId")

    @Mapping(source = "creator.id", target = "creatorId")
    WorkLogDTO toDto(WorkLog workLog); 

    @Mapping(source = "projectId", target = "project")

    @Mapping(source = "creatorId", target = "creator")
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
