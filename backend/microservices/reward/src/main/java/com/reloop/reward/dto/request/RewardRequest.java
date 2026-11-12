package com.reloop.reward.dto.request;

import com.reloop.reward.enums.RewardType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record RewardRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 2000) String description,
        @NotBlank @Size(max = 80) String category,
        @NotNull @Positive Long pointsCost,
        @NotNull @PositiveOrZero Integer stockQuantity,
        @Size(max = 500) String imageUrl,
        @NotNull RewardType rewardType,
        Boolean active,
        @Future LocalDateTime expirationDate
) {
}