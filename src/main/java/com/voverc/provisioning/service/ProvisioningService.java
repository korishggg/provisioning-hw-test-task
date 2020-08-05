package com.voverc.provisioning.service;

import com.voverc.provisioning.exception.DeviceNotFoundException;

public interface ProvisioningService {
    String getProvisioningFile(String macAddress) throws DeviceNotFoundException;
}
