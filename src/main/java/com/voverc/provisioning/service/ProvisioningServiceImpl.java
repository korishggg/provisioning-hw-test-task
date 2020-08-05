package com.voverc.provisioning.service;

import com.voverc.provisioning.config.ProvisioningProperty;
import com.voverc.provisioning.entity.Device;
import com.voverc.provisioning.exception.DeviceNotFoundException;
import com.voverc.provisioning.repository.DeviceRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.*;
import java.util.Properties;
@Service
public class ProvisioningServiceImpl implements ProvisioningService {

    private DeviceRepository deviceRepository;
    private ProvisioningProperty provisioningProperty;


    public ProvisioningServiceImpl(DeviceRepository deviceRepository,
                                   ProvisioningProperty provisioningProperty) {
        this.deviceRepository = deviceRepository;
        this.provisioningProperty = provisioningProperty;
    }

    public String getProvisioningFile(String macAddress) throws DeviceNotFoundException {
        Device device = deviceRepository.findByMacAddress(macAddress)
                .orElseThrow(
                        () -> new DeviceNotFoundException(macAddress)
                );

        String path = device.getModel().equals(Device.DeviceModel.CONFERENCE) ?
                generateJSONFile(device) :
                generatePropertyFile(device);

        return path;
    }

    private String generatePropertyFile(Device device) {

        String absolutePath = "src/main/resources/files/desk/" + device.getMacAddress() + "-app-desk.properties";

        Properties props = new Properties();
        props.setProperty("port", provisioningProperty.getPort());
        props.setProperty("domain", provisioningProperty.getDomain());
        props.setProperty("codes", provisioningProperty.getCodecs());
        props.setProperty("timeout", provisioningProperty.getTimeout());
        props.setProperty("password", device.getPassword());
        props.setProperty("username", device.getUsername());
        device.setOverrideFragment(props.toString());

        try {
            File file = new File(absolutePath);
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            DefaultPropertiesPersister p = new DefaultPropertiesPersister();
            p.store(props, out, "Properties for mac Address");
            deviceRepository.save(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return absolutePath;
    }

    private String generateJSONFile(Device device) {

        String absolutePath = "src/main/resources/files/conference/" + device.getMacAddress() + "-app-conference.json";

        JSONObject obj = new JSONObject();
        obj.put("port", provisioningProperty.getPort());
        obj.put("domain", provisioningProperty.getDomain());
        obj.put("codes", provisioningProperty.getCodecs());
        obj.put("timeout", provisioningProperty.getTimeout());
        obj.put("password", device.getPassword());
        obj.put("username", device.getUsername());
        System.out.println(obj.toJSONString());
        device.setOverrideFragment(obj.toJSONString());

        try (FileWriter file = new FileWriter(absolutePath)) {
            file.write(obj.toJSONString());
            file.flush();
            deviceRepository.save(device);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return absolutePath;
    }

}
