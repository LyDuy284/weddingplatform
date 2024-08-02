package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.BookingDetail;

import java.util.Optional;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {
    @Query("select bd from  BookingDetail bd where bd.id = ?1 and bd.status = ?2")
    Optional<BookingDetail> findBookingDetailByIdAndStatus(String id, String status);
}
