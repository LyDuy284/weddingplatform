package com.fu.weddingplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fu.weddingplatform.entity.InvoiceDetail;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, String> {
    @Query("select id from InvoiceDetail id where id.bookingDetail.id = ?1 and id.isDeposit = true and id.status = 'COMPLETED'")
    Optional<InvoiceDetail> findDepositedInvoiceDetailByBookingDetailId(String bookingDetailId);

    @Query("select id from InvoiceDetail id where id.bookingDetail.id = ?1 and id.status = 'COMPLETED'")
    List<InvoiceDetail> findCompletedInvoiceDetail(String bookingDetailId);

    @Query(value = "SELECT COALESCE(SUM(price), 0) as price FROM the_day.invoice_detail where booking_detail_id = ?1 and status = 'COMPLETED'", nativeQuery = true)
    int getPayMentPriceByBookingDetail(String id);
}
