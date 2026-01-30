package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.AlbumCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumCoverRepository  extends JpaRepository<AlbumCover, Long> {
    Optional<AlbumCover> findByAlbumId(Long albumId);

    boolean existsByAlbumId(Long albumId);
}
