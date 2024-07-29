package com.fu.weddingplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.BookingDetail;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {

}
