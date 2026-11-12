package com.reloop.reward.repository;

import com.reloop.reward.entity.Reward;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    @Query("""
            select r from Reward r
            where r.active = true
              and (r.expirationDate is null or r.expirationDate > :now)
            """)
    Page<Reward> findAvailable(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("""
            select r from Reward r
            where r.active = true
              and (r.expirationDate is null or r.expirationDate > :now)
              and lower(r.category) = lower(:category)
            """)
    Page<Reward> findAvailableByCategory(@Param("category") String category,
                                         @Param("now") LocalDateTime now,
                                         Pageable pageable);

    // Used by the redemption phase: locks the row so two users cannot take the last item.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Reward r where r.id = :id")
    Optional<Reward> findByIdForUpdate(@Param("id") Long id);
}