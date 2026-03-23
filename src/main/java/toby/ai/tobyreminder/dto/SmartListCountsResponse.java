package toby.ai.tobyreminder.dto;

import lombok.Builder;

@Builder
public record SmartListCountsResponse(
        long today,
        long scheduled,
        long all,
        long flagged,
        long completed
) {
}
