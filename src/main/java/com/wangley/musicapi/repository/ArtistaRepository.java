package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
}
