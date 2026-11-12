package com.reloop.reward.controller;

import com.reloop.reward.dto.request.RewardRequest;
import com.reloop.reward.dto.response.PageResponse;
import com.reloop.reward.dto.response.RewardResponse;
import com.reloop.reward.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.net.URI;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
@Tag(name = "Rewards", description = "Reward catalog")
public class RewardController {

    private final RewardService rewardService;

    @GetMapping
    @Operation(summary = "List available rewards, optionally filtered by category")
    public PageResponse<RewardResponse> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("pointsCost").ascending()
        );

        return rewardService.listAvailable(category, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a reward by id")
    public RewardResponse getById(@PathVariable Long id) {
        return rewardService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a reward (ADMIN)")
    public ResponseEntity<RewardResponse> create(@Valid @RequestBody RewardRequest request) {
        RewardResponse created = rewardService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a reward (ADMIN)")
    public RewardResponse update(@PathVariable Long id, @Valid @RequestBody RewardRequest request) {
        return rewardService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate a reward (ADMIN, soft delete)")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        rewardService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}