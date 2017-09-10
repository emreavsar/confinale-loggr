package ch.avsar.loggr.web.rest;

import com.codahale.metrics.annotation.Timed;
import ch.avsar.loggr.service.WorkLogService;
import ch.avsar.loggr.web.rest.util.HeaderUtil;
import ch.avsar.loggr.web.rest.util.PaginationUtil;
import ch.avsar.loggr.service.dto.WorkLogDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkLog.
 */
@RestController
@RequestMapping("/api")
public class WorkLogResource {

    private final Logger log = LoggerFactory.getLogger(WorkLogResource.class);

    private static final String ENTITY_NAME = "workLog";

    private final WorkLogService workLogService;

    public WorkLogResource(WorkLogService workLogService) {
        this.workLogService = workLogService;
    }

    /**
     * POST  /work-logs : Create a new workLog.
     *
     * @param workLogDTO the workLogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workLogDTO, or with status 400 (Bad Request) if the workLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-logs")
    @Timed
    public ResponseEntity<WorkLogDTO> createWorkLog(@Valid @RequestBody WorkLogDTO workLogDTO) throws URISyntaxException {
        log.debug("REST request to save WorkLog : {}", workLogDTO);
        if (workLogDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workLog cannot already have an ID")).body(null);
        }
        WorkLogDTO result = workLogService.save(workLogDTO);
        return ResponseEntity.created(new URI("/api/work-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-logs : Updates an existing workLog.
     *
     * @param workLogDTO the workLogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workLogDTO,
     * or with status 400 (Bad Request) if the workLogDTO is not valid,
     * or with status 500 (Internal Server Error) if the workLogDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-logs")
    @Timed
    public ResponseEntity<WorkLogDTO> updateWorkLog(@Valid @RequestBody WorkLogDTO workLogDTO) throws URISyntaxException {
        log.debug("REST request to update WorkLog : {}", workLogDTO);
        if (workLogDTO.getId() == null) {
            return createWorkLog(workLogDTO);
        }
        WorkLogDTO result = workLogService.save(workLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-logs : get all the workLogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workLogs in body
     */
    @GetMapping("/work-logs")
    @Timed
    public ResponseEntity<List<WorkLogDTO>> getAllWorkLogs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WorkLogs");
        Page<WorkLogDTO> page = workLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-logs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /work-logs/:id : get the "id" workLog.
     *
     * @param id the id of the workLogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workLogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/work-logs/{id}")
    @Timed
    public ResponseEntity<WorkLogDTO> getWorkLog(@PathVariable Long id) {
        log.debug("REST request to get WorkLog : {}", id);
        WorkLogDTO workLogDTO = workLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workLogDTO));
    }

    /**
     * DELETE  /work-logs/:id : delete the "id" workLog.
     *
     * @param id the id of the workLogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-logs/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkLog(@PathVariable Long id) {
        log.debug("REST request to delete WorkLog : {}", id);
        workLogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
