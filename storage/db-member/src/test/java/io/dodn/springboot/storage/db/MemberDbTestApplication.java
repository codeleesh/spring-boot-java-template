package io.dodn.springboot.storage.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class MemberDbTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberDbTestApplication.class, args);
    }

}
