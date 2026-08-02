package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.model.SystemConfig;
import com.joysistvi.sigsys.service.SystemConfigService;
import com.joysistvi.sigsys.service.SystemConfigServiceImpl;
import java.util.List;

public class SystemConfigController {
    private final SystemConfigService configService = new SystemConfigServiceImpl();
    public String getConfig(String key) { return configService.getConfig(key); }
    public boolean updateConfig(String key, String value) { return configService.updateConfig(key, value); }
    public List<SystemConfig> getAllConfigs() { return configService.getAllConfigs(); }
}
