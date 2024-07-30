package com.fu.weddingplatform.repository;

import com.fu.weddingplatform.entity.ComboServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComboServiceRepository extends JpaRepository<ComboServices, String> {
}
