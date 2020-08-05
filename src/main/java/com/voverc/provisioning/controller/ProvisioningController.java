package com.voverc.provisioning.controller;

import com.voverc.provisioning.exception.DeviceNotFoundException;
import com.voverc.provisioning.service.ProvisioningService;
import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
public class ProvisioningController {

    private ProvisioningService provisioningService;

    public ProvisioningController(ProvisioningService provisioningService) {
        this.provisioningService = provisioningService;
    }

    @GetMapping(
            value = "/provisioning/{macAddress}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<?> connect(@PathVariable String macAddress) throws IOException {

        String fileName = "";

        try {
            fileName = provisioningService.getProvisioningFile(macAddress);
        } catch (DeviceNotFoundException e) {
            return new ResponseEntity<>(e.getExceptionDescription(), HttpStatus.NOT_FOUND);
        }

        File file = new File(fileName);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}