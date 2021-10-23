CREATE TABLE `seckill_info`
(
    `seckill_id`   bigint NOT NULL,
    `ctime`        bigint                                  DEFAULT NULL,
    `utime`        bigint                                  DEFAULT NULL,
    `seckill_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `product_id`   varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `price`        decimal(10, 2)                          DEFAULT NULL,
    `count`        bigint                                  DEFAULT NULL,
    `lock_count`   bigint                                  DEFAULT NULL,
    `version`      bigint                                  DEFAULT '0',
    PRIMARY KEY (`seckill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `product_info` (
                                `id` bigint NOT NULL,
                                `ctime` bigint DEFAULT NULL,
                                `utime` bigint DEFAULT NULL,
                                `product_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `product_desc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `price` decimal(10,2) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

CREATE TABLE `seckill_order_info` (
                                      `order_id` bigint NOT NULL,
                                      `ctime` bigint DEFAULT NULL,
                                      `utime` bigint DEFAULT NULL,
                                      `product_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `seckill_id` bigint DEFAULT NULL,
                                      `user_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `status` int DEFAULT NULL,
                                      PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci

CREATE TABLE `user` (
                        `user_id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                        `ctime` bigint DEFAULT NULL,
                        `utime` bigint DEFAULT NULL,
                        `phone` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        `ip_address` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                        PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci