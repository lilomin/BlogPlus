CREATE DATABASE IF NOT EXISTS blogplus
  DEFAULT CHARSET utf8;

USE blogplus;

DROP TABLE IF EXISTS blog_user;
CREATE TABLE blog_user (
  `user_id`         VARCHAR(32) NOT NULL,
  `username`        VARCHAR(32) NOT NULL,
  `nickname`        VARCHAR(32)  DEFAULT NULL,
  `avatar`          VARCHAR(128) DEFAULT NULL,
  `email`           VARCHAR(64)  DEFAULT NULL,
  `sign`            VARCHAR(256) DEFAULT NULL,
  `password`        VARCHAR(64) NOT NULL,
  `create_time`     DATETIME    NOT NULL,
  `update_time`     DATETIME     DEFAULT NULL,
  `last_login_time` DATETIME     DEFAULT NULL,
  PRIMARY KEY (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS blog;
CREATE TABLE blog (
  `blog_id`     VARCHAR(32) NOT NULL,
  `user_id`     VARCHAR(32) NOT NULL,
  `title`       VARCHAR(32) NOT NULL,
  `content`     TEXT        NOT NULL,
  `create_time` DATETIME    NOT NULL,
  `update_time` DATETIME    NOT NULL,
  `description` VARCHAR(256) DEFAULT NULL,
  `hidden`        BOOL         DEFAULT 0,
  `image`       VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`blog_id`, `user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS blog_tag;
CREATE TABLE blog_tag (
  `blog_id`     VARCHAR(32) NOT NULL,
  `user_id`     VARCHAR(32) NOT NULL,
  `tag`         VARCHAR(32) NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`blog_id`, `user_id`, `tag`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE blog ADD COLUMN read_times INT(4) DEFAULT 0;
