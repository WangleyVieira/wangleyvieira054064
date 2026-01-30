CREATE TABLE album_cover (
     id BIGSERIAL PRIMARY KEY,
     object_name VARCHAR(255) NOT NULL,
     album_id BIGINT NOT NULL,

     CONSTRAINT fk_album_cover_album
         FOREIGN KEY (album_id)
             REFERENCES album(id)
);


-- Otimizar as buscas
CREATE INDEX idx_album_cover_album_id ON album_cover(album_id);