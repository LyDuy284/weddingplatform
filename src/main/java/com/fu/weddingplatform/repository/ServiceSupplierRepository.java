package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.ServiceSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ServiceSupplierRepository extends JpaRepository<ServiceSupplier, String> {
    // @Query("select distinct ss from ServiceSupplier ss join Services s on ss.id = s.serviceSupplier.id " +
    //         "join BookingDetail db on db.service.id = s.id where db.booking.id = ?1")
    // ServiceSupplier findSupplierByBookingDetailId(String bookingId);
}
