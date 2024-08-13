package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, String> {
    @Query("select id from InvoiceDetail id where id.bookingDetail.id = ?1 and id.isDeposit = true and id.status = 'COMPLETED'")
    Optional<InvoiceDetail> findDepositedInvoiceDetailByBookingDetailId(String bookingDetailId);

    @Query("select id from InvoiceDetail id where id.bookingDetail.id = ?1 and id.status = 'COMPLETED'")
    List<InvoiceDetail> findCompletedInvoiceDetail(String bookingDetailId);
}
