package ch.avsar.loggr.web.rest;

import ch.avsar.loggr.LoggrApp;

import ch.avsar.loggr.domain.WorkLog;
import ch.avsar.loggr.repository.WorkLogRepository;
import ch.avsar.loggr.service.WorkLogService;
import ch.avsar.loggr.service.dto.WorkLogDTO;
import ch.avsar.loggr.service.mapper.WorkLogMapper;
import ch.avsar.loggr.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static ch.avsar.loggr.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.avsar.loggr.domain.enumeration.WorkLogType;
/**
 * Test class for the WorkLogResource REST controller.
 *
 * @see WorkLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoggrApp.class)
public class WorkLogResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final WorkLogType DEFAULT_TYPE = WorkLogType.WORK;
    private static final WorkLogType UPDATED_TYPE = WorkLogType.VACATION;

    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    @Autowired
    private WorkLogRepository workLogRepository;

    @Autowired
    private WorkLogMapper workLogMapper;

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWorkLogMockMvc;

    private WorkLog workLog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkLogResource workLogResource = new WorkLogResource(workLogService);
        this.restWorkLogMockMvc = MockMvcBuilders.standaloneSetup(workLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkLog createEntity(EntityManager em) {
        WorkLog workLog = new WorkLog()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .type(DEFAULT_TYPE)
            .approved(DEFAULT_APPROVED);
        return workLog;
    }

    @Before
    public void initTest() {
        workLog = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void createWorkLog() throws Exception {
        int databaseSizeBeforeCreate = workLogRepository.findAll().size();

        // Create the WorkLog
        WorkLogDTO workLogDTO = workLogMapper.toDto(workLog);
        restWorkLogMockMvc.perform(post("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkLog in the database
        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeCreate + 1);
        WorkLog testWorkLog = workLogList.get(workLogList.size() - 1);
        assertThat(testWorkLog.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWorkLog.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testWorkLog.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWorkLog.isApproved()).isEqualTo(DEFAULT_APPROVED);
    }

    @Test
    @Transactional
    public void createWorkLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workLogRepository.findAll().size();

        // Create the WorkLog with an existing ID
        workLog.setId(1L);
        WorkLogDTO workLogDTO = workLogMapper.toDto(workLog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkLogMockMvc.perform(post("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workLogRepository.findAll().size();
        // set the field null
        workLog.setStartDate(null);

        // Create the WorkLog, which fails.
        WorkLogDTO workLogDTO = workLogMapper.toDto(workLog);

        restWorkLogMockMvc.perform(post("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isBadRequest());

        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = workLogRepository.findAll().size();
        // set the field null
        workLog.setEndDate(null);

        // Create the WorkLog, which fails.
        WorkLogDTO workLogDTO = workLogMapper.toDto(workLog);

        restWorkLogMockMvc.perform(post("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isBadRequest());

        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workLogRepository.findAll().size();
        // set the field null
        workLog.setType(null);

        // Create the WorkLog, which fails.
        WorkLogDTO workLogDTO = workLogMapper.toDto(workLog);

        restWorkLogMockMvc.perform(post("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isBadRequest());

        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkLogs() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);

        // Get all the workLogList
        restWorkLogMockMvc.perform(get("/api/work-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())));
    }

    @Test
    @Transactional
    public void getWorkLog() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);

        // Get the workLog
        restWorkLogMockMvc.perform(get("/api/work-logs/{id}", workLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workLog.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkLog() throws Exception {
        // Get the workLog
        restWorkLogMockMvc.perform(get("/api/work-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void updateWorkLog() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);
        int databaseSizeBeforeUpdate = workLogRepository.findAll().size();

        // Update the workLog
        WorkLog updatedWorkLog = workLogRepository.findOne(workLog.getId());
        updatedWorkLog
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .type(UPDATED_TYPE)
            .approved(UPDATED_APPROVED);
        WorkLogDTO workLogDTO = workLogMapper.toDto(updatedWorkLog);

        restWorkLogMockMvc.perform(put("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isOk());

        // Validate the WorkLog in the database
        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeUpdate);
        WorkLog testWorkLog = workLogList.get(workLogList.size() - 1);
        assertThat(testWorkLog.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWorkLog.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testWorkLog.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWorkLog.isApproved()).isEqualTo(UPDATED_APPROVED);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void updateNonExistingWorkLog() throws Exception {
        int databaseSizeBeforeUpdate = workLogRepository.findAll().size();

        // Create the WorkLog
        WorkLogDTO workLogDTO = workLogMapper.toDto(workLog);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWorkLogMockMvc.perform(put("/api/work-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workLogDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkLog in the database
        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    public void deleteWorkLog() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);
        int databaseSizeBeforeDelete = workLogRepository.findAll().size();

        // Get the workLog
        restWorkLogMockMvc.perform(delete("/api/work-logs/{id}", workLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkLog> workLogList = workLogRepository.findAll();
        assertThat(workLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkLog.class);
        WorkLog workLog1 = new WorkLog();
        workLog1.setId(1L);
        WorkLog workLog2 = new WorkLog();
        workLog2.setId(workLog1.getId());
        assertThat(workLog1).isEqualTo(workLog2);
        workLog2.setId(2L);
        assertThat(workLog1).isNotEqualTo(workLog2);
        workLog1.setId(null);
        assertThat(workLog1).isNotEqualTo(workLog2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkLogDTO.class);
        WorkLogDTO workLogDTO1 = new WorkLogDTO();
        workLogDTO1.setId(1L);
        WorkLogDTO workLogDTO2 = new WorkLogDTO();
        assertThat(workLogDTO1).isNotEqualTo(workLogDTO2);
        workLogDTO2.setId(workLogDTO1.getId());
        assertThat(workLogDTO1).isEqualTo(workLogDTO2);
        workLogDTO2.setId(2L);
        assertThat(workLogDTO1).isNotEqualTo(workLogDTO2);
        workLogDTO1.setId(null);
        assertThat(workLogDTO1).isNotEqualTo(workLogDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(workLogMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(workLogMapper.fromId(null)).isNull();
    }
}
