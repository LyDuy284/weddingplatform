package com.fu.weddingplatform.repository;

import java.util.List;
import java.util.Optional;

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
      "where s.id = ?1 \n" + //
      "order by bd.create_at desc")
  List<BookingDetail> findBySupplier(String supplierId);

  @Query("select bd from BookingDetail bd where bd.serviceSupplier.supplier.id = ?1 and bd in ?2")
  List<BookingDetail> findListBookingDetailBySupplierId(String supplierId, List<BookingDetail> bookingDetail);

  @Query("select bd from BookingDetail bd where bd.id in ?1")
  List<BookingDetail> findListBookingDetailInList(List<String> bookingDetailId);
}
