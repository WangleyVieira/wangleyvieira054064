package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
