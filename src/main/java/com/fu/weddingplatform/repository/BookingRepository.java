package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, String> {
}
