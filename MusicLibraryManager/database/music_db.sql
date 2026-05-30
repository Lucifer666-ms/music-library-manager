-- Run this file in MySQL Workbench or CLI before launching the app
CREATE DATABASE IF NOT EXISTS music_db;
USE music_db;

DROP TABLE IF EXISTS songs;

CREATE TABLE songs (
    song_id   INT PRIMARY KEY AUTO_INCREMENT,
    album_id  INT          NOT NULL DEFAULT 1,
    title     VARCHAR(150) NOT NULL,
    artist    VARCHAR(150) NOT NULL,
    duration  VARCHAR(10)  NOT NULL,
    genre     VARCHAR(80)  DEFAULT 'Unknown',
    liked     BOOLEAN      DEFAULT FALSE
);

INSERT INTO songs (album_id, title, artist, duration, genre, liked) VALUES
(1, 'Kesariya',          'Arijit Singh',      '4:28', 'Bollywood', TRUE),
(1, 'Tum Hi Ho',         'Arijit Singh',      '4:22', 'Bollywood', FALSE),
(2, 'Raataan Lambiyan',  'Jubin Nautiyal',    '3:52', 'Bollywood', TRUE),
(2, 'Heeriye',           'Jasleen Royal',     '3:45', 'Indie',     FALSE),
(3, 'Apna Bana Le',      'Arijit Singh',      '4:10', 'Bollywood', FALSE),
(3, 'Besharam Rang',     'Caralisa Monteiro', '3:30', 'Pop',       FALSE),
(4, 'Blinding Lights',   'The Weeknd',        '3:20', 'Pop',       TRUE),
(4, 'Shape of You',      'Ed Sheeran',        '3:53', 'Pop',       FALSE);
