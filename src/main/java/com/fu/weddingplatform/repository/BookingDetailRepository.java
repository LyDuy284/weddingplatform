package com.fu.weddingplatform.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.BookingDetail;

@Repository
@Transactional
public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {

}
