package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

}
