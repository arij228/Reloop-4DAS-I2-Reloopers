package com.reloop.reward.dto.response;

import com.reloop.reward.enums.RewardType;

import java.time.LocalDateTime;

public record RewardResponse(
        Long id,
        String name,
        String description,
        String category,
        Long pointsCost,
        Integer stockQuantity,
        String imageUrl,
        RewardType rewardType,
        boolean active,
        LocalDateTime expirationDate,
        LocalDateTime createdAt
) {
}