package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Regional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long> {

    Optional<Regional> findByCodigoExternoAndAtivoTrue(Integer codigoExterno);

    List<Regional> findAllByAtivoTrue();
}
