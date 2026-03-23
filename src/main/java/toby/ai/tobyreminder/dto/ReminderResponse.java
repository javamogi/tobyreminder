package toby.ai.tobyreminder.dto;

import lombok.Builder;
import toby.ai.tobyreminder.domain.Priority;
import toby.ai.tobyreminder.domain.Reminder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ReminderResponse(
        Long id,
        Long listId,
        String title,
        String notes,
        boolean completed,
        LocalDateTime completedAt,
        LocalDate dueDate,
        LocalTime dueTime,
        Priority priority,
        boolean flagged,
        Integer displayOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReminderResponse from(Reminder reminder) {
        return ReminderResponse.builder()
                .id(reminder.getId())
                .listId(reminder.getList().getId())
                .title(reminder.getTitle())
                .notes(reminder.getNotes())
                .completed(reminder.isCompleted())
                .completedAt(reminder.getCompletedAt())
                .dueDate(reminder.getDueDate())
                .dueTime(reminder.getDueTime())
                .priority(reminder.getPriority())
                .flagged(reminder.isFlagged())
                .displayOrder(reminder.getDisplayOrder())
                .createdAt(reminder.getCreatedAt())
                .updatedAt(reminder.getUpdatedAt())
                .build();
    }
}
