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

INSERT INTO `user` (`id`,`admin`,`created_at`,`email`,`password`,`updated_at`,`username`) VALUES (1,true,'2025-01-17 16:51:39.319000','admin@admin.com','$2a$10$KnXBfkEkt/yTfOlR2poiz.KDtk2MyGf4iR8n7YDlhUAJI1mhzOZ/m','2025-01-31 15:14:55.288000','admin');

INSERT INTO `theme` (`id`,`created_at`,`description`,`name`,`updated_at`) VALUES (1,'2025-01-20 09:50:54.729000','Java est un langage orienté objet, polyvalent et portable grâce à la JVM. Utilisé pour des applications web, mobiles et entreprises, il offre une vaste bibliothèque et une grande communauté, favorisant le développement rapide.','Java','2025-01-20 09:50:54.729000');
INSERT INTO `theme` (`id`,`created_at`,`description`,`name`,`updated_at`) VALUES (2,'2025-01-20 09:51:06.528000','Thème sur la programmation PHP','PHP','2025-01-20 09:51:06.528000');
INSERT INTO `theme` (`id`,`created_at`,`description`,`name`,`updated_at`) VALUES (3,'2025-01-20 09:51:13.192000','Thème sur la programmation C#','C#','2025-01-20 09:52:11.870000');