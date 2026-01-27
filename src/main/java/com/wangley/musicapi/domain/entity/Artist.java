package com.wangley.musicapi.domain.entity;

import com.wangley.musicapi.domain.enums.TypeArtist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artista")
@Getter
@Setter
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeArtist tipo;

    @ManyToMany(mappedBy = "artistas")
    private Set<Album> albums = new HashSet<>();
}
