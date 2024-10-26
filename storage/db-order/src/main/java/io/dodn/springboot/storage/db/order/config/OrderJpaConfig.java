package io.dodn.springboot.storage.db.order.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "io.dodn.springboot.storage.db.order")
@EnableJpaRepositories(basePackages = "io.dodn.springboot.storage.db.order")
class OrderJpaConfig {

}
