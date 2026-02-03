package com.wangley.musicapi.repository;

import com.wangley.musicapi.domain.entity.Artist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @EntityGraph(attributePaths = "albums")
    List<Artist> findAll();
}
