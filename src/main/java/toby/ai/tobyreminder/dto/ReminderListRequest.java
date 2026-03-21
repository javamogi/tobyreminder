package toby.ai.tobyreminder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ReminderListRequest(
        @NotBlank String name,
        String color,
        String icon
) {
}
