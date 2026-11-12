package com.reloop.reward.mapper;

import com.reloop.reward.dto.request.RewardRequest;
import com.reloop.reward.dto.response.RewardResponse;
import com.reloop.reward.entity.Reward;
import org.springframework.stereotype.Component;

@Component
public class RewardMapper {

    public Reward toEntity(RewardRequest request) {
        return Reward.builder()
                .name(request.name().trim())
                .description(request.description())
                .category(request.category().trim())
                .pointsCost(request.pointsCost())
                .stockQuantity(request.stockQuantity())
                .imageUrl(request.imageUrl())
                .rewardType(request.rewardType())
                .active(request.active() == null || request.active())
                .expirationDate(request.expirationDate())
                .build();
    }

    public void updateEntity(Reward reward, RewardRequest request) {
        reward.setName(request.name().trim());
        reward.setDescription(request.description());
        reward.setCategory(request.category().trim());
        reward.setPointsCost(request.pointsCost());
        reward.setStockQuantity(request.stockQuantity());
        reward.setImageUrl(request.imageUrl());
        reward.setRewardType(request.rewardType());
        if (request.active() != null) {
            reward.setActive(request.active());
        }
        reward.setExpirationDate(request.expirationDate());
    }

    public RewardResponse toResponse(Reward reward) {
        return new RewardResponse(
                reward.getId(),
                reward.getName(),
                reward.getDescription(),
                reward.getCategory(),
                reward.getPointsCost(),
                reward.getStockQuantity(),
                reward.getImageUrl(),
                reward.getRewardType(),
                reward.isActive(),
                reward.getExpirationDate(),
                reward.getCreatedAt());
    }
}