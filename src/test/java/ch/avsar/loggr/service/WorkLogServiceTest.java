package ch.avsar.loggr.service;

import ch.avsar.loggr.LoggrApp;
import ch.avsar.loggr.domain.Project;
import ch.avsar.loggr.domain.User;
import ch.avsar.loggr.domain.WorkLog;
import ch.avsar.loggr.repository.WorkLogRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LoggrApp.class)
public class WorkLogServiceTest {
    @Autowired
    WorkLogService workLogService;

    @MockBean
    WorkLogRepository workLogRepository;

    @Test
    public void worklogPerProject() throws Exception {
        // given there are 8 worklogs in 2 projects as follows
        List<WorkLog> allWorkLogs = getWorkLogs(5, "Project A", "Foo", "Bar", "foobar");
        allWorkLogs.addAll(getWorkLogs(3, "Project B", "Foo", "Bar", "foobar"));
        Mockito.when(workLogRepository.findAll()).thenReturn(allWorkLogs);

        // when the statistics per project gets retrieved
        Map<String, Double> result = workLogService.getStatisticPerProject();

        // then the statistic must contain the total worked hours summed up for each project
        Assertions.assertThat(result.get("Project A")).isEqualTo(0 + 1 + 2 + 3 + 4);
        Assertions.assertThat(result.get("Project B")).isEqualTo(0 + 1 + 2);
    }


    @Test
    public void worklogPerEmployee() throws Exception {
        // given there are 8 worklogs for 3 employees as follows
        List<WorkLog> allWorkLogs = getWorkLogs(1, "Project A", "Foo", "Bar", "foobar");
        allWorkLogs.addAll(getWorkLogs(4, "Project A", "Steve", "Wozniak", "woz"));
        allWorkLogs.addAll(getWorkLogs(2, "Project B", "Florian", "Schrag", "flo"));
        Mockito.when(workLogRepository.findAll()).thenReturn(allWorkLogs);

        // when the statistics per employee gets retrieved
        Map<String, Double> result = workLogService.getStatisticPerEmployee();

        // then the statistic must contain the total worked hours summed up for each project
        Assertions.assertThat(result.get("Foo Bar (foobar)")).isEqualTo(0);
        Assertions.assertThat(result.get("Steve Wozniak (woz)")).isEqualTo(0 + 1 + 2 + 3);
        Assertions.assertThat(result.get("Florian Schrag (flo)")).isEqualTo(0 + 1);
    }

    // generates some mock objects
    private List<WorkLog> getWorkLogs(int numberOfWorklogs, String projectName, String firstname, String lastname, String login) {
        List<WorkLog> workLogs = new ArrayList<>(numberOfWorklogs);

        Project project = new Project();
        User creator = new User();
        project.setName(projectName);
        creator.setFirstName(firstname);
        creator.setLastName(lastname);
        creator.setLogin(login);

        for (int i = 0; i < numberOfWorklogs; i++) {
            WorkLog workLog = new WorkLog();
            workLog.setCreator(creator);
            workLog.setProject(project);

            // start date is now, each worklog increments hours of work (we love to work more and more.., or we find more bugs to fix)
            ZonedDateTime startDate = ZonedDateTime.now();
            ZonedDateTime endDate = ZonedDateTime.now().plusHours(i);

            workLog.setStartDate(startDate);
            workLog.setEndDate(endDate);
            workLogs.add(workLog);
        }

        return workLogs;
    }
}
