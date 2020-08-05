package com.voverc.provisioning.exception;

public class DeviceNotFoundException extends Exception {
    private String exceptionDescription;

    public DeviceNotFoundException(String macAddress) {
        exceptionDescription = "Device with " + macAddress + " haven`t benn founded";
    }

    public String getExceptionDescription() {
        return exceptionDescription;
    }
}
