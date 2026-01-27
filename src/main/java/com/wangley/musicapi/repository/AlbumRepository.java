package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Album;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @EntityGraph(attributePaths = "artistas")
    List<Album> findAll();
}
