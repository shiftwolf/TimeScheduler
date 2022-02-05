CREATE DATABASE IF NOT EXISTS `scheduler`;
USE `scheduler`;

CREATE TABLE `events` (

    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `name` varchar(50) NOT NULL DEFAULT 'unnamed',
    `date` timestamp NOT NULL,
    `duration` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    `location` varchar(50) DEFAULT NULL,
    `priority` int(11) DEFAULT NULL,

    PRIMARY KEY (`id`)

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE `users` (

    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `email` varchar(50) NOT NULL,
    `username` varchar(50) NOT NULL,
    `name` varchar(50) NOT NULL,
    `hashedpw` varchar(100) NOT NULL,
    `isAdmin` BOOL NOT NULL,

    PRIMARY KEY (`id`)

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE `reminders` (
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
    `event_id` int(10) unsigned NOT NULL,

    PRIMARY KEY (`id`),
    KEY `event_id` (`event_id`),

    CONSTRAINT `reminders_ibfk_1` 
    FOREIGN KEY (`event_id`) 
    REFERENCES `events` (`id`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE `attachments` (

    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `event_id` int(10) unsigned NOT NULL,
    `attachment` varbinary(60000) NOT NULL,
    `name` varchar(255) NOT NULL,

    PRIMARY KEY (`id`),
    KEY `attachments_relation_1` (`event_id`),

    CONSTRAINT `attachments_relation_1` 
    FOREIGN KEY (`event_id`) 
    REFERENCES `events` (`id`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;


CREATE TABLE `participants` (

    `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
    `event_id` int(10) unsigned NOT NULL,
    `user_id` int(10) unsigned NOT NULL,

    PRIMARY KEY (`event_id`, `user_id`),
    KEY `participants_relation_2` (`user_id`),

    CONSTRAINT `participants_relation_1` 
    FOREIGN KEY (`event_id`) 
    REFERENCES `events` (`id`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,

    CONSTRAINT `participants_relation_2` 
    FOREIGN KEY (`user_id`) 
    REFERENCES `users` (`id`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `tokens` (
    `token` varchar(255) NOT NULL,
    `user_id` int(10) unsigned NOT NULL,
    
    PRIMARY KEY (`token`),
    KEY `tokens_relation_1` (`user_id`),

    CONSTRAINT `tokens_relation_1` 
    FOREIGN KEY (`user_id`) 
    REFERENCES `users` (`id`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
