package ch.avsar.loggr.service.mapper;

import ch.avsar.loggr.domain.*;
import ch.avsar.loggr.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper extends EntityMapper <ProjectDTO, Project> {

    @Mapping(target = "bookedHours", ignore = true)
    Project toEntity(ProjectDTO projectDTO);
    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
