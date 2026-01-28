package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumRepository extends
        JpaRepository<Album, Long>,
        JpaSpecificationExecutor<Album> {

    @Override
    @EntityGraph(attributePaths = "artistas")
    Page<Album> findAll(Specification<Album> spec, Pageable pageable);
}

