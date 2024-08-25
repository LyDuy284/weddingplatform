package com.fu.weddingplatform.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

  @Query(nativeQuery = true, value = " SELECT bd.* FROM booking_detail bd \n" + //
      "   join service_supplier ss on bd.service_supplier_id = ss.id \n" + //
      "   join supplier s on s.id = ss.supplier_id \n" + //
      "where s.id = ?1 and bd.booking_id = ?2\n" + //
      "order by bd.create_at desc")
  List<BookingDetail> findBySupplierAndBooking(String supplierId, String bookingId);

  @Query("select bd from BookingDetail bd where bd.serviceSupplier.supplier.id = ?1 and bd in ?2")
  List<BookingDetail> findListBookingDetailBySupplierId(String supplierId, List<BookingDetail> bookingDetail);

  @Query("select bd from BookingDetail bd where bd.id in ?1")
  List<BookingDetail> findListBookingDetailInList(List<String> bookingDetailId);

  @Query(value = "SELECT bd.* FROM booking_detail bd where bd.booking_id = ?1 and bd.status != 'REJECTED' and bd.status != 'CANCELED'", nativeQuery = true)
  List<BookingDetail> findValidBookingDetailByBooking(String bookingId);

  int countByStatusInAndBooking(List<String> statuses, Booking booking);

  int countByStatusNotInAndBooking(List<String> statuses, Booking booking);

  @Query(value = "SELECT bd.* FROM booking_detail bd \n" + //
      " where bd.status not like 'CANCELED' and  bd.status not like 'REJECTED' \n" + //
      " and bd.booking_id = ?1", nativeQuery = true)
  List<BookingDetail> findAvailableBookingDetailByBooking(String bookingId);
}
