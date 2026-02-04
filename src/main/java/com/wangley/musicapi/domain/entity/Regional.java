package com.wangley.musicapi.domain.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_externo", nullable = false)
    private Integer codigoExterno;

    @Column(length = 200, nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;
}
