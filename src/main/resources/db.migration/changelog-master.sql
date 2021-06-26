-- liquibase formatted sql
-- changeset Liquibase:1 -- Database: music_player

CREATE TABLE IF NOT EXISTS Album
(
    id    BIGINT  NOT NULL AUTO_INCREMENT,
    name  VARCHAR(250) NOT NULL,
    year  BIGINT  NOT NULL,
    notes VARCHAR(250) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Genre
(
    id  BIGINT NOT NULL AUTO_INCREMENT,
    genreName VARCHAR(250) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Artist
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    artistName  VARCHAR(250) NOT NULL,
    artistNotes VARCHAR(250) NOT NULL,
    genreId     BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT Genre
        FOREIGN KEY (genreId)
            REFERENCES Genre (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Storage
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    storageType VARCHAR(250) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Source
(
    id            BIGINT auto_increment,
    storage_id    bigint       not null,
    song_id       bigint       not null,
    name          varchar(250) not null,
    path          varchar(250) not null,
    size          bigint       not null,
    checksum      varchar(250) not null,
    storage_types varchar(250) not null,
    file_type     varchar(250) not null,
    PRIMARY KEY (id),
    CONSTRAINT Storage
        foreign key (storage_id)
            references Storage (id)
            on update cascade
            on delete cascade
);

CREATE TABLE IF NOT EXISTS Song
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    album_id BIGINT       NOT NULL,
    name     VARCHAR(250) NOT NULL,
    notes    VARCHAR(250) NOT NULL,
    year     BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT Album
        FOREIGN KEY (album_id)
            REFERENCES Album (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS SourceMetadata
(
    type VARCHAR(250) NOT NULL,
    path VARCHAR(250) NOT NULL,
    PRIMARY KEY (type)
);

-- changeset Liquibase:2
INSERT into  Album (id, name ,year ,notes) VALUES (1, 'Spring', 2005, 'Baeldung');
INSERT IGNORE  Song (id, album_id , name ,notes ,year) values (1, 1 , 'firstSong','firstSongNotes',2020);
INSERT IGNORE  Storage (id, storageType) VALUES (1, 'FILE_SYSTEM');
INSERT IGNORE  Storage (id, storageType) VALUES (2, 'CLOUD_STORAGE');
INSERT IGNORE  Source (id, storage_id , song_id ,name ,path,size, checksum, storage_types ,file_type)
values (1, 1 ,1,'firstSongSource','firstSongSourcePath',11111,'b25b589bf15fe218cbdcd91e7fdfc959','FILE_SYSTEM','audio/wave');
