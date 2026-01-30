package com.wangley.musicapi.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "album_cover")
public class AlbumCover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "object_name", nullable = false)
    private String objectName;

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    protected AlbumCover() {}

    public AlbumCover(String objectName, Album album) {
        this.objectName = objectName;
        this.album = album;
    }

    public Long getId() {
        return id;
    }

    public String getObjectName() {
        return objectName;
    }

    public Album getAlbum() {
        return album;
    }
}



