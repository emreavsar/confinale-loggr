package ch.avsar.loggr.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import ch.avsar.loggr.domain.enumeration.WorkLogType;

/**
 * A WorkLog.
 */
@Entity
@Table(name = "work_log")
public class WorkLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private WorkLogType type;

    @Column(name = "approved")
    private Boolean approved;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User creator;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public WorkLog startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public WorkLog endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public WorkLogType getType() {
        return type;
    }

    public WorkLog type(WorkLogType type) {
        this.type = type;
        return this;
    }

    public void setType(WorkLogType type) {
        this.type = type;
    }

    public Boolean isApproved() {
        return approved;
    }

    public WorkLog approved(Boolean approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }


    public User getCreator() {
        return creator;
    }

    public WorkLog creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public Project getProject() {
        return project;
    }

    public WorkLog project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkLog workLog = (WorkLog) o;
        if (workLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkLog{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", type=" + type +
            ", approved=" + approved +
            ", project=" + project +
            ", creator=" + creator +
            '}';
    }
}
