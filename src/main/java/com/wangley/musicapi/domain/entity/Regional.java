package com.wangley.musicapi.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "regional")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Regional {

    @Id
    private Integer id;

    @Column(length = 200, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;
}
