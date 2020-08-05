package com.voverc.provisioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("com.voverc.provisioning")
public class ProvisioningApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProvisioningApplication.class, args);
    }

}