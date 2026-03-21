package toby.ai.tobyreminder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ReminderRequest(
        @NotBlank String title,
        String notes
) {
}
