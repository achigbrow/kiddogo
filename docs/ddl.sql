CREATE TABLE IF NOT EXISTS `ActivityEntity`
(
    `activity_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `user_id`     INTEGER                           NOT NULL,
    `timestamp`   INTEGER,
    `time`        INTEGER                           NOT NULL,
    `result`      INTEGER                           NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `UserEntity` (`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX `index_ActivityEntity_user_id` ON `ActivityEntity` (`user_id`);

CREATE TABLE IF NOT EXISTS `UserEntity`
(
    `user_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `name`    TEXT
);