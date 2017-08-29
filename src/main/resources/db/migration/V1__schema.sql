CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lqjrcobrh9jc8wpcar64q1bfh` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o3uty20c6csmx5y4uk2tc5r4m` (`phone`),
  KEY `FKj8dlm21j202cadsbfkoem0s58` (`user_id`),
  CONSTRAINT `FKj8dlm21j202cadsbfkoem0s58` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `service_counter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ordinal` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `service_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdk4bsoy793tjke25iaox9vlk0` (`service_id`),
  CONSTRAINT `FKdk4bsoy793tjke25iaox9vlk0` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `status` int(11) DEFAULT NULL,
  `counter_id` bigint(20) DEFAULT NULL,
  `customer_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbbro6t9onaeq87ot4cak9blex` (`counter_id`),
  KEY `FKke9j5rnuwqfkmnyyfa9k2p3ug` (`customer_id`),
  CONSTRAINT `FKbbro6t9onaeq87ot4cak9blex` FOREIGN KEY (`counter_id`) REFERENCES `service_counter` (`id`),
  CONSTRAINT `FKke9j5rnuwqfkmnyyfa9k2p3ug` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `token_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author` tinyblob,
  `comment` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `status` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `counter_id` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhj43euuoqgiechfm2i69isl49` (`counter_id`),
  KEY `FK2oup0x1o4tsqh1nvjj0scpn7w` (`token_id`),
  CONSTRAINT `FK2oup0x1o4tsqh1nvjj0scpn7w` FOREIGN KEY (`token_id`) REFERENCES `token` (`id`),
  CONSTRAINT `FKhj43euuoqgiechfm2i69isl49` FOREIGN KEY (`counter_id`) REFERENCES `service_counter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `token_services` (
  `token_id` bigint(20) NOT NULL,
  `services_id` bigint(20) NOT NULL,
  KEY `FK77xmuu3pkcpjdapjaxdinmsdb` (`services_id`),
  KEY `FKapdpdtscsyigkyht5oihqd303` (`token_id`),
  CONSTRAINT `FK77xmuu3pkcpjdapjaxdinmsdb` FOREIGN KEY (`services_id`) REFERENCES `service` (`id`),
  CONSTRAINT `FKapdpdtscsyigkyht5oihqd303` FOREIGN KEY (`token_id`) REFERENCES `token` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
