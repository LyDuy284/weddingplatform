package com.fu.weddingplatform.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fu.weddingplatform.entity.SentEmail;

@Transactional
@Repository
public interface EmailRepository extends JpaRepository<SentEmail, Integer> {

}
