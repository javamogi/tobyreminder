package toby.ai.tobyreminder.dto;

import lombok.Builder;
import toby.ai.tobyreminder.domain.ReminderList;

import java.time.LocalDateTime;

@Builder
public record ReminderListResponse(
        Long id,
        String name,
        String color,
        String icon,
        Integer displayOrder,
        long reminderCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReminderListResponse from(ReminderList list, long reminderCount) {
        return ReminderListResponse.builder()
                .id(list.getId())
                .name(list.getName())
                .color(list.getColor())
                .icon(list.getIcon())
                .displayOrder(list.getDisplayOrder())
                .reminderCount(reminderCount)
                .createdAt(list.getCreatedAt())
                .updatedAt(list.getUpdatedAt())
                .build();
    }

    public static ReminderListResponse from(ReminderList list) {
        return from(list, 0);
    }
}
