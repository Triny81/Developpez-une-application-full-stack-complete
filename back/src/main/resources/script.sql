CREATE TABLE `USER` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) NOT NULL UNIQUE,
  `username` varchar(255) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `admin` boolean NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
);

CREATE TABLE `THEME` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` longtext NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
);

CREATE TABLE `ARTICLE` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `message` longtext NOT NULL,
  `author_id` integer NOT NULL,
  `theme_id` integer NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
);

CREATE TABLE `COMMENT` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `message` longtext NOT NULL,
  `author_id` integer NOT NULL,
  `article_id` integer NOT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
);

CREATE TABLE `SUBSCRIBE` (
  `user_id` integer NOT NULL,
  `theme_id` integer NOT NULL
);

ALTER TABLE `ARTICLE` ADD FOREIGN KEY (`author_id`) REFERENCES `USER` (`id`);
ALTER TABLE `ARTICLE` ADD FOREIGN KEY (`theme_id`) REFERENCES `THEME` (`id`);

ALTER TABLE `COMMENT` ADD FOREIGN KEY (`author_id`) REFERENCES `USER` (`id`);
ALTER TABLE `COMMENT` ADD FOREIGN KEY (`article_id`) REFERENCES `ARTICLE` (`id`);

ALTER TABLE `SUBSCRIBE` ADD FOREIGN KEY (`user_id`) REFERENCES `USER` (`id`);
ALTER TABLE `SUBSCRIBE` ADD FOREIGN KEY (`theme_id`) REFERENCES `THEME` (`id`);