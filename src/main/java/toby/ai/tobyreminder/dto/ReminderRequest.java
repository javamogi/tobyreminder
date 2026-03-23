package toby.ai.tobyreminder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import toby.ai.tobyreminder.domain.Priority;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ReminderRequest(
        @NotBlank String title,
        String notes,
        LocalDate dueDate,
        LocalTime dueTime,
        Priority priority,
        Boolean flagged
) {
}
