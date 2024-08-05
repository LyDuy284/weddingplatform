package com.fu.weddingplatform.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.BookingDetail;
import com.fu.weddingplatform.entity.ServiceSupplier;

@Repository
@Transactional
public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {

  List<BookingDetail> findByServiceSupplierAndStatus(ServiceSupplier serviceSupplier, String status);

  List<BookingDetail> findByBookingAndStatus(Booking booking, String status);

  List<BookingDetail> findByBookingAndStatusNot(Booking booking, String status);

}
