package com.reloop.transactionservice.repository;

import com.reloop.transactionservice.entity.CustomerOrder;
import com.reloop.transactionservice.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    // For fetching user's order history
    Page<CustomerOrder> findByCustomerId(String customerId, Pageable pageable);

    // For verifying order exists and belongs to user
    Optional<CustomerOrder> findByIdAndCustomerId(Long id, String customerId);

    // For generating unique order numbers
    boolean existsByOrderNumber(String orderNumber);
}