package com.fu.weddingplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.Couple;

public interface BookingRepository extends JpaRepository<Booking, String> {

  List<Booking> findByCouple(Couple couple);

  @Query(nativeQuery = true, value = "SELECT distinct bd.booking_id \n" + //
      " FROM service s \n" + //
      "    join service_supplier ss on s.service_supplier_id = ss.id \n" + //
      "    join booking_detail bd on bd.service_id = s.id \n" + //
      " where ss.id = ?1 \n" + //
      " order by bd.booking_id desc")
  List<String> findBookingIdBySupplierId(String supplierId);

  @Query(nativeQuery = true, value = "SELECT distinct b.*  FROM the_day.booking_detail bd \n" + //
      "    join service_supplier ss on bd.service_supplier_id = ss.id \n" + //
      "    join supplier s on s.id = ss.supplier_id \n" + //
      "    join booking b on b.id = bd.booking_id \n" + //
      "where s.id = ?1")
  List<Booking> findBookingBySupplierId(String supplierId);

}
