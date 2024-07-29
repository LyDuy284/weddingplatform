package com.fu.weddingplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fu.weddingplatform.entity.Booking;
import com.fu.weddingplatform.entity.Couple;

public interface BookingRepository extends JpaRepository<Booking, String> {

  Page<Booking> findByCouple(Couple couple, PageRequest request);

}
