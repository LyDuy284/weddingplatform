package com.fu.weddingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.BookingDetailHistory;

public interface BookingDetailHistoryRepository extends JpaRepository<BookingDetailHistory, String> {
    List<BookingDetailHistory> findByBookingDetail(BookingDetail bookingDetail);
}
