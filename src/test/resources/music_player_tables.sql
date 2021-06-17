CREATE SCHEMA IF NOT EXISTS `music_player` DEFAULT CHARACTER SET utf8;
USE `music_player`;

CREATE TABLE IF NOT EXISTS `music_player`.`Album`
(
    `id`    BIGINT       NOT NULL AUTO_INCREMENT,
    `name`  VARCHAR(250) NOT NULL,
    `year`  BIGINT       NOT NULL,
    `notes` VARCHAR(250) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `music_player`.`Genre`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `genreName` VARCHAR(250) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `music_player`.`Artist`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `artistName`  VARCHAR(250) NOT NULL,
    `artistNotes` VARCHAR(250) NOT NULL,
    `genreId`     BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `Genre`
        FOREIGN KEY (`genreId`)
            REFERENCES `music_player`.`Genre` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `music_player`.`Storage`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `storageType` VARCHAR(250) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `music_player`.`Source`
(
    id            bigint auto_increment,
    storage_id    bigint       not null,
    song_id       bigint       not null,
    name          varchar(250) not null,
    path          varchar(250) not null,
    size          bigint       not null,
    checksum      varchar(250) not null,
    storage_types varchar(250) not null,
    file_type     varchar(250) not null,
    PRIMARY KEY (`id`),
    constraint `Storage`
        foreign key (`storage_id`) references `Storage` (`id`)
            on update cascade on delete cascade
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `music_player`.`Song`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `album_id`  BIGINT       NOT NULL,
    `name`      VARCHAR(250) NOT NULL,
    `notes`     VARCHAR(250) NOT NULL,
    `year`      BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `Album`
        FOREIGN KEY (`album_id`)
            REFERENCES `music_player`.`Album` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `music_player`.`SourceMetadata`
(
    `type` VARCHAR(250) NOT NULL,
    `path` VARCHAR(250) NOT NULL,
    PRIMARY KEY (`type`)
)
    ENGINE = InnoDB;