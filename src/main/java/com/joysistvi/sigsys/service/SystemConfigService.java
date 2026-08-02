package com.joysistvi.sigsys.service;

public interface SystemConfigService {
    String getConfig(String key);
    boolean updateConfig(String key, String value);
}