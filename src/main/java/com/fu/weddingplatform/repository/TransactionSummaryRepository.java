package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.TransactionSummary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionSummaryRepository extends JpaRepository<TransactionSummary, String>, JpaSpecificationExecutor<TransactionSummary> {
    @Query("select ts from TransactionSummary ts where ts.booking.id = ?1")
    Optional<TransactionSummary> findByBookingId(String bookingId);

    Optional<TransactionSummary> findFirstByBooking(Booking booking);

    @Query("select ts from TransactionSummary ts where year(cast(ts.dateModified as date)) = ?1 and month(cast(ts.dateModified as date)) =?2")
    List<TransactionSummary> findAllByMonthAndYear(int year, int month);
}
