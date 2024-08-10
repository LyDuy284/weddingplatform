package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionSummaryRepository extends JpaRepository<TransactionSummary, String> {
    @Query("select ts from TransactionSummary ts where ts.booking.id = ?1")
    Optional<TransactionSummary> findByBookingId(String bookingId);

    Optional<TransactionSummary> findFirstByBooking(Booking booking);
}
