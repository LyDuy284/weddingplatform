package com.fu.weddingplatform.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingHistory;

@Repository
@Transactional
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, String> {
    List<BookingHistory> findByBooking(Booking booking);
}
