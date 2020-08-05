package com.voverc.provisioning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "provisioning")
public class ProvisioningProperty {
    private String domain;
    private String port;
    private String codecs;
    private String timeout;
}
