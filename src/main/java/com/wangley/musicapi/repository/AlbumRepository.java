package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
