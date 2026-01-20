INSERT INTO artista(id, nome, tipo)
VALUES (1, 'Serj Tankian', 'CANTOR'),
       (2, 'Mike Shinoda', 'CANTOR'),
       (3, 'Michel Teló', 'CANTOR'),
       (4, 'Guns N’ Roses', 'BANDA');

INSERT INTO album(id, nome, data_lancamento)
VALUES (1, 'Harakiri', '2012-07-10'),
       (2, 'Black Blooms', '2015-05-01'),
       (3, 'The Rough Dog', '2021-01-15'),

       (4, 'The Rising Tied', '2005-11-22'),
       (5, 'Post Traumatic', '2018-06-15'),
       (6, 'Post Traumatic EP', '2018-01-12'),
       (7, 'Where’d You Go', '2006-04-25'),

       (8, 'Bem Sertanejo', '2014-11-04'),
       (9, 'Bem Sertanejo - O Show (Ao Vivo)', '2016-10-21'),
       (10, 'Bem Sertanejo - (1ª Temporada) - EP', '2018-01-01'),

       (11, 'Use Your Illusion I', '1991-09-17'),
       (12, 'Use Your Illusion II', '1991-09-17'),
       (13, 'Greatest Hits', '2004-03-23');

INSERT INTO artista_album (artista_id, album_id)
VALUES  (1, 1),
        (1, 2),
        (1, 3),
        (2, 4),
        (2, 5),
        (2, 6),
        (2, 7),
        (3, 8),
        (3, 9),
        (3, 10),
        (4, 11),
        (4, 12),
        (4, 13);

SELECT setval(pg_get_serial_sequence('artista', 'id'), (SELECT MAX(id) FROM artista));
SELECT setval(pg_get_serial_sequence('album', 'id'), (SELECT MAX(id) FROM album));



