package toby.ai.tobyreminder.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private ReminderList list;

    @Column(nullable = false)
    private String title;

    private String notes;

    @Column(nullable = false)
    private boolean completed;

    private LocalDateTime completedAt;

    private LocalDate dueDate;

    private LocalTime dueTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.NONE;

    @Column(nullable = false)
    private boolean flagged;

    @Column(nullable = false)
    private Integer displayOrder = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Reminder(ReminderList list, String title, String notes, LocalDate dueDate,
                    LocalTime dueTime, Priority priority, Boolean flagged, Integer displayOrder) {
        this.list = list;
        this.title = title;
        this.notes = notes;
        this.completed = false;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.priority = priority != null ? priority : Priority.NONE;
        this.flagged = flagged != null ? flagged : false;
        this.displayOrder = displayOrder != null ? displayOrder : 0;
    }

    public void update(String title, String notes, LocalDate dueDate, LocalTime dueTime,
                       Priority priority, Boolean flagged) {
        this.title = title;
        this.notes = notes;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        if (priority != null) this.priority = priority;
        if (flagged != null) this.flagged = flagged;
    }

    public void toggleComplete() {
        this.completed = !this.completed;
        this.completedAt = this.completed ? LocalDateTime.now() : null;
    }

    public void toggleFlag() {
        this.flagged = !this.flagged;
    }

    public void changeList(ReminderList list) {
        this.list = list;
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
