package io.dodn.springboot.storage.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CoreKafkaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreKafkaTestApplication.class, args);
    }

}
