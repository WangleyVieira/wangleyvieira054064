CREATE TABLE artista (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

CREATE TABLE album (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    data_lancamento DATE
);

CREATE TABLE artista_album (
   artista_id BIGINT NOT NULL,
   album_id BIGINT NOT NULL,
   PRIMARY KEY (artista_id, album_id),

   CONSTRAINT fk_artista_album_artista
       FOREIGN KEY (artista_id)
           REFERENCES artista (id),

   CONSTRAINT fk_artista_album_album
       FOREIGN KEY (album_id)
           REFERENCES album (id)
);