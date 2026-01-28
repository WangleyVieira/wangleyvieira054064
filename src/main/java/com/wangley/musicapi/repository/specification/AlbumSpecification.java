package com.wangley.musicapi.repository.specification;

import com.wangley.musicapi.domain.entity.Album;
import com.wangley.musicapi.domain.entity.Artist;
import com.wangley.musicapi.domain.enums.TypeArtist;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlbumSpecification {

    public static Specification<Album> filter(
            TypeArtist tipo,
            String nomeArtista,
            String nomeAlbum,
            Sort.Direction sortDirection
    ) {
        return (root, query, cb) -> {

            Join<Album, Artist> artistJoin =
                    root.join("artistas", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (tipo != null) {
                predicates.add(cb.equal(artistJoin.get("tipo"), tipo));
            }

            if (nomeArtista != null && !nomeArtista.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(artistJoin.get("nome")),
                                "%" + nomeArtista.toLowerCase() + "%"
                        )
                );
            }

            if (nomeAlbum != null && !nomeAlbum.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nome")),
                                "%" + nomeAlbum.toLowerCase() + "%"
                        )
                );
            }

            // Ordenação somente aqui
            if (sortDirection != null) {
                query.orderBy(
                        sortDirection.isAscending()
                                ? cb.asc(artistJoin.get("nome"))
                                : cb.desc(artistJoin.get("nome"))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}



