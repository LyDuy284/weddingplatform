package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.constant.invoice.InvoiceStatus;
import com.fu.weddingplatform.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Invoice;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    @Query("select i from Invoice i where i.booking.id = ?1 and i.status = ?2")
    List<Invoice> findByBookingIdAndStatus(String bookingId, String status);

    List<Invoice> findByCreateAtLessThanEqualAndStatus(String dateTime, String status);
}
